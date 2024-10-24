package com.automotive.infotainment;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.automotive.infotainment.vm.SpeedViewModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SpeedTestActivity extends AppCompatActivity {

    public SpeedViewModel mSpeedmodel;
    public FirebaseDatabase mFirebaseDB;
    public DatabaseReference myRef;

    private TextView speedDisplay;
    private ImageView alertBanner;

    // ImageViews for dynamic icons
    private ImageView doorIcon;
    private ImageView powerIcon;
    private ImageView fuelIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_speed_test);

        // Initialize Firebase
        mFirebaseDB = FirebaseDatabase.getInstance();
        myRef = mFirebaseDB.getReference("users");

        // Initialize ViewModel
        mSpeedmodel = new ViewModelProvider(this).get(SpeedViewModel.class);

        // Find UI elements
        speedDisplay = findViewById(R.id.speed_number);
        alertBanner = findViewById(R.id.alert_banner);

        // Find ImageViews for icons
        doorIcon = findViewById(R.id.door);
        powerIcon = findViewById(R.id.power);
        fuelIcon = findViewById(R.id.fuel);

        // Observe speed data from ViewModel
        ObserveSpeedData();
    }

    public void ObserveSpeedData() {
        if (mSpeedmodel.ObserveVehicleSpeed() != null) {
            mSpeedmodel.ObserveVehicleSpeed().observe(this, speed -> {
                // Update the UI with the current speed
                updateSpeedDisplay(speed);

                // Check if the speed exceeds the limit
                if (speed > getOEMVehicleSpeed()) {
                    // Show alert if the speed exceeds the limit
                    showAlert();
                } else {
                    // Hide the alert if the speed is under the limit
                    hideAlert();
                }
            });
        }
        observeAdditionalVehicleData(); // New method to observe additional data
    }

    public float getOEMVehicleSpeed() {
        final float[] speed = {0.0f};
        // Retrieve speed data for all users from Firebase
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String userId = userSnapshot.getKey();
                    speed[0] = userSnapshot.child("speed").getValue(Float.class);

                    if (speed[0] != 0.0f) {
                        System.out.println("User: " + userId + ", Speed: " + speed[0]);
                    } else {
                        System.out.println("Speed data not available for user: " + userId);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.err.println(error.toException());
            }
        });
        return speed[0];
    }

    private void observeAdditionalVehicleData() {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String userId = userSnapshot.getKey();

                    // Update door lock status
//                    boolean isLocked = userSnapshot.child("doorLock").getValue(Boolean.class);
//                    updateDoorLockStatus(isLocked);

                    // Update door status
                    int doorPosition = userSnapshot.child("doorPosition").getValue(Integer.class);
                    updateDoorStatus(doorPosition);

                    // Update ignition state
                    int ignitionStateValue = userSnapshot.child("ignitionState").getValue(Integer.class);
                    updateIgnitionState(ignitionStateValue);

                    // Update fuel level
                    float fuelLevelValue = userSnapshot.child("fuelLevel").getValue(Float.class);
                    updateFuelLevel(fuelLevelValue);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.err.println(error.toException());
            }
        });
    }

    private void updateSpeedDisplay(float speed) {
        // Update the speed display TextView with the current speed
        speedDisplay.setText((int) speed);
    }

    private void showAlert() {
        // Show the alert banner when the speed exceeds the limit
        alertBanner.setVisibility(View.VISIBLE);
    }

    private void hideAlert() {
        // Hide the alert banner when the speed is within the limit
        alertBanner.setVisibility(View.GONE);
    }


    private void updateDoorStatus(int doorPosition) {
        String status = (doorPosition == 1) ? "Open" : "Closed";
//        doorStatus.setText("Door Status: " + status);

        // Update door icon based on door position
        int doorIconRes = (doorPosition == 1) ? R.drawable.door_open : R.drawable.door_closed;
        doorIcon.setImageResource(doorIconRes);
    }

    private void updateIgnitionState(int ignitionStateValue) {
        String state = (ignitionStateValue == 1) ? "On" : "Off";
//        ignitionState.setText("Ignition: " + state);

        // Update ignition icon based on ignition state
        int ignitionIconRes = (ignitionStateValue == 1) ? R.drawable.power_on : R.drawable.power_off;
        powerIcon.setImageResource(ignitionIconRes);
    }

    private void updateFuelLevel(float fuelLevelValue) {
//        fuelLevel.setText("Fuel Level: " + fuelLevelValue + "%");

        // Update fuel icon based on fuel level thresholds
        int fuelIconRes;
         if (fuelLevelValue >= 10) {
            fuelIconRes = R.drawable.fuel_ok;
        } else  {
            fuelIconRes = R.drawable.fuel_low;
        }
        fuelIcon.setImageResource(fuelIconRes);
    }
}
