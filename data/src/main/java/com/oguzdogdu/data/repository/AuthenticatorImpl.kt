package com.oguzdogdu.data.repository

import android.R.attr.password
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.oAuthCredential
import com.google.firebase.firestore.FirebaseFirestore
import com.oguzdogdu.data.common.Constants.COLLECTION_PATH
import com.oguzdogdu.data.common.Constants.EMAIL
import com.oguzdogdu.data.common.Constants.ID
import com.oguzdogdu.data.common.Constants.IMAGE
import com.oguzdogdu.data.common.Constants.NAME
import com.oguzdogdu.data.common.Constants.SURNAME
import com.oguzdogdu.domain.repository.Authenticator
import com.oguzdogdu.domain.wrapper.Resource
import com.oguzdogdu.domain.wrapper.toResource
import com.oguzdogdu.network.model.auth.User
import com.oguzdogdu.network.model.auth.toUserDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
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
        user.email?.let { auth.createUserWithEmailAndPassword(it, password).await() }
        val userModel = hashMapOf(
            ID to auth.currentUser?.uid,
            EMAIL to user.email,
            NAME to user.name,
            SURNAME to user.surname,
            IMAGE to user.image
        )
        auth.currentUser?.uid?.let {
            firebaseFirestore.collection(COLLECTION_PATH).document(it)
                .set(userModel)
        }?.await()
        val result = User(
            name = userModel.getOrDefault(key = NAME, defaultValue = null).toString(),
            surname = userModel.getOrDefault(key = SURNAME, defaultValue = null).toString(),
            email = userModel.getOrDefault(key = EMAIL, defaultValue = null).toString(),
            image = userModel.getOrDefault(key = IMAGE,defaultValue = null).toString()
        )
        return result.toUserDomain()
    }

    override suspend fun changeUsername(name: String?) {
        auth.currentUser?.uid?.let {
            firebaseFirestore.collection(COLLECTION_PATH).document(it).update(NAME, name)
        }?.await()
    }

    override suspend fun changeSurname(surname: String?) {
        auth.currentUser?.uid?.let {
            firebaseFirestore.collection(COLLECTION_PATH).document(it).update(SURNAME, surname)
        }?.await()
    }

    override suspend fun changeEmail(email: String?, password: String) {
        val credential =
            auth.currentUser?.email?.let { EmailAuthProvider.getCredential(it, password) }
        credential?.let { credential ->
            auth.currentUser?.reauthenticate(credential)?.await()
            auth.currentUser?.updateEmail(email.orEmpty())?.await()
            firebaseFirestore.collection(COLLECTION_PATH).document(auth.currentUser!!.uid)
                .update(EMAIL, email).await()
        }
    }

    override suspend fun changeProfilePhoto(photo: String?) {
        auth.currentUser?.uid?.let {
            firebaseFirestore.collection(COLLECTION_PATH).document(it).update(IMAGE, photo)
        }?.await()
    }


    override suspend fun signIn(userEmail: String, password: String):AuthResult {
      return  auth.signInWithEmailAndPassword(userEmail, password).await()
    }

    override suspend fun signOut() = auth.signOut()

    override fun getCurrentUserEmail() = auth.currentUser?.email ?: ""

    override suspend fun fetchUserInfos(): com.oguzdogdu.domain.model.auth.User {
        val user = FirebaseAuth.getInstance().currentUser
        val id = user?.uid ?: ""

        val db = FirebaseFirestore.getInstance()
        val userDocument = db.collection(COLLECTION_PATH).document(id).get().await()

        val name = userDocument?.getString(NAME)
        val email = userDocument?.getString(EMAIL)
        val profileImageUrl = userDocument?.getString(IMAGE)
        val surname = userDocument?.getString(SURNAME)

        val result = User(name = name, surname = surname, email = email, image = profileImageUrl)
        return result.toUserDomain()
    }
}