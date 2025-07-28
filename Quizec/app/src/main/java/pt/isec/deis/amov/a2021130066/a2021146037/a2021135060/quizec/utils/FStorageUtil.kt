package pt.isec.deis.amov.a2021130066.a2021146037.a2021135060.quizec.utils

import android.content.res.AssetManager
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await
import pt.isec.deis.amov.a2021130066.a2021146037.a2021135060.quizec.ui.screens.QuestionsType
import pt.isec.deis.amov.a2021130066.a2021146037.a2021135060.quizec.ui.viewmodels.LISTAPERGUNTAS
import pt.isec.deis.amov.a2021130066.a2021146037.a2021135060.quizec.ui.viewmodels.LISTAQUESTIONARIO
import pt.isec.deis.amov.a2021130066.a2021146037.a2021135060.quizec.ui.viewmodels.Question
import pt.isec.deis.amov.a2021130066.a2021146037.a2021135060.quizec.ui.viewmodels.Questionario
import pt.isec.deis.amov.a2021130066.a2021146037.a2021135060.quizec.ui.viewmodels.TIMEQUEST
import pt.isec.deis.amov.a2021130066.a2021146037.a2021135060.quizec.ui.viewmodels.TimeQuest
import java.io.IOException
import java.io.InputStream

class FStorageUtil {
    companion object {
        fun addPerguntaToFireBase(pergunta: Question, onResult: (Throwable?) -> Unit) {
            val q = hashMapOf(
                "ID" to pergunta.uniqueCode,
                "id_questionario" to pergunta.idQuestionario,
                "imageUrl" to pergunta.serverPhotoUrl,
                "options" to pergunta.options,
                "pergunta" to pergunta.title,
                "quemRespondeu" to pergunta.quemRespondeu,
                "respostas" to pergunta.respostas,
                "tipo" to pergunta.questionType
            )
            pergunta.uniqueCode.let {
                Firebase.firestore.collection(LISTAPERGUNTAS).document(it).set(q)
                    .addOnCompleteListener { result ->
                        onResult(result.exception)
                    }
            }
        }

        fun updatePerguntaInFirestoreTrans(pergunta: Question, onResult: (Throwable?) -> Unit) {
            val db = Firebase.firestore
            val v = db.collection(LISTAPERGUNTAS).document("Perguntas")

            db.runTransaction { transaction ->
                if (transaction.get(v).exists()) {
                    transaction.update(v, "pergunta", pergunta.title)
                    transaction.update(v, "quemRespondeu", pergunta.quemRespondeu)
                    transaction.update(v, "respostas", pergunta.respostas)
                    transaction.update(v, "tipo", pergunta.questionType)
                    null
                } else
                    throw FirebaseFirestoreException(
                        "Doesn't exist",
                        FirebaseFirestoreException.Code.UNAVAILABLE
                    )
            }.addOnCompleteListener { result ->
                onResult(result.exception)
            }
        }
        fun removePerguntaFromFirestore(pergunta: String, onResult: (Throwable?) -> Unit) {
            val db = Firebase.firestore
            val v = db.collection(LISTAPERGUNTAS).document(pergunta)

            v.delete()
                .addOnCompleteListener { onResult(it.exception) }
        }
        suspend fun getAnswersByID(id:String):List<Question>{
            val answersList = mutableListOf<Question>()
            Log.d("Firestore", "Starting getAnswersByID for ID: $id")
            try {
                val questions = Firebase.firestore.collection(LISTAPERGUNTAS)
                    .whereEqualTo("id_questionario", id)
                    .get()
                    .await()

                for (document in questions.documents) {
                    Log.d("Firestore", "Document data: ${document.data}")
                    Log.d("Firestore", "Document found for ID: $id")

                    val question = Question(
                        title = document.getString("pergunta") ?: "",
                        questionType = QuestionsType.valueOf( document.getString("tipo") ?: ""), //NOT WORKING
                        options = document.get("options") as? ArrayList<*>,
                        serverPhotoUrl = document.getString("imageUrl"),
                        uniqueCode = document.getString("ID") ?: "",
                        quemRespondeu = document.get("quemRespondeu") as? ArrayList<String> ?: null,
                        respostas = document.get("respostas") as? ArrayList<*>,
                        idQuestionario = id,
                    )
                    answersList.add(question)
                    Log.d("Firestore", "Question added to list: $question")
                }
            } catch (e: Exception) {
                Log.e("Firebase", "Erro ao recuperar as respostas com ID: $id", e)
            }
            Log.d("Firestore", "Size answer: ${answersList.size}")
            Log.d("Firestore", "LIST $answersList")
            return answersList
        }

        private var listenerPRegistration: ListenerRegistration? = null
        private var listenerForListPRegistration: ListenerRegistration? = null
        private var listenerTimeRegistration: ListenerRegistration? = null

        fun addQuestionarioToFireBase(quest: Questionario, onResult: (Throwable?) -> Unit) {
            val q = hashMapOf(
                "ID" to quest.id,
                "active" to quest.active,
                "criadoPor" to quest.criadorPor,
                "data" to quest.data,
                "imageUrl" to quest.imageUrl,
                "nPerguntas" to quest.nPerguntas,
                "titulo" to quest.titulo,
                "respostas" to quest.respostas,
                "location" to quest.location
            )
            quest.id?.let {
                Firebase.firestore.collection(LISTAQUESTIONARIO).document(it).set(q)
                    .addOnCompleteListener { result ->
                        onResult(result.exception)
                    }
            }
        }
        suspend fun getQuestionarioByID(id: String): Questionario {
            val aux = Questionario()
            try {
                val docRef = Firebase.firestore.collection(LISTAQUESTIONARIO).document(id)
                val document = docRef.get().await()
                if (document.exists()) {
                    aux.id = document.getString("ID") ?: ""
                    aux.titulo = document.getString("titulo") ?: ""
                    aux.active = document.getBoolean("active") ?: false
                    aux.data = document.getString("data") ?: ""
                    aux.nPerguntas = document.getLong("nPerguntas")?.toInt() ?: 0
                    aux.imageUrl = document.getString("imageUrl") ?: ""
                    aux.respostas = document.get("respostas") as ArrayList<String>?
                    aux.location = document.getString("location")
                }
            } catch (e: Exception) {
                Log.e("Firebase", "Erro ao recuperar o questionÃ¡rio com ID: $id", e)
            }
            return aux
        }
        fun atualizaFireBase(questionario: Questionario) {
            val docRef = questionario.id?.let {
                Firebase.firestore.collection(LISTAQUESTIONARIO).document(it)
            }
            docRef?.get()?.addOnSuccessListener { document ->
                if (document.exists()) {
                    val updatedData = hashMapOf(
                        "respostas" to questionario.respostas,
                        "location" to questionario.location
                    )
                    docRef.update(updatedData as Map<String, Any>).addOnCompleteListener { task ->
                        if (task.isSuccessful) Log.d("Firestore", "Questionario updated successfully")
                        else Log.e("Firestore", "Error updating questionario", task.exception)
                    }
                } else
                    Log.e("Firestore", "No such document to update")
            }?.addOnFailureListener { e -> Log.e("Firestore", "Error getting document: ", e) }
        }
        fun removeQuestionarioFromFirestore(questionario: String, onResult: (Throwable?) -> Unit) {
            val db = Firebase.firestore
            val v = db.collection(LISTAQUESTIONARIO).document(questionario)

            v.delete()
                .addOnCompleteListener { onResult(it.exception) }
        }

        fun startObserverOfPergunta(lpergunta: String, onNewValues: (Question) -> Unit) {
            stopObserverOfPergunta()
            listenerPRegistration =  Firebase.firestore.collection(LISTAPERGUNTAS).document(lpergunta)
                .addSnapshotListener { docSS, e ->
                    if (e != null) {
                        return@addSnapshotListener
                    }
                    if (docSS != null && docSS.exists()) {
                        val idQuestionario = docSS.getString("id_questionario") ?: ""
                        val pergunta = docSS.getString("pergunta") ?: ""
                        val tipo = docSS.getString("tipo") ?: ""
                        val imageUrl = docSS.getString("imageUrl")
                        val id = docSS.getString("ID") ?: ""
                        val options = docSS.get("options") as? ArrayList<*>
                        val quemRespondeu = docSS.get("quemRespondeu") as? ArrayList<String>
                        val respostas = docSS.get("respostas") as? ArrayList<String>

                        val p = Question(
                            title = pergunta,
                            questionType = TODO(),//tipo,
                            options = options,
                            serverPhotoUrl = imageUrl,
                            uniqueCode = id,
                            quemRespondeu = quemRespondeu,
                            respostas = respostas,
                            idQuestionario = idQuestionario
                        )
                        onNewValues(p)
                    }
                }
        }
        fun stopObserverOfPergunta() {
            listenerPRegistration?.remove()
        }
        fun startObserverOfListQuest(onNewValues: (List<Questionario>) -> Unit) {
            stopObserverOfListQuest()
            val db = Firebase.firestore
            listenerForListPRegistration = db.collection(LISTAQUESTIONARIO)
                .addSnapshotListener { docSS, e ->
                    if (e != null) {
                        Log.e("Firestore", "Erro ao obter questionÃ¡rios", e)
                        return@addSnapshotListener
                    }
                    if (docSS != null && !docSS.isEmpty) {
                        val questionarioList = docSS.documents.mapNotNull { d ->
                            val questionario = Questionario(
                                id = d.getString("ID"),
                                active = d.getBoolean("active"),
                                criadorPor = d.getString("criadoPor"),
                                data = d.getString("data"),
                                imageUrl = d.getString("imageUrl"),
                                nPerguntas = d.getLong("nPerguntas")?.toInt(),
                                titulo = d.getString("titulo"),
                                respostas = d.get("respostas") as ArrayList<String>?,
                                location = d.getString("location")
                            )
                            questionario
                        }
                        onNewValues(questionarioList)
                    } else {
                        Log.d("Firestore", "Nenhum questionÃ¡rio encontrado.")
                        onNewValues(emptyList())
                    }
                }
        }
        fun stopObserverOfListQuest() {
            listenerForListPRegistration?.remove()
        }

        fun startObserverOfTimeQuest(onNewValues: (List<TimeQuest>) -> Unit) {
            stopObserverOfTime()
            val db = Firebase.firestore
            listenerTimeRegistration = db.collection(TIMEQUEST)
                .addSnapshotListener { docSS, e ->
                    if (e != null) {
                        Log.e("Firestore", "Erro ao obter questionÃ¡rios", e)
                        return@addSnapshotListener
                    }
                    if (docSS != null) {
                        val timeStamp = docSS.mapNotNull { d ->
                            val tempo = TimeQuest(
                                id = d.getString("ID"),
                                tempolimite = d.getTimestamp("tempo_limite")
                            )
                            tempo
                        }
                        onNewValues(timeStamp)
                    } else {
                        Log.d("Firestore", "Nenhum questionÃ¡rio encontrado.")
                        onNewValues(emptyList())
                    }
                }
        }

        suspend fun getTimeQuestByID(id: String): TimeQuest { // Devolve o tempo do questinario
            val aux = TimeQuest()
            try {
                val docRef = Firebase.firestore.collection(TIMEQUEST).document(id)
                val document = docRef.get().await()
                if (document.exists()) {
                    aux.id = document.getString("ID") ?: ""
                    aux.tempolimite = document.getTimestamp("tempo_limite")
                }
            } catch (e: Exception) {
                Log.e("Firebase", "Erro ao saber tempo do questionÃ¡rio com ID: $id", e)
            }
            return aux
        }

        fun addTimeQuestToFireBase(time: TimeQuest, onResult: (Throwable?) -> Unit) {
            val q = hashMapOf(
                "ID" to time.id,
                "tempo_limite" to time.tempolimite
            )
            time.id?.let {
                Firebase.firestore.collection(TIMEQUEST).document(it).set(q)
                    .addOnCompleteListener { result ->
                        onResult(result.exception)
                    }
            }
        }

        fun updateTimeQuestInFirestore(quest: TimeQuest, onResult: (Throwable?) -> Unit) {
            val db = Firebase.firestore
            try {
                val v = db.collection(TIMEQUEST).document(quest.id!!)

                db.runTransaction { transaction ->
                    if (transaction.get(v).exists()) {
                        transaction.update(v, "tempo_limite", quest.tempolimite)
                        null
                    } else
                        throw FirebaseFirestoreException(
                            "Doesn't exist",
                            FirebaseFirestoreException.Code.UNAVAILABLE
                        )
                }.addOnCompleteListener { result ->
                    onResult(result.exception)
                }
            } catch (e: Exception) {
                Log.e("Update", e.toString())
            }
        }

        fun stopObserverOfTime() {
            listenerTimeRegistration?.remove()
        }

        // Storage
        fun getFileFromAsset(assetManager: AssetManager, strName: String): InputStream? {
            var inputStream: InputStream? = null
            try {
                inputStream = assetManager.open(strName)
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return inputStream
        }
        fun uploadFile(inputStream: InputStream, imgFile: String) {
            val storage = Firebase.storage
            val ref1 = storage.reference
            val ref2 = ref1.child("images")
            val ref3 = ref2.child(imgFile)

            val uploadTask = ref3.putStream(inputStream)
            uploadTask.continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                ref3.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result
                    println(downloadUri.toString())
                    // something like:
                    //   https://firebasestorage.googleapis.com/v0/b/p0405ansamov.appspot.com/o/images%2Fimage.png?alt=media&token=302c7119-c3a9-426d-b7b4-6ab5ac25fed9
                } else {
                    // Handle failures
                    // ...
                }
            }


        }
        //https://firebase.google.com/docs/storage/android/upload-files
    }
}