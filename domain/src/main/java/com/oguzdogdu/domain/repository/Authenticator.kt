package com.oguzdogdu.domain.repository

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import com.oguzdogdu.domain.model.auth.User

interface Authenticator {
    suspend fun isUserAuthenticatedInFirebase(): Boolean
    suspend fun signUp(user: User, password: String) : User
    suspend fun signIn(userEmail: String, password: String):AuthResult
    suspend fun signOut()
    fun getCurrentUserEmail(): String
}