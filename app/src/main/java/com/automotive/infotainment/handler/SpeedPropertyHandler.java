package com.automotive.infotainment.handler;

import android.car.VehiclePropertyIds;
import android.car.hardware.CarPropertyValue;
import android.util.Log;
import android.car.hardware.property.CarPropertyManager;

public class SpeedPropertyHandler extends BasePropertyHandler {

    private static final String TAG = SpeedPropertyHandler.class.getName();
    float vehicleSpeedMiles;
    int gearSelection;
    int previousGear = -1;

    public SpeedPropertyHandler(CarPropertyManager carPropertyManager) {
        super(carPropertyManager);
        Log.i(TAG,"Constructor initialized");
    }

    @Override
    void generatePropertyList() {
        // Add all vehicle property IDs you need to observe
        propertyList.add(VehiclePropertyIds.PERF_VEHICLE_SPEED);    // Vehicle speed
        propertyList.add(VehiclePropertyIds.DOOR_LOCK);             // Door lock status
        propertyList.add(VehiclePropertyIds.DOOR_POS);              // Door open/closed status
        propertyList.add(VehiclePropertyIds.IGNITION_STATE);        // Ignition status
        propertyList.add(VehiclePropertyIds.PERF_ODOMETER);         // Odometer (total distance traveled)
        propertyList.add(VehiclePropertyIds.FUEL_LEVEL);            // Fuel level (for mileage calculations)
        Log.i(TAG,"Properties generated.");
    }

    @Override
    public void onChangeEvent(CarPropertyValue carPropertyValue) {
        Log.i(TAG, "onChangeEvent() id " + carPropertyValue.getPropertyId() + " , value " + carPropertyValue.getValue());
        int propertyId = carPropertyValue.getPropertyId();

        switch (propertyId) {
            case VehiclePropertyIds.PERF_VEHICLE_SPEED:
                vehicleSpeedMiles = getFloatProperty(VehiclePropertyIds.PERF_VEHICLE_SPEED);
                mSpeedRepository.setSpeedInMiles(vehicleSpeedMiles);
                break;

            case VehiclePropertyIds.DOOR_LOCK:
                boolean isLocked = (boolean) carPropertyValue.getValue();
                Log.i(TAG, "Door lock status: " + (isLocked ? "Locked" : "Unlocked"));
                handleDoorLockStatus(isLocked);
                break;

            case VehiclePropertyIds.DOOR_POS:
                int doorPosition = (int) carPropertyValue.getValue();
                Log.i(TAG, "Door status (open/closed): " + doorPosition);
                handleDoorStatus(doorPosition);
                break;

            case VehiclePropertyIds.IGNITION_STATE:
                int ignitionState = (int) carPropertyValue.getValue();
                Log.i(TAG, "Ignition state: " + (ignitionState == 1 ? "Started" : "Not Started"));
                handleIgnitionState(ignitionState);
                break;

            case VehiclePropertyIds.PERF_ODOMETER:
                float totalKilometers = getFloatProperty(VehiclePropertyIds.PERF_ODOMETER);
                Log.i(TAG, "Total kilometers traveled: " + totalKilometers);
                mSpeedRepository.setOdometer(totalKilometers);
                break;


            case VehiclePropertyIds.FUEL_LEVEL:
                float fuelLevel = getFloatProperty(VehiclePropertyIds.FUEL_LEVEL);
                Log.i(TAG, "Fuel level: " + fuelLevel);
                handleFuelLevel(fuelLevel);
                break;

            // Add more logic for other vehicle properties if needed
        }
    }

    @Override
    public void onErrorEvent(int i, int i1) {
        Log.i(TAG, "onErrorEvent: " + i + ", i1: " + i1);
    }

    @Override
    public void getDefaultProperties() {
        // Get the default properties if needed on initialization
        float defaultSpeed = getFloatProperty(VehiclePropertyIds.PERF_VEHICLE_SPEED);
        float defaultOdometer = getFloatProperty(VehiclePropertyIds.PERF_ODOMETER);
        Log.i(TAG, "Default Speed: " + defaultSpeed + ", Odometer: " + defaultOdometer);
    }
    private void handleDoorLockStatus(boolean isLocked) {
        // Update door lock status in the repository
        mSpeedRepository.setDoorLockStatus(isLocked);
        Log.i(TAG, "Door lock status updated: " + (isLocked ? "Locked" : "Unlocked"));
    }

    private void handleDoorStatus(int doorPosition) {
        // Convert door position to status string
        String status = (doorPosition == 1) ? "Open" : "Closed";
        mSpeedRepository.setDoorStatus(doorPosition);
        Log.i(TAG, "Door status updated: " + status);
    }

    private void handleIgnitionState(int ignitionState) {
        // Convert ignition state to boolean
        boolean isStarted = (ignitionState == 1);
        mSpeedRepository.setIgnitionState(ignitionState);
        Log.i(TAG, "Ignition state updated: " + (isStarted ? "Started" : "Not Started"));
    }

    private void handleFuelLevel(float fuelLevel) {
        // Update fuel level in the repository
        mSpeedRepository.setFuelLevel(fuelLevel);
        Log.i(TAG, "Fuel level updated: " + fuelLevel + "%");
    }


}
