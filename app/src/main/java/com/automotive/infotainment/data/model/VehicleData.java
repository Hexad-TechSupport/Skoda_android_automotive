package com.automotive.infotainment.data.model;

public class VehicleData {
    private float speed;
    private float odometer;
    private int doorPosition;
    private int ignitionState;
    private float fuelLevel;

    // Getters
    public float getSpeed() {
        return speed;
    }

    public float getOdometer() {
        return odometer;
    }

    public int getDoorPosition() {
        return doorPosition;
    }

    public int getIgnitionState() {
        return ignitionState;
    }

    public float getFuelLevel() {
        return fuelLevel;
    }

    // Setters (if necessary)
    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public void setOdometer(float odometer) {
        this.odometer = odometer;
    }

    public void setDoorPosition(int doorPosition) {
        this.doorPosition = doorPosition;
    }

    public void setIgnitionState(int ignitionState) {
        this.ignitionState = ignitionState;
    }

    public void setFuelLevel(float fuelLevel) {
        this.fuelLevel = fuelLevel;
    }
}
