package com.sparta.estacionapp.rest

import android.util.Log
import com.google.firebase.database.*

typealias GarageReservationResponse = (child: DataSnapshot) -> Unit

fun DatabaseReference.childAdded(callback: GarageReservationResponse) {
    val changeListener = object : ChildEventListener {
        override fun onChildMoved(p0: DataSnapshot?, p1: String?) {}

        override fun onChildChanged(p0: DataSnapshot?, p1: String?) {}

        override fun onChildAdded(p0: DataSnapshot?, p1: String?) {
            callback.invoke(p0!!)
            removeEventListener(this)
        }

        override fun onChildRemoved(p0: DataSnapshot?) {}

        override fun onCancelled(p0: DatabaseError?) {
            Log.i("FireBase/GarageResponse", "Cancelling")
        }
    }
    addChildEventListener(changeListener)
}
