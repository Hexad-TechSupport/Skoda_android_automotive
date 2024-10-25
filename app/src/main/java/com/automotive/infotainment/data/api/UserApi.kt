package com.automotive.infotainment.data.api


import com.automotive.infotainment.data.model.VehicleData
import retrofit2.Call
import retrofit2.http.GET

interface UserApi {
    @get:GET("path/api")
    val vehicleData: Call<VehicleData?>?
}
