package com.eazico.autoreset;

import android.Manifest;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.widget.TextView;
import android.widget.Toast;

import static android.media.AudioManager.RINGER_MODE_NORMAL;
import static android.media.AudioManager.RINGER_MODE_SILENT;
import static android.media.AudioManager.RINGER_MODE_VIBRATE;
import static java.lang.Math.acos;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class GPS implements LocationListener {
    private TextView gpv;
    public LocationManager locm;
    //Location loc;
   public  double lat;public double lon;
    Context contm;
    Audio aud;
    int[] s = {RINGER_MODE_NORMAL, RINGER_MODE_SILENT, RINGER_MODE_VIBRATE};
    NotificationManager notificationManager;
    NotificationCompat.Builder mBuilder;
    //RINGER_MODE_NORMAL
    //RINGER_MODE_SILENT
    //	RINGER_MODE_VIBRATE

    GPS(Context contm) {
        // gpv = t;
        this.contm = contm;
        aud = new Audio(contm);
        lat=-1;lon=-1;

        locm = (LocationManager) contm
                .getSystemService(Context.LOCATION_SERVICE);
        boolean statusOfGPS = locm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if(statusOfGPS) {
            if (ActivityCompat.checkSelfPermission(contm, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(contm, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                // return TODO;
            }
            Location last = locm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            locm.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    1,
                    1, this);
            if (last != null) {
                lon = last.getLongitude();
                lat = last.getLatitude();
            }
        }
        else
            ;//create notification

    }

    public void update(Context c) {
        if (ActivityCompat.checkSelfPermission(c, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(c, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locm.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                1,
                1, this);

    }

    @Override

    public void onLocationChanged(Location location) {
         lat=location.getLatitude();//current location
         lon= location.getLongitude();

       // gpv.setText("latt:" + Double.toString(lat) + "long:" +Double.toString(lon));
       /*///////// double lat2=11.5937578;double lon2=76.0727309;//reference
        double dr = acos(sin(lat)*sin(lat2)+cos(lat)*cos(lat2)*cos(lon-lon2));
        double dkm=6371*dr;
        double dm=dkm*1000;
        if(lat.intValue()==11 && lon.intValue()==76){
            //set vol
            aud.setvol(s[1]);

            Toast.makeText(contm,"set vol",Toast.LENGTH_SHORT).show();
        }
        else Toast.makeText(contm,"not displayed",Toast.LENGTH_SHORT).show();
        if (ActivityCompat.checkSelfPermission(contm, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(contm, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locm.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                1,
                1, this);
      //  gpv.setText();*////////
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
//notificationManager.cancel(1);
    }

    @Override
    public void onProviderDisabled(String provider) {
//gpv.setText("location not enabled");
        //create_notification();

    }
    public void create_notification(){
        mBuilder =
                new NotificationCompat.Builder(contm)
                        .setSmallIcon(R.drawable.ic_stat_name)
                        .setContentTitle("Auto Reset")
                        .setContentText("Please turn ON GPS for full functioning!")
                        .setOngoing(true)
        ;

        mBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        notificationManager = (NotificationManager) contm.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, mBuilder.build());
    }
}
