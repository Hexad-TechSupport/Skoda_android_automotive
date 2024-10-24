package com.automotive.infotainment.vm;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.automotive.infotainment.repository.SpeedRepository;

public class SpeedViewModel extends ViewModel {
    private SpeedRepository mSpeedRepository = SpeedRepository.getInstance();

    public LiveData<Float> ObserveVehicleSpeed() {
        return mSpeedRepository.getSpeedInMiles();
    }
}

