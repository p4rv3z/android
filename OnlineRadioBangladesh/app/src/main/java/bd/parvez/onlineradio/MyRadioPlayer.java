package bd.parvez.onlineradio;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.telecom.TelecomManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * Created by Parvez on 6/3/2015.
 */
public class MyRadioPlayer extends Service implements
        MediaPlayer.OnCompletionListener, MediaPlayer.OnInfoListener,
        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
        MediaPlayer.OnSeekCompleteListener, MediaPlayer.OnBufferingUpdateListener {
    private static final String TAG = "Online Radio Bangladesh";
    private MediaPlayer mediaPlayer = new MediaPlayer();
    private String radioName, url;
    private boolean isCallComming = false;
    private PhoneStateListener phoneStateListener;
    private TelephonyManager telecomManager;
    public static final String BROADCAST_BUFFER = "bd.parvez.onlineradio";
    private Intent bufferIntent;

    @Override
    public void onCreate() {
        //buffer
        bufferIntent = new Intent(BROADCAST_BUFFER);
        //super.onCreate();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                stopSelf();
            }
        });
        mediaPlayer.setOnInfoListener(this);
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnErrorListener(this);
        mediaPlayer.setOnSeekCompleteListener(this);
        mediaPlayer.setOnBufferingUpdateListener(this);
        mediaPlayer.reset();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //listen call
        telecomManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        phoneStateListener = new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                super.onCallStateChanged(state, incomingNumber);
                switch (state) {
                    case TelephonyManager.CALL_STATE_OFFHOOK:
                    case TelephonyManager.CALL_STATE_RINGING:
                        if (mediaPlayer != null) {
                            pauseMedia();
                            isCallComming = true;
                        }
                        break;
                    case TelephonyManager.CALL_STATE_IDLE:
                        if (mediaPlayer != null) {
                            if (isCallComming) {
                                isCallComming = false;
                                playMedia();
                            }
                        }
                        break;
                }
            }
        };
        telecomManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
        ///PLAY RADIO(SERVICES)
        url = intent.getExtras().getString("url");
        radioName = intent.getExtras().getString("radioName");

        ///NOTIFICATION

        mediaPlayer.reset();
        if (!mediaPlayer.isPlaying()) {
            try {
                mediaPlayer.setDataSource(url);
                startBufferBroadcast();
                mediaPlayer.prepareAsync();
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        }
        return START_STICKY;
    }

    private void startBufferBroadcast() {
        bufferIntent.putExtra("buffering", "1");
        sendBroadcast(bufferIntent);
    }

    private void stopBufferBroadcast() {
        bufferIntent.putExtra("buffering", "0");
        sendBroadcast(bufferIntent);
    }

    private void pauseMedia() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }


    @Override
    public void onDestroy() {
        //stop notification
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
        }
        if (phoneStateListener != null) {
            telecomManager.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE);
        }
        super.onDestroy();
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {

    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        stopMedia();
        stopSelf();
    }

    private void stopMedia() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        switch (what) {
            case MediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK:
                Log.d(TAG, "MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK ");
                break;
            case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
                Log.d(TAG, "MEDIA_ERROR_SERVER_DIED");
                break;
            case MediaPlayer.MEDIA_ERROR_UNKNOWN:
                Log.d(TAG, "MEDIA_ERROR_UNKNOWN");
                break;
        }
        return false;
    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        stopBufferBroadcast();
        playMedia();

    }

    private void playMedia() {
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
    }

    @Override
    public void onSeekComplete(MediaPlayer mp) {

    }
}
