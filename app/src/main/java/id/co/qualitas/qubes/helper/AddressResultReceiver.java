package id.co.qualitas.qubes.helper;

import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.interfaces.LocationRequestCallback;

public class AddressResultReceiver extends ResultReceiver {
    private LocationRequestCallback<String, Location> callbackOnResult;
    private String mAddressOutput;
    private Location mLastLocation;

    public AddressResultReceiver(Handler handler) {
        super(handler);
    }

    public void setCallback(LocationRequestCallback<String, Location> callbackOnResult) {
        this.callbackOnResult = callbackOnResult;
    }

    public void setLastLocation(Location mLastLocation) {
        this.mLastLocation = mLastLocation;
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        mAddressOutput = resultData.getString(Constants.RESULT_DATA_KEY);
        // Show a toast message if an address was found.
//        if (resultCode == Constants.SUCCESS_RESULT) {
//                showToast(mAddressOutput);
//        }
        if (this.callbackOnResult != null) {
            this.callbackOnResult.onFinish(mAddressOutput, mLastLocation);
        }
    }
}