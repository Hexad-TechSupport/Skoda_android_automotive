package com.automotive.infotainment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.automotive.infotainment.vm.SpeedViewModel;

public class SpeedTestActivity extends AppCompatActivity {

    private static final int FUEL_THRESHOLD = 10;

    private SpeedViewModel speedViewModel;

    private TextView speedDisplay;
    private ImageView alertBanner, doorIcon, powerIcon, fuelIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_speed_test);

        // Initialize ViewModel
        speedViewModel = new ViewModelProvider(this).get(SpeedViewModel.class);

        // Bind UI components
        speedDisplay = findViewById(R.id.speed_number);
        alertBanner = findViewById(R.id.alert_banner);
        doorIcon = findViewById(R.id.door);
        powerIcon = findViewById(R.id.power);
        fuelIcon = findViewById(R.id.fuel);

        speedViewModel.ObserveVehicleSpeed().observe(this, speed -> {
            updateSpeedDisplay(speed);
            if (speed > getOEMVehicleSpeed()) {
                showAlert();
            } else {
                hideAlert();
            }
        });

        speedViewModel.getOdometer().observe(this, this::updateOdometer);
        speedViewModel.getDoorStatus().observe(this, this::updateDoorStatus);
        speedViewModel.getIgnitionState().observe(this, this::updateIgnitionState);
        speedViewModel.getFuelLevel().observe(this, this::updateFuelLevel);
    }

    private float getOEMVehicleSpeed() {
        return 0;
    }

    @SuppressLint("DefaultLocale")
    private void updateSpeedDisplay(float speed) {
        speedDisplay.setText(String.format("%d km/h", (int) speed));
    }

    private void showAlert() {
        alertBanner.setVisibility(View.VISIBLE);
    }

    private void hideAlert() {
        alertBanner.setVisibility(View.GONE);
    }

    private void updateOdometer(float odometer) {
        // Update odometer UI here if required
    }

    private void updateDoorStatus(int doorPosition) {
        int doorIconRes = (doorPosition == 1) ? R.drawable.door_open : R.drawable.door_closed;
        doorIcon.setImageResource(doorIconRes);
    }

    private void updateIgnitionState(int ignitionStateValue) {
        int ignitionIconRes = (ignitionStateValue == 1) ? R.drawable.power_on : R.drawable.power_off;
        powerIcon.setImageResource(ignitionIconRes);
    }

    private void updateFuelLevel(float fuelLevelValue) {
        int fuelIconRes = (fuelLevelValue >= FUEL_THRESHOLD) ? R.drawable.fuel_ok : R.drawable.fuel_low;
        fuelIcon.setImageResource(fuelIconRes);
    }
}
