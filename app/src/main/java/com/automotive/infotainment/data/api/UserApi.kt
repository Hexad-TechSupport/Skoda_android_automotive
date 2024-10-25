package com.automotive.infotainment.data.api

import com.automotive.infotainment.data.model.VehicleData
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface UserApi {
    @GET("vehicles")
    fun getVehicleData(
        @Query("vehicleId") vehicleId: String,
        @Query("credentialId") credentialId: String
    ): Call<VehicleData>

    @POST("vehicles/speed")
    fun updateSpeedDisplay(
        @Query("vehicleId") vehicleId: String,
        @Query("credentialId") credentialId: String,
        @Body speed: Float
    ): Call<Void>

    @POST("vehicles/doorLockStatus/open")
    fun updateDoorLockOpen(
        @Query("vehicleId") vehicleId: String,
        @Query("credentialId") credentialId: String
    ): Call<Void>


    @POST("vehicles/doorLockStatus/close")
    fun updateDoorLockClose(
        @Query("vehicleId") vehicleId: String,
        @Query("credentialId") credentialId: String
    ): Call<Void>



    @POST("vehicles/ignition/start")
    fun updateIgnitionStart(
        @Query("vehicleId") vehicleId: String,
        @Query("credentialId") credentialId: String
    ): Call<Void>


    @POST("vehicles/ignition/stop")
    fun updateIgnitionStop(
        @Query("vehicleId") vehicleId: String,
        @Query("credentialId") credentialId: String
    ): Call<Void>

    @POST("vehicles/fuelLevel")
    fun updateFuelLevel(
        @Query("vehicleId") vehicleId: String,
        @Query("credentialId") credentialId: String,
        @Body fuelLevel: Float
    ): Call<Void>
}
