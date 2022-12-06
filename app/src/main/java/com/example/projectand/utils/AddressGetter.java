package com.example.projectand.utils;

import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;

import java.io.IOException;
import java.util.List;

public class AddressGetter extends AsyncTask<String, Void, String> {
    private final OnAddressGetterFinished listener;
    private final Geocoder geocoder;
    private Address address;

    public AddressGetter(Geocoder geocoder, OnAddressGetterFinished listener){
        this.listener=listener;
        this.geocoder = geocoder;
    }

    public interface OnAddressGetterFinished {
        public void onAddressGetterFinished(String result, Address address);
    }

    // required methods

    @Override
    protected String doInBackground(String... strings) {
        String location = strings[0];
        try {
            List<Address> addressList = this.geocoder.getFromLocationName(location, 1);
            if (!addressList.isEmpty()) {
                address = addressList.get(0);
                return location;
            } else {
                return "This location does not exist!";
            }
        } catch (IOException e) {
            return "Couldn't check address validity! Please check your internet connection.";
        }
    }

    @Override
    protected void onPostExecute(String result) {
        listener.onAddressGetterFinished(result, address);
    }
}