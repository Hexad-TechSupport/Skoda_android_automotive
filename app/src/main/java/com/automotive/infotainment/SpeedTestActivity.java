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

    private static final int FUEL_THRESHOLD = 10;

    private SpeedViewModel speedViewModel;
    private DatabaseReference firebaseRef;

    private TextView speedDisplay;
    private ImageView alertBanner, doorIcon, powerIcon, fuelIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_speed_test);

        // Initialize Firebase and ViewModel
        firebaseRef = FirebaseDatabase.getInstance().getReference("users");
        speedViewModel = new ViewModelProvider(this).get(SpeedViewModel.class);

        // Bind UI components
        speedDisplay = findViewById(R.id.speed_number);
        alertBanner = findViewById(R.id.alert_banner);
        doorIcon = findViewById(R.id.door);
        powerIcon = findViewById(R.id.power);
        fuelIcon = findViewById(R.id.fuel);

        // Start observing data
        observeSpeedData();
        observeAdditionalVehicleData();
    }

    private void observeSpeedData() {
        speedViewModel.ObserveVehicleSpeed().observe(this, speed -> {
            updateSpeedDisplay(speed);
            if (speed > getOEMVehicleSpeed()) {
                showAlert();
            } else {
                hideAlert();
            }
        });
    }

    private float getOEMVehicleSpeed() {
        final float[] oemSpeed = {0.0f};
        firebaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    oemSpeed[0] = userSnapshot.child("speed").getValue(Float.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.err.println(error.toException());
            }
        });
        return oemSpeed[0];
    }

    private void observeAdditionalVehicleData() {
        firebaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    updateDoorStatus(userSnapshot.child("doorPosition").getValue(Integer.class));
                    updateIgnitionState(userSnapshot.child("ignitionState").getValue(Integer.class));
                    updateFuelLevel(userSnapshot.child("fuelLevel").getValue(Float.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.err.println(error.toException());
            }
        });
    }

    private void updateSpeedDisplay(float speed) {
        speedDisplay.setText(String.format("%d km/h", (int) speed));
    }

    private void showAlert() {
        alertBanner.setVisibility(View.VISIBLE);
    }

    private void hideAlert() {
        alertBanner.setVisibility(View.GONE);
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
