package com.automotive.infotainment.vm;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.automotive.infotainment.data.repository.SpeedRepository;

public class SpeedViewModel extends ViewModel {
    private final SpeedRepository mSpeedRepository;

    public SpeedViewModel() {
        mSpeedRepository = SpeedRepository.getInstance();
        mSpeedRepository.fetchVehicleData();  // Fetch vehicle data when the ViewModel is created
    }

    public LiveData<Float> ObserveVehicleSpeed() {
        return mSpeedRepository.getSpeedInMiles();
    }

    public LiveData<Float> getOdometer() {
        return mSpeedRepository.getOdometer();
    }

    public LiveData<Integer> getDoorStatus() {
        return mSpeedRepository.getDoorStatus();
    }

    public LiveData<Integer> getIgnitionState() {
        return mSpeedRepository.getIgnitionState();
    }

    public LiveData<Float> getFuelLevel() {
        return mSpeedRepository.getFuelLevel();
    }
}
