package com.ios.widget.provider;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import com.google.android.gms.location.LocationResult;
import com.ios.widget.crop.utils.MyAppPref;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LocationBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (LocationResult.hasResult(intent)) {
            LocationResult locationResult = LocationResult.extractResult(intent);
            Location location = locationResult.getLastLocation();

            try {
                Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                String city = addresses.get(0).getLocality();
                new MyAppPref(context).getString(MyAppPref.IS_WEATHER_CITY, city);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
