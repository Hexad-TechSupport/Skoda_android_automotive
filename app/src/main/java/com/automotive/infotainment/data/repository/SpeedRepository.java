package com.automotive.infotainment.data.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.automotive.infotainment.data.api.RetrofitInstance;
import com.automotive.infotainment.data.api.UserApi;
import com.automotive.infotainment.data.model.VehicleData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SpeedRepository {

    public static String TAG = SpeedRepository.class.getName();

    private static SpeedRepository repository;
    private final UserApi userApi;

    // LiveData to store vehicle speed and odometer values
    private final MutableLiveData<Float> speedInMiles = new MutableLiveData<>();
    private final MutableLiveData<Float> odometer = new MutableLiveData<>();

    // LiveData for additional vehicle properties
    private final MutableLiveData<Boolean> doorLockStatus = new MutableLiveData<>();
    private final MutableLiveData<Integer> doorStatus = new MutableLiveData<>();
    private final MutableLiveData<Integer> ignitionState = new MutableLiveData<>();
    private final MutableLiveData<Float> fuelLevel = new MutableLiveData<>();

    public static SpeedRepository getInstance() {
        synchronized (SpeedRepository.class) {
            if (repository == null) {
                repository = new SpeedRepository();
            }
            return repository;
        }
    }

    private SpeedRepository() {
        userApi = RetrofitInstance.INSTANCE.getApi();
    }

    // Method to fetch vehicle data from the API
    public void fetchVehicleData(String vehicleId, String credentialId) {
        userApi.getVehicleData(vehicleId, credentialId).enqueue(new Callback<VehicleData>() {
            @Override
            public void onResponse(Call<VehicleData> call, Response<VehicleData> response) {
                if (response.isSuccessful() && response.body() != null) {
                    VehicleData vehicleData = response.body();
                    setSpeedInMiles(vehicleData.getSpeed()); // Assuming speed is in km/h
                    setOdometer(vehicleData.getOdometer());
                    setDoorStatus(vehicleData.getDoorPosition());
                    setIgnitionState(vehicleData.getIgnitionState());
                    setFuelLevel(vehicleData.getFuelLevel());
                } else {
                    Log.e(TAG, "Failed to fetch vehicle data: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<VehicleData> call, Throwable t) {
                Log.e(TAG, "API call failed: " + t.getMessage());
            }
        });
    }

    // Getter for speed in miles
    public LiveData<Float> getSpeedInMiles() {
        Log.i(TAG, "getSpeedInMiles");
        return this.speedInMiles;
    }

    // Setter for speed in miles
    public void setSpeedInMiles(Float val) {
        Log.i(TAG, "setSpeedInMiles: " + val);
        this.speedInMiles.setValue(val);
    }

    // Getter for odometer value
    public LiveData<Float> getOdometer() {
        Log.i(TAG, "getOdometer");
        return this.odometer;
    }

    // Setter for odometer value
    public void setOdometer(Float val) {
        Log.i(TAG, "setOdometer: " + val);
        this.odometer.setValue(val);
    }

    // Getter for door lock status
    public LiveData<Boolean> getDoorLockStatus() {
        Log.i(TAG, "getDoorLockStatus");
        return this.doorLockStatus;
    }

    // Setter for door lock status
    public void setDoorLockStatus(Boolean val) {
        Log.i(TAG, "setDoorLockStatus: " + val);
        this.doorLockStatus.setValue(val);
    }

    // Getter for door status
    public LiveData<Integer> getDoorStatus() {
        Log.i(TAG, "getDoorStatus");
        return this.doorStatus;
    }

    // Setter for door status
    public void setDoorStatus(Integer val) {
        Log.i(TAG, "setDoorStatus: " + val);
        this.doorStatus.setValue(val);
    }

    // Getter for ignition state
    public LiveData<Integer> getIgnitionState() {
        Log.i(TAG, "getIgnitionState");
        return this.ignitionState;
    }

    // Setter for ignition state
    public void setIgnitionState(Integer val) {
        Log.i(TAG, "setIgnitionState: " + val);
        this.ignitionState.setValue(val);
    }

    // Getter for fuel level
    public LiveData<Float> getFuelLevel() {
        Log.i(TAG, "getFuelLevel");
        return this.fuelLevel;
    }

    // Setter for fuel level
    public void setFuelLevel(Float val) {
        Log.i(TAG, "setFuelLevel: " + val);
        this.fuelLevel.setValue(val);
    }
}
