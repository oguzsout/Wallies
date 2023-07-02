package com.oguzdogdu.data.repository

import android.os.Build
import android.provider.ContactsContract.DisplayNameSources.NICKNAME
import android.provider.SimPhonebookContract.SimRecords.PHONE_NUMBER
import androidx.annotation.RequiresApi
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.oguzdogdu.data.common.Constants.COLLECTION_PATH
import com.oguzdogdu.data.common.Constants.EMAIL
import com.oguzdogdu.data.common.Constants.ID
import com.oguzdogdu.data.common.Constants.NAME
import com.oguzdogdu.data.common.Constants.SURNAME
import com.oguzdogdu.data.model.auth.User
import com.oguzdogdu.data.model.auth.toUserDomain
import com.oguzdogdu.domain.repository.Authenticator
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthenticatorImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore,
) : Authenticator {
    override suspend fun isUserAuthenticatedInFirebase() = auth.currentUser != null

    @RequiresApi(Build.VERSION_CODES.N)
    override suspend fun signUp(
        user: com.oguzdogdu.domain.model.auth.User,
        password: String,
    ): com.oguzdogdu.domain.model.auth.User {
        auth.createUserWithEmailAndPassword(user.email, password)
        val userModel = hashMapOf(
            ID to auth.currentUser?.uid,
            EMAIL to user.email,
            NAME to user.name,
            SURNAME to user.surname
        )
        auth.currentUser?.uid?.let {
            firebaseFirestore.collection(COLLECTION_PATH).document(it)
                .set(userModel)
        }
        val result = User(
            name = userModel.getOrDefault(key = NAME, defaultValue = null).toString(),
            surname = userModel.getOrDefault(key = SURNAME, defaultValue = null).toString(),
            email = userModel.getOrDefault(key = EMAIL, defaultValue = null).toString()
        )
        return result.toUserDomain()
    }


    override suspend fun signIn(userEmail: String, password: String):AuthResult {
      return  auth.signInWithEmailAndPassword(userEmail, password).await()
    }

    override suspend fun signOut() = auth.signOut()

    override fun getCurrentUserEmail() = auth.currentUser?.email ?: ""

}