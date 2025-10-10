package br.com.chase.data

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await

class FirebaseAuthRepository(private val context: Context) {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val googleClient: GoogleSignInClient by lazy { buildGoogleClient() }

    suspend fun signInWithGoogle(data: android.content.Intent?): FirebaseUser? {
        signOut() // Limpando possivel estado anterior de usuário...

        val account = GoogleSignIn.getSignedInAccountFromIntent(data)
            .getResult(Exception::class.java)

        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        val result = firebaseAuth.signInWithCredential(credential).await()
        return result.user
    }

    fun getSignInIntent() = googleClient.signInIntent

    fun signOut() {
        googleClient.signOut()
        firebaseAuth.signOut()
    }

    private fun buildGoogleClient(): GoogleSignInClient {
        val webClientId = getWebClientId()
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(webClientId)
            .requestEmail()
            .build()
        return GoogleSignIn.getClient(context, gso)
    }

    private fun getWebClientId(): String {
        val id = context.resources.getIdentifier("default_web_client_id", "string", context.packageName)
        return if (id != 0) context.getString(id) else ""
    }
}
