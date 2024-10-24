package com.automotive.infotainment.core;

import android.car.Car;
import android.content.Context;
import android.util.Log;

import com.automotive.infotainment.handler.SpeedPropertyHandler;
import com.automotive.infotainment.interfaces.IBasePropertyHandler;

public class CarPropertyManager {

    private static final String TAG = CarPropertyManager.class.getName();
    private android.car.hardware.property.CarPropertyManager mCarPropertyManager;
    private IBasePropertyHandler speedPropertyHandler;
    public static Context context;
    public static android.car.hardware.property.CarPropertyManager mCarPropertyManagerinstance;

    public CarPropertyManager(Context ctx) {
        context = ctx;
        connectToCar(ctx);
    }

    private void connectToCar(Context context) {
        Log.i(TAG, "ConnectToCar()");
        Car mCarApiClient = Car.createCar(context);
        boolean isCarConnected = mCarApiClient.isConnected();
        Log.i(TAG, String.valueOf(isCarConnected));
        if (isCarConnected) {
            handleCarLifeCycleEvent(mCarApiClient, isCarConnected);
        }
    }

    /*
     * Method for generating, registering
     * and getting default property from VHAL
     * */
    private void handleCarLifeCycleEvent(Car mCarApiClient, boolean isCarConnected) {
        Log.i(TAG, String.valueOf(isCarConnected));
        if (mCarApiClient != null) {
            Log.i(TAG, "Car is now ready so register all properties and get all default values.");
            mCarPropertyManager = (android.car.hardware.property.CarPropertyManager) mCarApiClient.getCarManager(Car.PROPERTY_SERVICE);
            generatePropertyHandler();
            registerAllPropertyId();
            getDefaultValuesFromVHAL();
        }
    }

    /*
     * Method for getting Default property
     * */
    private void getDefaultValuesFromVHAL() {
        if (mCarPropertyManager != null) {
            speedPropertyHandler.getDefaultProperties();
        }
    }
    /*
     * Method for registering all propertyid
     * */
    private void registerAllPropertyId() {
        if (mCarPropertyManager != null) {
            speedPropertyHandler.registerProperties();
        }
    }
    /*
     * Initialization all property handler class here
     * */
    private void generatePropertyHandler() {
        if (mCarPropertyManager != null) {
            Log.i(TAG, "registerAllPropertyId()");
            speedPropertyHandler = new SpeedPropertyHandler(mCarPropertyManager);
        }
    }

    /*
     * Method for un registering all property id
     * */
    public void unRegisterAllpropertyId() {
        if (mCarPropertyManager != null) {
            speedPropertyHandler.unRegisterProperties();
        }
    }

}
