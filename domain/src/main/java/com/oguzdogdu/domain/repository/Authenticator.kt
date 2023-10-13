package com.oguzdogdu.domain.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.oguzdogdu.domain.model.auth.User
import com.oguzdogdu.domain.wrapper.Resource
import kotlinx.coroutines.flow.Flow

interface Authenticator {
    suspend fun isUserAuthenticatedInFirebase(): Boolean
    suspend fun isUserAuthenticatedWithGoogle(): Boolean
    suspend fun signUp(user: User, password: String) : User
    suspend fun changeUsername(name:String?)
    suspend fun changeSurname(surname:String?)
    suspend fun changeEmail(email:String?,password: String)
    suspend fun changeProfilePhoto(photo: String?)
    suspend fun addFavorites(id:String?,favorite: String?)
    suspend fun deleteFavorites(id: String?, favorite: String?)
    suspend fun forgotMyPassword(email: String?)
    suspend fun updatePassword(password: String?): Flow<Resource<Task<Void>?>>
    suspend fun signIn(userEmail: String, password: String):AuthResult
    suspend fun signInWithGoogle(idToken: String?):AuthResult
    suspend fun signOut()
    fun getCurrentUserEmail(): String
    suspend fun fetchUserInfos():User
}