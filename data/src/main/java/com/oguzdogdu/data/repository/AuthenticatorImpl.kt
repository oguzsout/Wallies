package com.oguzdogdu.data.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.oguzdogdu.data.common.Constants.COLLECTION_PATH
import com.oguzdogdu.data.common.Constants.EMAIL
import com.oguzdogdu.data.common.Constants.FAVORITES
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
    override suspend fun isUserAuthenticatedWithGoogle(): Boolean {
        val user = FirebaseAuth.getInstance().currentUser
        return user?.providerData?.any { it.providerId == GoogleAuthProvider.PROVIDER_ID } == true
    }

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
            IMAGE to user.image,
            FAVORITES to user.favorites
        )
        auth.currentUser?.uid?.let {
            firebaseFirestore.collection(COLLECTION_PATH).document(it)
                .set(userModel)
        }?.await()
        val result = User(
            name = userModel.get(key = NAME).toString(),
            surname = userModel.get(key = SURNAME).toString(),
            email = userModel.get(key = EMAIL).toString(),
            image = userModel.get(key = IMAGE).toString(),
            favorites = userModel.get(key = FAVORITES) as? List<HashMap<String, String>> ?: emptyList()
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

    override suspend fun addFavorites(id: String?, favorite: String?) {
        auth.currentUser?.uid?.let { userId ->
            val userDocRef = firebaseFirestore.collection(COLLECTION_PATH).document(userId)
            userDocRef.get().addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val currentFavorites = documentSnapshot[FAVORITES] as? List<HashMap<String, String>>
                    val updatedFavorites = mutableListOf<HashMap<String, String>>()

                    if (currentFavorites != null) {
                        updatedFavorites.addAll(currentFavorites)
                    }

                    val newFavorite = hashMapOf(
                        "id" to id.orEmpty(),
                        "favorite" to favorite.orEmpty()
                    )

                    updatedFavorites.add(newFavorite)

                    val dataToUpdate = mapOf(FAVORITES to updatedFavorites)

                    userDocRef.update(dataToUpdate)
                }
            }
        }
    }

    override suspend fun deleteFavorites(id: String?, favorite: String?) {
        auth.currentUser?.uid?.let { userId ->
            if (id == null && favorite == null) {
                return
            }

            val userDocRef = firebaseFirestore.collection(COLLECTION_PATH).document(userId)
            userDocRef.get().addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val currentFavorites =
                        documentSnapshot[FAVORITES] as? List<HashMap<String, String>>

                    if (currentFavorites != null) {
                        val updatedFavorites = currentFavorites.toMutableList()

                        updatedFavorites.filterIndexed { index, hashMap ->
                            hashMap["id"] != id && hashMap["favorite"] != favorite
                        }.toMutableList().also { filteredList ->
                            if (filteredList.size != updatedFavorites.size) {
                                val dataToUpdate = mapOf(FAVORITES to filteredList)
                                userDocRef.update(dataToUpdate)
                            }
                        }
                    }
                }
            }
        }
    }

    override suspend fun forgotMyPassword(email: String?) {
        email?.let { auth.sendPasswordResetEmail(it).await() }
    }

    override suspend fun updatePassword(password: String?) : Flow<Resource<Task<Void>?>> = flow {
        password?.let { newPassword ->
            val updateTask = auth.currentUser?.updatePassword(newPassword)
            emit(updateTask)
        }
    }.toResource()

    override suspend fun signIn(userEmail: String, password: String):AuthResult {
      return  auth.signInWithEmailAndPassword(userEmail, password).await()
    }

    override suspend fun signInWithGoogle(idToken: String?): AuthResult {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        return auth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = FirebaseAuth.getInstance().currentUser
                val userModel = hashMapOf(
                    ID to auth.currentUser?.uid,
                    EMAIL to user?.email.orEmpty(),
                    NAME to user?.displayName.orEmpty(),
                    IMAGE to user?.photoUrl.toString()
                )
                auth.currentUser?.uid?.let {
                    firebaseFirestore.collection(COLLECTION_PATH).document(it)
                        .set(userModel)
                }
            }
        }.await()
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
        val favorites = userDocument?.get(FAVORITES) as List<HashMap<String, String>>

        val result = User(name = name, surname = surname, email = email, image = profileImageUrl, favorites = favorites)
        return result.toUserDomain()
    }
}