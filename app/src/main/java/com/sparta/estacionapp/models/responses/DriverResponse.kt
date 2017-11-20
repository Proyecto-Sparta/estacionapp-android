package com.sparta.estacionapp.models.responses

import java.io.Serializable

data class DriverResponse(var garage: Long,
                          var isAccepted: Boolean,
                          var floor: Long? = null,
                          var parkingSpace: String? = null) : Serializable
