package io.github.dracula101.todo.data.repository

import android.content.ContentProviderClient
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.util.Log
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInRequest.GoogleIdTokenRequestOptions
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import io.github.dracula101.blaze.common.utils.Resource
import java.util.concurrent.CancellationException
import javax.inject.Inject


class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val oneTapClient: SignInClient,
    private val context: Context
): AuthRepository{
    override val currentUser: User?
        get() = firebaseAuth.currentUser?.toUser()

    override suspend fun login(email: String, password: String): Resource<User> {
        return try {
            val authResult = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            Resource.Success(authResult.user!!.toUser())
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    private fun buildGoogleSignInRequest():BeginSignInRequest{
        return BeginSignInRequest.Builder()
            .setGoogleIdTokenRequestOptions(
                GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(context.getString(R.string.google_sign_in_client_id))
                    .build()
            )
            .setAutoSelectEnabled(true)
            .build()
    }

    override suspend fun signInWithGoogle(intent: Intent): Resource<User> {
        return try {
            val signInResult = oneTapClient.getSignInCredentialFromIntent(intent)
            val googleIdToken = signInResult.googleIdToken
            val googleAuthCredential = GoogleAuthProvider.getCredential(googleIdToken, null)
            val authResult = firebaseAuth.signInWithCredential(googleAuthCredential).await()
            Resource.Success(authResult.user!!.toUser())
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }


    override suspend fun getGoogleSignInIntent(): IntentSender {
        return try {
            oneTapClient.beginSignIn(buildGoogleSignInRequest()).await().pendingIntent.intentSender
        } catch (e: Exception) {
            e.printStackTrace()
            throw CancellationException(e.message)
        }
    }

    override suspend fun register(name: String, email: String, password: String): Resource<User> {
        return try {
            val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            Resource.Success(authResult.user!!.toUser())
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }



    override suspend fun logout() {
        oneTapClient.signOut().await()
        firebaseAuth.signOut()
    }

}