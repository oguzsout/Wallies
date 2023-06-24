package com.oguzdogdu.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.oguzdogdu.domain.repository.Authenticator
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthenticatorImpl @Inject constructor(
    private val auth: FirebaseAuth,
) : Authenticator {
    override suspend fun isUserAuthenticatedInFirebase() = auth.currentUser != null

    override suspend fun signUp(userEmail: String, password: String) {
        auth.createUserWithEmailAndPassword(userEmail, password)
    }

    override suspend fun signIn(userEmail: String, password: String) {
        auth.signInWithEmailAndPassword(userEmail, password)
    }

    override suspend fun signOut() = auth.signOut()

    override fun getCurrentUserEmail() = auth.currentUser?.email ?: ""

}