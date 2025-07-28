package pt.isec.deis.amov.a2021130066.a2021146037.a2021135060.quizec.ui.viewmodels

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import pt.isec.deis.amov.a2021130066.a2021146037.a2021135060.quizec.utils.FAuthUtil
import pt.isec.deis.amov.a2021130066.a2021146037.a2021135060.quizec.utils.FStorageUtil
import java.time.LocalTime
import java.util.Calendar

const val LISTAPERGUNTAS = "ListaPerguntas"
const val LISTAQUESTIONARIO = "ListaQuestionarios"
const val TIMEQUEST = "TempoLimite"

data class User(val name: String, val email:String, val picture:String?)

fun FirebaseUser.toUser() : User {
    val displayName = this.displayName ?: ""
    val strEmail = this.email ?: "n.d."
    val picture = this.photoUrl?.toString()
    return User(displayName,strEmail,picture)
}

class FirebaseViewModel : ViewModel() {
    private val _user = mutableStateOf(FAuthUtil.currentUser?.toUser())
    val user : MutableState<User?> get() = _user

    private val _error = mutableStateOf<String?>(null)
    val error : MutableState<String?> get() = _error

    fun createUserWithEmail(email: String, password: String) {
        if (email.isBlank() || password.isBlank())
            return

        viewModelScope.launch {
            FAuthUtil.createUserWithEmail(email, password) { exception ->
                if (exception == null)
                    _user.value = FAuthUtil.currentUser?.toUser()
                _error.value = exception?.message
            }
        }
    }
    fun signInWithEmail(email: String, password: String) {
        if (email.isBlank() || password.isBlank())
            return
        viewModelScope.launch {
            FAuthUtil.signInWithEmail(email, password) { exception ->
                if (exception == null)
                    _user.value = FAuthUtil.currentUser?.toUser()
                _error.value = exception?.message
            }
        }
    }
    fun signOut() {
        FAuthUtil.signOut()
        _user.value = null
        _error.value = null
    }

    // FIRESTORE AND STORAGE
    private val _pergunta = mutableStateOf("")
    val pergunta : State<String> get() = _pergunta

    private val _quemRespondeu = mutableListOf("")
    val quemRespondeu : MutableList<String> get() = _quemRespondeu

    private val _respostas = mutableListOf("")
    val respostas : MutableList<String> get() = _respostas

    private val _tipo = mutableStateOf("")
    val tipo : State<String> get() = _tipo

    // Guarda todos os questinarios
    private var _allquestionarios = MutableStateFlow<List<Questionario>>(emptyList())
    val allquestinarios : StateFlow<List<Questionario>> get() = _allquestionarios

    private var _questionario = MutableStateFlow(Questionario())
    val newQuestinario : StateFlow<Questionario> get() = _questionario

    private var _questionarioWatch = MutableStateFlow(Questionario())
    val questinarioOnWatch : StateFlow<Questionario> get() = _questionarioWatch


    // Todos os tempos de perguntas
    private var _allTimeQuests = MutableStateFlow<List<TimeQuest>>(emptyList())
    val allTimeQuests : StateFlow<List<TimeQuest>> get() = _allTimeQuests

    private var _time = MutableStateFlow(TimeQuest())
    val newTimeQuest : StateFlow<TimeQuest> get() = _time

    private var _timetoAdd = mutableIntStateOf(0)
    val timetoAdd : MutableState<Int> get() = _timetoAdd

    fun prepareTime(): Timestamp {
        val actualTimeOfQuest = Timestamp.now().toDate()
        val calendar = Calendar.getInstance()
        calendar.time = actualTimeOfQuest
        calendar.add(Calendar.MINUTE, _timetoAdd.intValue)
        return Timestamp(calendar.time)
    }

    private val _timeRemaining = mutableStateOf("")
    val timeRemaining: State<String> get() = _timeRemaining

    suspend fun timeIsClocking(targetTimestamp: Timestamp): Boolean {
        val targetTime = targetTimestamp.toDate().time
        while (System.currentTimeMillis() < targetTime) {
            val remainingTime = targetTime - System.currentTimeMillis()
            _timeRemaining.value = formatTime(remainingTime) // Atualizar UI
            delay(1000L)
        }
        return true
    }
    private fun formatTime(milliseconds: Long): String {
        val seconds = (milliseconds / 1000) % 60
        val minutes = (milliseconds / (1000 * 60)) % 60
        return String.format(buildString { append("%02d:%02d") }, minutes, seconds)
    }

    fun changeQuestOnWatch(newValue: Questionario) {
        _questionarioWatch.value = newValue
    }

    fun addPerguntaToFirestore(pergunta: Question) {
        viewModelScope.launch {
            FStorageUtil.addPerguntaToFireBase(pergunta) { exception ->
                _error.value = exception?.message
            }
        }
    }

    fun updatePerguntaInFirestore(pergunta: Question) {
        viewModelScope.launch {
            //FirebaseUtils.updateDataInFirestore()
            FStorageUtil.updatePerguntaInFirestoreTrans(pergunta) { exception ->
                _error.value = exception?.message
            }
        }
    }
    fun removePerguntaFromFirestore(pergunta: String) {
        viewModelScope.launch {
            FStorageUtil.removePerguntaFromFirestore(pergunta) { exception ->
                _error.value = exception?.message
            }
        }
    }
    fun startObserverOfPergunta(pergunta: String) {
        viewModelScope.launch {
            FStorageUtil.startObserverOfPergunta(pergunta) { p ->
                _pergunta.value = p.title
                for (newItem in p.quemRespondeu!!)
                    for (item in _quemRespondeu)
                        if (newItem != item)
                            _quemRespondeu.add(newItem.toString())
                for (newItem in p.respostas!!)
                    for (item in _respostas)
                        if (newItem != item)
                            _respostas.add(newItem.toString())
                //   _tipo.value = p.tipo
            }
        }
    }

    fun startObserverOfTimeQuest() {
        viewModelScope.launch {
            FStorageUtil.startObserverOfTimeQuest { p ->
                _allTimeQuests.value = p
            }
        }
    }
    fun stopObserverOfTimeQuest() {
        viewModelScope.launch {
            FStorageUtil.stopObserverOfTime()
        }
    }

    fun getTimeQuestByID(id:String):TimeQuest {
        return runBlocking {
            val timeQ = FStorageUtil.getTimeQuestByID(id)
            timeQ
        }
    }

    fun addTimeQuestToFirestore(timeQuest: TimeQuest) {
        viewModelScope.launch {
            FStorageUtil.addTimeQuestToFireBase(timeQuest) { exception ->
                _error.value = exception?.message
            }
        }
    }

    fun updateTimeQuestToFirestore(timeQuest: TimeQuest){
        viewModelScope.launch {
            FStorageUtil.updateTimeQuestInFirestore(timeQuest) { exception ->
                _error.value = exception?.message
            }
        }
    }

    fun getAnswersByID(id:String):List<Question>{
        return runBlocking {
            val answer = FStorageUtil.getAnswersByID(id)
            answer
        }
    }

    // Questionario
    fun addQuestionarioToFirestore(questionario: Questionario) {
        viewModelScope.launch {
            FStorageUtil.addQuestionarioToFireBase(questionario) { exception ->
                _error.value = exception?.message
            }
        }
    }
    fun atualizaFireBase(questionario: Questionario){
        FStorageUtil.atualizaFireBase(questionario)
    }
    fun getQuestionarioByID(id: String): Questionario {
        return runBlocking {
            val questionario = FStorageUtil.getQuestionarioByID(id)
            questionario
        }
    }
    fun removeQuestionarioFromFirestore(questionario: String) {
        viewModelScope.launch {
            FStorageUtil.removeQuestionarioFromFirestore(questionario) { exception ->
                _error.value = exception?.message
            }
        }
    }

    fun startObserverOfListQuest() {
        viewModelScope.launch {
            FStorageUtil.startObserverOfListQuest { p ->
                _allquestionarios.value = p
            }
        }
    }
    fun stopObserverOfPergunta() {
        viewModelScope.launch {
            FStorageUtil.stopObserverOfPergunta()
        }
    }
    fun stopObserverOfListQuest() {
        viewModelScope.launch {
            FStorageUtil.stopObserverOfListQuest()
        }
    }
    fun uploadToStorage(context : Context) {
        viewModelScope.launch {
            FStorageUtil.getFileFromAsset(context.assets,"images/img.png")?.let { inputStream ->
                FStorageUtil.uploadFile(inputStream,"image.png")
            }
        }
    }
}