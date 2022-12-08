package com.example.projectand.utils;

import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class AddressGetter extends AsyncTask<String, Void, String> {
    private final OnAddressGetterFinished listener;
    private final Geocoder geocoder;
    private final Integer code;
    private Address address;

    public AddressGetter(Integer code, Geocoder geocoder, OnAddressGetterFinished listener){
        this.code = code;
        this.listener=listener;
        this.geocoder = geocoder;
    }

    public interface OnAddressGetterFinished {
        public void onAddressGetterFinished(Integer code, String result, Address address);
    }

    // required methods

    @Override
    protected String doInBackground(String... strings) {

        try {
            if (strings.length == 1) {
                String location = strings[0];
                List<Address> addressList = this.geocoder.getFromLocationName(location, 1);
                if (!addressList.isEmpty()) {
                    address = addressList.get(0);
                    return location;
                } else {
                    return "This location does not exist!";
                }
            } else {
                Double latitude = Double.parseDouble(strings[0]);
                Double longitute = Double.parseDouble(strings[1]);
                List<Address> addressList = this.geocoder.getFromLocation(latitude, longitute, 1);
                if (!isCancelled()) {
                    if (!addressList.isEmpty()) {
                        address = addressList.get(0);
                        return "location";
                    } else {
                        return "This location does not exist!";
                    }
                } else {
                    Log.e("task", "canceled!!");
                    return "canceled";
                }
            }
        } catch (IOException e) {
            return "Couldn't check address validity! Please check your internet connection.";
        }
    }

    @Override
    protected void onPostExecute(String result) {
        if (!Objects.equals(result, "canceled"))
            listener.onAddressGetterFinished(code, result, address);
    }
}