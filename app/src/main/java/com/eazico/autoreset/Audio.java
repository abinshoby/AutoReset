package com.eazico.autoreset;

import android.app.NotificationManager;
import android.content.Context;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

public class Audio {
    Context cm;

    Audio(Context cm) {
        this.cm = cm;
    }

    //AudioManager audioManager = (AudioManager) cm.getSystemService(cm.AUDIO_SERVICE);
    // audioManager.//setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
    public void setvol(int vol) {
        AudioManager audioManager = (AudioManager) cm.getSystemService(cm.AUDIO_SERVICE);
int a=0;int b=0;
        if (audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) != vol) {
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, vol, AudioManager.FLAG_PLAY_SOUND);
            a=1;
          //  Toast.makeText(cm, "music vol set to" + vol, Toast.LENGTH_SHORT).show();
        }
        if (audioManager.getStreamVolume(AudioManager.STREAM_RING) != vol) {
            audioManager.setStreamVolume(AudioManager.STREAM_RING, vol, AudioManager.FLAG_PLAY_SOUND);
           // Toast.makeText(cm, "Ring vol set to" + vol, Toast.LENGTH_SHORT).show();
            b=1;
        }
        if(a==1||b==1)
        {create_notification(vol,vol);
           // Toast.makeText(cm, "notification", Toast.LENGTH_SHORT).show();
            }

        a=0;b=0;
    }


    public void create_notification(int ringvol,int musicvol) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(cm)
                .setSmallIcon(R.drawable.ic_stat_name)
                .setContentTitle("Auto Reset")
                .setContentText(" volume is set to "+ringvol)
                .setOngoing(false);
        ;

        mBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        NotificationManager notificationManager = (NotificationManager) cm.getSystemService(cm.NOTIFICATION_SERVICE);
        notificationManager.notify(2, mBuilder.build());
    }
}
