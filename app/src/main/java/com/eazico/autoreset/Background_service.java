package com.eazico.autoreset;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

import static android.media.AudioManager.RINGER_MODE_NORMAL;
import static android.media.AudioManager.RINGER_MODE_SILENT;
import static android.media.AudioManager.RINGER_MODE_VIBRATE;

public class Background_service extends Service implements LocationListener  {
    public Handler handler = null;
    public static Runnable runnable = null;
    public LocationManager locm;
    //Location loc;
    public double lat;
    public double lon;
    Context contm;
    Audio aud;
    boolean statusOfGPS=false;
    int[] s = {RINGER_MODE_NORMAL, RINGER_MODE_SILENT, RINGER_MODE_VIBRATE};
    NotificationManager notificationManager;
    NotificationCompat.Builder mBuilder;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate()  {

        //Toast.makeText(this, " MyService Created ", Toast.LENGTH_LONG).show();
        Log.i("srvice creation","MyService Created");
        this.contm = getApplicationContext();
        aud = new Audio(contm);
        lat = -1;
        lon = -1;





        locm = (LocationManager) contm
                .getSystemService(Context.LOCATION_SERVICE);
         statusOfGPS = locm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (statusOfGPS) {
            if(notificationManager!=null)
                 notificationManager.cancel(1);
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
            if (last != null) {
                lon = last.getLongitude();
                lat = last.getLatitude();
            }

            locm.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    1,
                    1, this);
        } else {
            //turnGPSOn();
            create_notification();
          //  while(!statusOfGPS){
                statusOfGPS=locm.isProviderEnabled(LocationManager.GPS_PROVIDER);
           // }
        }
    }

    public Background_service(Context applicationContext) {
        super();
        Log.i("HERE", "here I am!");
    }

    public Background_service() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // super.onStartCommand(intent, flags, startId);
       // Toast.makeText(this, " MyService Started", Toast.LENGTH_LONG).show();
Log.i("on service start","MyService Started");
    if (statusOfGPS) {
        if(notificationManager!=null)
        notificationManager.cancel(1);
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
        if (last != null) {
            lon = last.getLongitude();
            lat = last.getLatitude();
        }

        locm.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                1,
                1, this);
    } else {
        //turnGPSOn();
       // notificationManager.notify(1, mBuilder.build());
        create_notification();
          while(!statusOfGPS){


        statusOfGPS = locm.isProviderEnabled(LocationManager.GPS_PROVIDER);

         }
        locm.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                1,
                1, this);
        if(notificationManager!=null)
         notificationManager.cancel(1);
    }


        return START_STICKY;
    }

    Intent mServiceIntent;
    private Background_service mSensorService;

    @Override
    public void onDestroy() {
        mSensorService = new Background_service(this);
        mServiceIntent = new Intent(this, Background_service.class);
      //  notificationManager.cancel(1);

        startService(mServiceIntent);
        Log.i("EXIT", "ondestroy of service!");

        // super.onDestroy();


        //Intent broadcastIntent = new Intent("uk.ac.shef.oak.ActivityRecognition.RestartSensor");
        // sendBroadcast(broadcastIntent);
        //getApplicationContext().startService(new Intent(getApplicationContext(), Background_service.class));;

    }

    @Override
    public void onLocationChanged(Location location) {
        lat = location.getLatitude();//current location
        lon = location.getLongitude();
        //display locatio day2 below
      //  Toast.makeText(getApplicationContext(),"lon:"+lon+"lat"+lat,Toast.LENGTH_SHORT).show();
        Log.i("location","longitude"+lon+"lattitude"+lat);
        Audio a = new Audio(this);
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(getApplicationContext().WIFI_SERVICE);
        //wifiManager.setWifiEnabled(true);
        DatabaseHandler db = new DatabaseHandler(this);
        List<Data_class> d = db.getsettings( lon,  lat);
        if (!d.isEmpty()) {
            //for (Data_class i : d) {
Log.i("got","sucesfull");
            // }
            int i=d.size();
            a.setvol(d.get(i-1).vol);
            if(d.get(i-1).wifi==1){
              if(! wifiManager.isWifiEnabled())
                    wifiManager.setWifiEnabled(true);}
            else
                ;
           // Toast.makeText(getApplicationContext(),"vol set",Toast.LENGTH_SHORT).show();
        }
        else
            Log.i("got","unsucesfull");
        db.close();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
         statusOfGPS = locm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if(statusOfGPS) {
            if(notificationManager!=null)
                notificationManager.cancel(1);
            locm.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    1,
                    1, this);
        }
        else{
           // turnGPSOn();

            create_notification();
            while(!statusOfGPS){
                statusOfGPS = locm.isProviderEnabled(LocationManager.GPS_PROVIDER);

            }
            locm.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    1,
                    1, this);
         //  statusOfGPS=locm.isProviderEnabled(LocationManager.GPS_PROVIDER);
           // }
           // super.onDestroy();
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
statusOfGPS=locm.isProviderEnabled(LocationManager.GPS_PROVIDER);
if(!statusOfGPS){
    create_notification();
}
else{
if(notificationManager!=null)
    notificationManager.cancel(1);}
    }

    @Override
    public void onProviderEnabled(String provider) {
       // notificationManager.cancel(1);
        notificationManager.cancel(1);

    }

    @Override
    public void onProviderDisabled(String provider) {
      //  Toast.makeText(getApplicationContext(),"please enable GPS",Toast.LENGTH_SHORT).show();
      //  turnGPSOn();

       // notificationManager.notify(1, mBuilder.build());
       // super.onDestroy();
        create_notification();
        super.onCreate();
    }

    public void create_notification(){
        mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_stat_name)
                        .setContentTitle("Auto Reset")
                        .setContentText("Please turn ON GPS for full functioning!")
                        .setOngoing(true)
        ;

        mBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, mBuilder.build());
    }

}


