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

import com.automotive.infotainment.utility.VhalConstant;
import com.automotive.infotainment.vm.SpeedViewModel;


public class SpeedTestActivity extends AppCompatActivity {

    private static final int FUEL_THRESHOLD = 10;

    private int SPEED_THRESHOLD = (int) VhalConstant.OEM_VEHICLE_SPEED_LIMIT;

    private SpeedViewModel speedViewModel;

    private TextView speedDisplay;
    private ImageView alertBanner, doorIcon, powerIcon, fuelIcon;

    // Variable to track alert visibility
    private boolean isAlertVisible = false;

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
        SPEED_THRESHOLD = getOEMVehicleSpeed();

        speedViewModel.ObserveVehicleSpeed().observe(this, speed -> {
            updateSpeedDisplay(speed);

            // Check if speed exceeds the limit and update alert state accordingly
            if (speed > SPEED_THRESHOLD) {
                if (!isAlertVisible) {  // Only show the alert if it's not already visible
                    showAlert();
                    isAlertVisible = true;  // Update state to indicate alert is shown
                }
            } else {
                if (isAlertVisible) {  // Only hide the alert if it's currently visible
                    hideAlert();
                    isAlertVisible = false;  // Update state to indicate alert is hidden
                }
            }
        });


        speedViewModel.getDoorStatus().observe(this, this::updateDoorStatus);
        speedViewModel.getIgnitionState().observe(this, this::updateIgnitionState);
        speedViewModel.getFuelLevel().observe(this, this::updateFuelLevel);
    }

    private int getOEMVehicleSpeed() {
        // api to get the OEM vehicle speed
        return 0;
    }

    @SuppressLint("DefaultLocale")
    private void updateSpeedDisplay(float speed) {
        speedDisplay.setText(String.format("%d", (int) speed));
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
        if (ignitionStateValue == 1) {
          float totalDistance = speedViewModel.getOdometer().getValue() ;
          updateOdometer(totalDistance);
        } else {
            float totalDistance = speedViewModel.getOdometer().getValue() ;
            updateOdometer(totalDistance);
        }
    }

    private void updateFuelLevel(float fuelLevelValue) {
        int fuelIconRes = (fuelLevelValue >= FUEL_THRESHOLD) ? R.drawable.fuel_ok : R.drawable.fuel_low;
        fuelIcon.setImageResource(fuelIconRes);
    }
}
