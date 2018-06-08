package com.eazico.autoreset;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.media.ToneGenerator;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;


import java.util.List;

import static android.media.AudioManager.STREAM_ACCESSIBILITY;
import static android.media.AudioManager.STREAM_MUSIC;
import static android.media.AudioManager.STREAM_RING;

public class MainActivity extends AppCompatActivity  {
GPS g;
boolean touch1=false;
    SharedPreferences prefs = null;
Button ok;
SeekBar s;
int w;
//Button ok2;
boolean touch2;
Switch wf;
GPS init;
TextView prog;


TextView gpv;
Button set_wifi;
    Intent mServiceIntent;
  LocationManager locm;
     Context cm;
    private AdView mAdView;
    private Background_service mSensorService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //start service






        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);



       // gpv=findViewById(R.id.gps);
      //  GPS g=new GPS(getApplicationContext());
        prefs = getSharedPreferences("com.eazico.AutoReset", MODE_PRIVATE);
        s=(SeekBar)findViewById(R.id.seekBar);

        ok=(Button)findViewById(R.id.ok);
        prog=(TextView)findViewById(R.id.prog) ;
        prog.setText("0");
        touch2=false;

             //find current location
    //  ok2=(Button)findViewById(R.id.ook);
       wf=(Switch)findViewById(R.id.sw);
        init=new GPS(getApplicationContext());
        set_wifi=findViewById(R.id.set_wifi);
         cm=getApplicationContext();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 1000ms
                mSensorService = new Background_service(getApplicationContext());
                mServiceIntent = new Intent(getApplicationContext(),Background_service.class );
              if (!isMyServiceRunning(Background_service.class)) {
                   cm.startService(mServiceIntent);
               }
               else
                   Log.i("status:","running");
            }
        }, 1000);




    }
    @Override
    public void onDestroy() {

        mSensorService = new Background_service(this);
        mServiceIntent = new Intent(this, Background_service.class);
        startService(mServiceIntent);
        Log.i("EXIT_a", "ondestroy main!");

        super.onDestroy();

    }
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i ("isMyServiceRunning?", true+"");
                return true;
            }
        }
        Log.i ("isMyServiceRunning?", false+"");
        return false;
    }




    @Override
    protected void onResume() {
        super.onResume();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SECURE);
        if (prefs.getBoolean("firstrun", true)) {
            // Do first run stuff here then set 'firstrun' as false
            // using the following line to edit/commit prefs
           // DatabaseHandler db = new DatabaseHandler(this);
           // DatabaseHandler db=new DatabaseHandler(getApplicationContext());;
            if(!locm.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                if (isMyServiceRunning(Background_service.class)) {
                    cm.stopService(mServiceIntent);
                }
            }
            else{
                if (!isMyServiceRunning(Background_service.class)) {
                    cm.startService(mServiceIntent);
                }
            }
            prefs.edit().putBoolean("firstrun", false).commit();
        }


    }
    int v;

    public void reset_vol(View view) {
        locm = (LocationManager) getSystemService(Context.LOCATION_SERVICE );
        boolean statusOfGPS = locm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if(touch1==false){
            if(touch2==false)
            {

                if(statusOfGPS)
                ok.setVisibility(View.VISIBLE);
            else Toast.makeText(getApplicationContext(),"Please turn ON GPS",Toast.LENGTH_LONG).show();}
        g=new GPS(getApplicationContext());
        set_wifi.animate().translationY(120);
        s.setVisibility(View.VISIBLE);
        prog.setVisibility(View.VISIBLE);

            if(statusOfGPS)
                     ok.setVisibility(View.VISIBLE);



        touch1=true;
       // Toast to;
      final  AudioManager audioManager = (AudioManager) getApplicationContext().getSystemService(getApplicationContext().AUDIO_SERVICE);
        s.setMax(audioManager.getStreamMaxVolume(STREAM_MUSIC));
        s.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            Toast to;
            @Override

            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
           // prog=new TextView(getApplicationContext());
            prog.setText(String.valueOf(i));
               // int val = (i * (seekBar.getWidth() - 2 * seekBar.getThumbOffset())) / seekBar.getMax();
          //  prog.setX(seekBar.getX() + val + seekBar.getThumbOffset() / 2);

              //  audioManager.setStreamVolume(AudioManager.STREAM_RING, i, 0);
               // to= Toast.makeText(getApplicationContext(),"volume:"+i,Toast.LENGTH_SHORT);
                v=i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
             //  to.show();

             // ToneGenerator toneG=new ToneGenerator(STREAM_RING,15);
            //  toneG.startTone(ToneGenerator.TONE_CDMA_ABBR_ALERT, 1500);
               /* int tempv=audioManager.getStreamVolume(STREAM_RING);
                try {
                    Uri sound = RingtoneManager.getDefaultUri(1);

                    audioManager.setStreamVolume(STREAM_RING,v,0);
                    Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), sound);
                   r.play();

               } catch (Exception e) {
                   e.printStackTrace();
               }
               finally {
                    audioManager.setStreamVolume(STREAM_RING,tempv,0);
                }*/
             //   AudioManager am = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
                ; //This will be half of the default system sound

              //  audioManager.playSoundEffect(AudioManager.FLAG_PLAY_SOUND, 1);

            }
        });}
        else{
            touch1=false;
            s.setVisibility(View.INVISIBLE);
            prog.setVisibility(View.INVISIBLE);
            if(touch2==false)
                ok.setVisibility(View.INVISIBLE);
          //  ok.setVisibility(View.INVISIBLE);
            set_wifi.animate().translationY(0);
        }
    }

    public void reset_wifi(View view) {
if(touch2==false) {
    wf.setVisibility(View.VISIBLE);
    locm = (LocationManager) getSystemService(Context.LOCATION_SERVICE );
    boolean statusOfGPS = locm.isProviderEnabled(LocationManager.GPS_PROVIDER);

    if(touch1==false )
    {if(statusOfGPS)
    ok.setVisibility(View.VISIBLE);
    else
    Toast.makeText(getApplicationContext(),"Please turn ON GPS",Toast.LENGTH_LONG).show();}
   // ok2.setVisibility(View.VISIBLE);
    touch2 = true;
}
else{
    touch2=false;
    wf.setVisibility(View.INVISIBLE);
    if(touch1==false)
        ok.setVisibility(View.INVISIBLE);

   // ok2.setVisibility(View.INVISIBLE);
}
    }

    public void store_audio(View view) {

        Location l=null;
        if(wf.isChecked())
            w=1;
        else
            w=0;
        ;
       // g.update(getApplicationContext());

        double lon=g.lon;
        double lat=g.lat;
      while (lat==-1||lon==-1)
        {lon=g.lon;lat=g.lat;}


     //   Toast.makeText(getApplicationContext(),"longitude:"+lon+"lattitude:"+lat,Toast.LENGTH_SHORT).show();
        DatabaseHandler db = new DatabaseHandler(this);
       add_entry_status stat= db.add_entry(lat,lon,v,w);
       if(stat.type==0){
           if(stat.status==-1)
               Toast.makeText(getApplicationContext(),"Error adding the profile  please try again..",Toast.LENGTH_SHORT).show();
            else
               Toast.makeText(getApplicationContext(),"Profile added sucessfully..",Toast.LENGTH_SHORT).show();

       }
       else {
           if(stat.status==0)
               Toast.makeText(getApplicationContext(),"Error updating the profile please try again..",Toast.LENGTH_SHORT).show();
           else
               Toast.makeText(getApplicationContext(),"Profile updated sucessfully..",Toast.LENGTH_SHORT).show();

       }
        //display current status of database
//     List< Data_class> d= db.getsettings(lat,lon);
  //   String st="";
    //for(Data_class i: d){
      //  st+="longitude:"+i.lon+"lattitude:"+i.lat+"vol:"+i.vol+"wifi"+i.wifi+"\n";
    //}
    //Toast.makeText(getApplicationContext(),st,Toast.LENGTH_SHORT).show();


    }

    public void change_switch(View view) {
        if(wf.isChecked())
            wf.setText("On");
        else
            wf.setText("Off");
    }
    @Override
    protected void onPause() {
        super.onPause();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,WindowManager.LayoutParams.FLAG_SECURE);

       // super.onPause();
    }
















}


