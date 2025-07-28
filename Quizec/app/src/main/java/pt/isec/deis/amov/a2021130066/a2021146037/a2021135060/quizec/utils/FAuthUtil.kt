package pt.isec.deis.amov.a2021130066.a2021146037.a2021135060.quizec.utils
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.Date

data class MeetingPoint(
    val latitude: Double,
    val longitude: Double
)

class FAuthUtil {
    companion object {
        private val auth by lazy { Firebase.auth }

        private val history: MutableList<MeetingPoint> = mutableListOf()

        val currentUser: FirebaseUser?
            get() = auth.currentUser

        fun createUserWithEmail(email: String, password: String,
                                onResult: (Throwable?) -> Unit) {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { result ->
                    onResult(result.exception)
                }
        }

        fun signInWithEmail(email: String, password: String, onResult: (Throwable?) -> Unit) {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { result ->
                    onResult(result.exception)
                }
        }

        fun signOut() {
            if (auth.currentUser != null) {
                auth.signOut()
            }
        }

        fun addMeetingPoint(latitude: Double, longitude: Double) {
            history.add(MeetingPoint(latitude, longitude))
        }
    }
}