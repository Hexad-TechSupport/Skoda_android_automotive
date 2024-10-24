package com.automotive.infotainment.handler;

import android.car.VehicleAreaType;
import android.car.hardware.property.CarPropertyManager;
import android.util.Log;

import com.automotive.infotainment.interfaces.IBasePropertyHandler;
import com.automotive.infotainment.repository.SpeedRepository;
import com.automotive.infotainment.utility.VhalConstant;

import java.util.ArrayList;

public abstract class BasePropertyHandler implements CarPropertyManager.CarPropertyEventCallback, IBasePropertyHandler {

    private static final String TAG = BasePropertyHandler.class.getName();

    // List of properties to register and listen for
    protected ArrayList<Integer> propertyList = new ArrayList<>();

    // Car Property Manager instance to handle car properties
    public CarPropertyManager mCarPropertyManager;
    public static CarPropertyManager mCarUnitManager;

    // Repository for managing speed-related data
    public SpeedRepository mSpeedRepository;

    /**
     * Constructor initializes the car property manager and speed repository.
     */
    public BasePropertyHandler(CarPropertyManager carPropertyManager) {
        this.mCarPropertyManager = carPropertyManager;
        generatePropertyList();
        this.mSpeedRepository = SpeedRepository.getInstance();
    }

    /**
     * @return CarPropertyManager instance
     */
    public static CarPropertyManager getCarPropertyManagerInstance() {
        return mCarUnitManager;
    }

    /**
     * Abstract method to generate the list of properties to observe.
     */
    abstract void generatePropertyList();

    /**
     * Registers the car properties to listen for changes.
     */
    @Override
    public void registerProperties() {
        Log.i(TAG, "Registering properties...");
        if (propertyList.isEmpty()) {
            Log.i(TAG, "No properties to register.");
            return;
        }
        propertyList.forEach(prop -> {
            mCarPropertyManager.registerCallback(this, prop, VhalConstant.PROP_RATE);
        });
    }

    /**
     * Unregisters the car properties to stop listening for changes.
     */
    @Override
    public void unRegisterProperties() {
        Log.i(TAG, "Unregistering properties...");
        if (propertyList.isEmpty()) {
            Log.i(TAG, "No properties to unregister.");
            return;
        }
        propertyList.forEach(prop -> {
            mCarPropertyManager.unregisterCallback(this, prop);
        });
    }

    /**
     * Retrieves a boolean property from the car's system.
     * @param propId The property ID to retrieve
     * @return The boolean value of the property
     */
    public boolean getBooleanProperty(int propId) {
        if (mCarPropertyManager != null) {
            return mCarPropertyManager.getBooleanProperty(propId, VehicleAreaType.VEHICLE_AREA_TYPE_GLOBAL);
        } else {
            return false;
        }
    }

    /**
     * Retrieves an integer array property from the car's system.
     * @param propId The property ID to retrieve
     * @return An Integer array containing the property values
     */
    private Integer[] getIntArrayProperty(int propId) {
        if (mCarPropertyManager != null) {
            int[] array = mCarPropertyManager.getIntArrayProperty(propId, VehicleAreaType.VEHICLE_AREA_TYPE_GLOBAL);
            Integer[] wrappedArray = new Integer[array.length];
            for (int index = 0; index < array.length; index++) {
                wrappedArray[index] = array[index];
            }
            return wrappedArray;
        } else {
            return null;
        }
    }

    /**
     * Retrieves a float property from the car's system.
     * @param propId The property ID to retrieve
     * @return The float value of the property
     */
    protected float getFloatProperty(int propId) {
        if (mCarPropertyManager != null) {
            return mCarPropertyManager.getFloatProperty(propId, VehicleAreaType.VEHICLE_AREA_TYPE_GLOBAL);
        } else {
            return 0f;
        }
    }

    /**
     * Retrieves an integer property from the car's system.
     * @param propId The property ID to retrieve
     * @return The integer value of the property
     */
    protected int getIntProperty(int propId) {
        if (mCarPropertyManager != null) {
            return mCarPropertyManager.getIntProperty(propId, VehicleAreaType.VEHICLE_AREA_TYPE_GLOBAL);
        } else {
            return 0;
        }
    }

    /**
     * Sets an integer property in the car's system.
     * @param propId The property ID to set
     * @param val The value to set
     */
    protected void setIntProperty(int propId, int val) {
        if (mCarPropertyManager != null) {
            mCarPropertyManager.setIntProperty(propId, VehicleAreaType.VEHICLE_AREA_TYPE_GLOBAL, val);
        }
    }

    /**
     * Sets a boolean property in the car's system.
     * @param propId The property ID to set
     * @param val The value to set
     */
    protected void setBooleanProperty(int propId, boolean val) {
        if (mCarPropertyManager != null) {
            mCarPropertyManager.setBooleanProperty(propId, VehicleAreaType.VEHICLE_AREA_TYPE_GLOBAL, val);
        }
    }

    /**
     * Sets a float property in the car's system.
     * @param propId The property ID to set
     * @param val The value to set
     */
    protected void setFloatProperty(int propId, float val) {
        if (mCarPropertyManager != null) {
            mCarPropertyManager.setFloatProperty(propId, VehicleAreaType.VEHICLE_AREA_TYPE_GLOBAL, val);
        }
    }
}
