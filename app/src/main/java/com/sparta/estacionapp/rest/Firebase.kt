package com.sparta.estacionapp.rest

import android.util.Log
import com.google.firebase.database.*
import com.sparta.estacionapp.models.responses.DriverResponse

typealias DriverReservationResponse = (child: DriverResponse) -> Unit

fun DatabaseReference.childAdded(callback: DriverReservationResponse) {
    val changeListener = object : ValueEventListener {
        override fun onDataChange(data: DataSnapshot?) {
            if (data!!.value == null) return
            val driverResponse = DriverResponse(
                    garage = data.child("garage").value as Long,
                    isAccepted = data.child("isAccepted").value as Boolean,
                    floor = data.child("floor").value as Long?,
                    parkingSpace = data.child("parkingSpace").value as String?)
            data.ref.removeValue();
            removeEventListener(this)
            callback.invoke(driverResponse)
        }

        override fun onCancelled(p0: DatabaseError?) {
            Log.i("FireBase/GarageResponse", "Cancelling")
        }
    }
    addValueEventListener(changeListener)
}
