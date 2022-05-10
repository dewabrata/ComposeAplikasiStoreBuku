package com.adl.aplikasistorebuku.repo

import android.util.Log
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

class BooksRepo {

    private val firestore = FirebaseFirestore.getInstance()


    fun getBookDetails() = callbackFlow<BookResponse> {

        val collection = firestore.collection("book")
        val snapshotListener = collection.addSnapshotListener{value,error ->
            run {

                Log.d("Error", error.toString())
                val response = if (error == null) {
                    OnSuccess(value)
                } else {
                    OnFailure(error)
                }

                trySend(response).isSuccess
            }


        }
        awaitClose{
                snapshotListener.remove()
        }

    }

}