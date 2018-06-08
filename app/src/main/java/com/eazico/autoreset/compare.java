package com.eazico.autoreset;

import android.util.Log;

import static java.lang.Math.toRadians;
import static java.lang.StrictMath.abs;

public class compare {
    public static boolean ready(double lat1,double lon1,double lat2,double lon2){
        int R = 6371; // Km
        double φ1 = Math.toRadians(lat1);
        double φ2 = Math.toRadians(lat2);
        double Δφ = Math.toRadians(lat2-lat1);
        double Δλ = Math.toRadians(lon2-lon1);

        double a = Math.sin(Δφ/2) * Math.sin(Δφ/2) +
                Math.cos(φ1) * Math.cos(φ2) *
                        Math.sin(Δλ/2) * Math.sin(Δλ/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        double d = Math.round(R * c);
        Log.i("dist in m",""+d*1000);
        if(d/1000<=50){
            Log.i("compare","ok");
            return true;}
        else { Log.i("compare"," not ok");
            return false;
        }

    }
}
