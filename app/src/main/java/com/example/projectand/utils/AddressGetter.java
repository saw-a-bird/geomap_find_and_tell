package com.example.projectand.utils;

import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;

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
        void onAddressGetterFinished(Integer code, String result, Address address);
    }

    // required methods

    @Override
    protected String doInBackground(String... strings) {

        try {
            List<Address> addressList;
            if (strings.length == 1) {
                String location = strings[0];
                addressList = this.geocoder.getFromLocationName(location, 1);
            } else {
                double latitude = Double.parseDouble(strings[0]);
                double longitude = Double.parseDouble(strings[1]);
                addressList = this.geocoder.getFromLocation(latitude, longitude, 1);
            }

            if (!addressList.isEmpty()) {
                address = addressList.get(0);
                return "Address list is not empty but the first address is null! (who knows why)"; // if address becomes null (for some weird reason), this error will show, but normally, it works successfully.
            } else {
                return "This location does not exist! (Somewhere unknown like the sea)";
            }
        } catch (IOException e) {
            return "Couldn't process request! Please check your internet connection.";
        }
    }

    @Override
    protected void onPostExecute(String result) {
        if (!Objects.equals(result, "canceled"))
            listener.onAddressGetterFinished(code, result, address);
    }
}