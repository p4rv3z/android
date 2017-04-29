package bd.parvez.onlineradio;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.audiofx.Visualizer;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.AlarmClock;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;


public class MainActivity extends Activity implements View.OnClickListener {
    private ImageView imgVolume, imgRecord, imgPlay, imgNext, imgPrevious, imgNewActivity, imgSleep, imgFav, imgAlarm;
    private TextView tvName;
    private SeekBar volumeSeekBar;
    private int progressValue;
    private AudioManager audioManager;
    private AdView bannerAd;
    private AdRequest bannerAdRequest;
    private Animation animRecord;
    private boolean rec = false;
    private boolean play = false;
    private static final String TAG = "Online Radio Bangladesh";
    private VisualizerView mVisualizerView;
    private Intent radioPlayerServices;
    private Visualizer mVisualizer;
    private AlertDialog.Builder exitBuilder;
    private AlertDialog exitAlert;
    int streamType;
    private Handler handler;
    private RadioDB radioDB;
    private RecordData recordData;
    private SharedPreferences sp;
    private SharedPreferences.Editor spEditor;
    private SharedPreferences agreeSp;
    private RelativeLayout newActivity;
    private boolean isBuffering;
    private ProgressDialog progressDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        agreeSp = getSharedPreferences("agreement", MODE_PRIVATE);
        if (agreementValue() == 0) {
            agreementMethod();
        }
        setVolumeControlStream(AudioManager.STREAM_MUSIC);//taking audio services
        initDB();//database
        sp = getSharedPreferences("position", MODE_PRIVATE);//create sharedpreferences
        //position = getPosition();
        init();//initialize
        clickListener();//onClickListener
        bannerAdRequest();//ad request for banner
        volumeSeekBar();// changing volume
        initAudio();
        setRadioServices();
    }

    private void agreementMethod() {
        AlertDialog.Builder agreementDia = new AlertDialog.Builder(this);
        agreementDia.setTitle("Agreement");
        agreementDia.setMessage(Value.valueOfAgreement);
        agreementDia.setCancelable(false);
        agreementDia.setPositiveButton("AGREE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferences.Editor editorSp = agreeSp.edit();
                editorSp.putInt("agreement", 1);
                editorSp.commit();
                dialog.dismiss();
            }
        });
        agreementDia.setNeutralButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                System.exit(0);
                dialog.dismiss();
            }
        });
        agreementDia.create();
        agreementDia.show();
    }

    private int agreementValue() {
        return agreeSp.getInt("agreement", 0);
    }


    private int getPosition() {
        return sp.getInt("position", 0);
    }

    private void setPosition(int pos) {
        spEditor = sp.edit();
        spEditor.putInt("position", pos);
        spEditor.commit();
    }

    private void defaultUI() {
        String name = radioDB.getAllData().get(getPosition()).getName().toString();
        int f = radioDB.getAllData().get(getPosition()).getFav();
        tvName.setText(name);
        Log.v(TAG, "Name:" + name + "FAV: " + f);
        if (f == 1) {
            Log.v(TAG, "true set");
            imgFav.setImageResource(R.mipmap.fav_ena);
        } else {
            imgFav.setImageResource(R.mipmap.fav_dis);
            Log.v(TAG, "false set");
        }
        if (MyServices.isMyServiceRunning(MyRadioPlayer.class, this)) {
            imgPlay.setImageResource(R.mipmap.pause);
            play = true;
        } else {
            play = false;
            imgPlay.setImageResource(R.mipmap.play);
        }
    }

    private void initDB() {
        radioDB = RadioDB.getInstance(this);
        //allRadioClass = radioDB.getAllData();
    }

    private void setRadioServices() {
        try {
            radioPlayerServices = new Intent(this, MyRadioPlayer.class);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        if (isMyServiceRunning(MyRadioPlayer.class)) {
            imgPlay.setImageResource(R.mipmap.pause);
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private void initAudio() {

        streamType = AudioManager.STREAM_MUSIC;
        setupVisualizerFxAndUI();
        setupVisualizerFxAndUI();
        mVisualizer.setEnabled(true);

    }

    private void setupVisualizerFxAndUI() {

        // Creating a Visualizer on the output mix (audio session 0)
        // need permission MODIFY_AUDIO_SETTINGS
        mVisualizer = new Visualizer(0);

        mVisualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);
        mVisualizer.setDataCaptureListener(
                new Visualizer.OnDataCaptureListener() {
                    public void onWaveFormDataCapture(Visualizer visualizer,
                                                      byte[] bytes, int samplingRate) {
                        mVisualizerView.updateVisualizer(bytes);
                    }

                    public void onFftDataCapture(Visualizer visualizer,
                                                 byte[] bytes, int samplingRate) {
                    }
                }, Visualizer.getMaxCaptureRate() / 2, true, false);

    }

    private void volumeSeekBar() {
        try {
            audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            volumeSeekBar.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
            progressValue = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            setProgressImage(progressValue);
            volumeSeekBar.setProgress(progressValue);


            volumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onStopTrackingTouch(SeekBar arg0) {
                    volumeSeekBar.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onStartTrackingTouch(SeekBar arg0) {
                }

                @Override
                public void onProgressChanged(SeekBar arg0, int progress, boolean arg2) {
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
                    setProgressImage(progress);

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setProgressImage(int progress) {
        if (progress == 0) {
            imgVolume.setImageResource(R.mipmap.volume_silent);
        } else if (progress <= 5 && progress >= 1) {
            imgVolume.setImageResource(R.mipmap.volume_under_30);
        } else if (progress >= 6 && progress <= 10) {
            imgVolume.setImageResource(R.mipmap.volume_under_80);
        } else if (progress >= 11) {
            imgVolume.setImageResource(R.mipmap.volume_up);
        } else {
            imgVolume.setImageResource(R.mipmap.volume_up);
        }
    }

    private void bannerAdRequest() {
        //todo Testing Banner Ad
        //bannerAdRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();//test ad
        bannerAdRequest = new AdRequest.Builder().build(); // real
        bannerAd.loadAd(bannerAdRequest);
    }

    private void clickListener() {
        imgVolume.setOnClickListener(this);
        imgRecord.setOnClickListener(this);
        imgPlay.setOnClickListener(this);
        imgNext.setOnClickListener(this);
        imgPrevious.setOnClickListener(this);
        imgNewActivity.setOnClickListener(this);
        imgSleep.setOnClickListener(this);
        imgFav.setOnClickListener(this);
        imgAlarm.setOnClickListener(this);
        newActivity.setOnClickListener(this);
    }

    private void init() {
        tvName = (TextView) findViewById(R.id.tv_title_main_3);
        imgFav = (ImageView) findViewById(R.id.img_main_fav);
        imgVolume = (ImageView) findViewById(R.id.img_main_volume);
        imgRecord = (ImageView) findViewById(R.id.img_main_record);
        imgPlay = (ImageView) findViewById(R.id.img_main_play);
        imgNext = (ImageView) findViewById(R.id.img_main_next);
        imgPrevious = (ImageView) findViewById(R.id.img_main_previous);
        imgSleep = (ImageView) findViewById(R.id.img_main_sleep);
        imgNewActivity = (ImageView) findViewById(R.id.img_main_new_activity);
        imgAlarm = (ImageView) findViewById(R.id.img_main_alarm);
        animRecord = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_record);
        bannerAd = (AdView) findViewById(R.id.banner_ad);
        volumeSeekBar = (SeekBar) findViewById(R.id.seekBar_main_volume);
        mVisualizerView = (VisualizerView) findViewById(R.id.myvisualizerview);
        newActivity = (RelativeLayout) findViewById(R.id.rel_main_body);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_main_volume:
                //volume
                Log.d(TAG, "Volume click");
                volumeSeekBar.setVisibility(View.VISIBLE);
                break;
            case R.id.img_main_record:
                animRecord();
                break;
            case R.id.img_main_play:
                //play
                //int p = getPosition();
                play();
                defaultUI();
                break;
            case R.id.img_main_next:
                //next
                int n = getPosition() + 1;
                if (n < radioDB.getAllData().size()) {
                    setPosition(n);
                    defaultUI();
                    play = false;
                    play();
                    Log.v(TAG, "position " + n + " Saved Position " + getPosition());
                } else {
                    Toast.makeText(this, "This is the last one.\nPlease click Previous.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.img_main_previous:
                int pre = getPosition() - 1;
                if (pre >= 0) {
                    setPosition(pre);
                    defaultUI();
                    play = false;
                    play();
                    Log.v(TAG, "position " + pre + " Saved Position " + getPosition());
                } else {
                    Toast.makeText(this, "This is the last one.\nPlease click Next.", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.img_main_new_activity:
            case R.id.rel_main_body:
                Intent newActivity = new Intent(this, ListOfRadio.class);
                startActivity(newActivity);
                overridePendingTransition(R.anim.slide_from, R.anim.slide_to);
                break;
            case R.id.img_main_sleep:
                final String[] minuteOption = {"After 5 Minutes", "After 15 Minutes", "After 30 Minutes", "After 1 Hour", "After 2 Hours"};
                final int[] minute = {5, 15, 30, 60, 120};
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, minuteOption);
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                //builder.setTitle("Select your Item");
                builder.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "Radio will close " + minuteOption[which], Toast.LENGTH_LONG).show();
                        handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                finish();

                            }
                        }, (minute[which] * 1000 * 60));
                        imgSleep.setEnabled(false);
                    }
                });
                builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create();
                builder.show();
                break;
            case R.id.img_main_fav:
                int f = radioDB.getAllData().get(getPosition()).getFav();
                String msg[] = {"ADD ", "REMOVE "};
                String prepo[] = {" to", " from "};
                String name = radioDB.getAllData().get(getPosition()).getName().toUpperCase().toString();
                AlertDialog.Builder favAlert = new AlertDialog.Builder(this);
                //builder.setTitle("Select your Item");
                favAlert.setMessage("Are you sure?\nYou want to " + msg[f] + " " + name + prepo[f] + " your favourite list.");
                favAlert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int id = radioDB.getAllData().get(getPosition()).getId();
                        Log.v(TAG, radioDB.getAllData().get(getPosition()).getFav() + "");
                        if (radioDB.getAllData().get(getPosition()).getFav() == 0) {
                            radioDB.updateFav(id, 1);
                            defaultUI();
                        } else {
                            radioDB.updateFav(id, 0);
                            defaultUI();
                        }
                        dialog.dismiss();
                    }
                });
                favAlert.setNeutralButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                favAlert.create();
                favAlert.show();
                break;
            case R.id.img_main_alarm:
                addAlarm(v);
                break;
            default:
                break;
        }
    }

    private void addAlarm(View v) {
        final Dialog alarmDia = new Dialog(this);
        alarmDia.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alarmDia.setContentView(R.layout.time_picker);
        final String zz = radioDB.getAllData().get(getPosition()).getName().toString();
        final TimePicker pickTime = (TimePicker) alarmDia.findViewById(R.id.timePicker_alarm);
        final Button ok = (Button) alarmDia.findViewById(R.id.btn_ok_alarm_add);
        final Button cancel = (Button) alarmDia.findViewById(R.id.btn_cancel_alarm_add);
        final EditText alarmName = (EditText) alarmDia.findViewById(R.id.alarm_name_timepicker);
        alarmName.setText(zz);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour = pickTime.getCurrentHour();
                int minute = pickTime.getCurrentMinute();
                String alarm_name = alarmName.getText().toString();
                if (alarm_name.isEmpty()) {
                    alarm_name = zz;
                }
                Intent alarm = new Intent(AlarmClock.ACTION_SET_ALARM);
                alarm.putExtra(AlarmClock.EXTRA_MESSAGE, alarm_name);
                alarm.putExtra(AlarmClock.EXTRA_HOUR, hour);
                alarm.putExtra(AlarmClock.EXTRA_MINUTES, minute);
                startActivity(alarm);
                Toast.makeText(MainActivity.this, "alarm set", Toast.LENGTH_LONG).show();
                alarmDia.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alarmDia.dismiss();
            }
        });
        alarmDia.show();
    }

    private void startRecord() {
        String name = radioDB.getAllData().get(getPosition()).getName().toString();
        String url = radioDB.getAllData().get(getPosition()).getUrl().trim().toString();
        recordData = new RecordData(this, name);
        recordData.execute(url);

    }

    private void stopRecording() {
        recordData.p = false;
    }

    private void play() {
        //String name = allRadioClass.get(pos).getName().toString();
        String name = radioDB.getAllData().get(getPosition()).getName().toString();
        String url = radioDB.getAllData().get(getPosition()).getUrl().trim().toString();
        if (!play) {
            Log.v(TAG, "Radio Start");
            play = true;
            if (checkConnectivity()) {
                try {
                    if (isMyServiceRunning(MyRadioPlayer.class)) {
                        stopService(radioPlayerServices);
                    }
                } catch (Exception e) {
                    Log.v(TAG, e.getMessage());
                }
                radioPlayerServices.putExtra("url", url);
                radioPlayerServices.putExtra("radioName", name);
                try {
                    imgPlay.setImageResource(R.mipmap.pause);
                    startService(radioPlayerServices);
                } catch (Exception e) {
                    Log.v(TAG, e.getMessage());
                }
            } else {
                Toast.makeText(MainActivity.this, "Please Connect your internet.", Toast.LENGTH_LONG).show();
            }

        } else {
            Log.v(TAG, "Radio Stop");
            play = false;
            try {
                imgPlay.setImageResource(R.mipmap.play);
                stopService(radioPlayerServices);
            } catch (Exception e) {
                Log.v(TAG, e.getMessage());
            }
        }
    }

    public boolean checkConnectivity() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting()
                || connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }

    private void animRecord() {
        if (!rec) {
            if (MyServices.checkConnectivity(this)) {
                rec = true;
                Log.v(TAG, "REC Animation Start");
                imgRecord.startAnimation(animRecord);
                startRecord();
            } else {
                Toast.makeText(this, "Please connect your Internet", Toast.LENGTH_LONG).show();
            }
        } else {
            Log.v(TAG, "REC Animation Stop");
            imgRecord.clearAnimation();
            stopRecording();
            rec = false;
        }

    }

    /**
     * Called when leaving the activity
     */
    @Override
    public void onPause() {
        if (bannerAd != null) {
            bannerAd.pause();
        }
        if (isFinishing()) {
            mVisualizer.release();
        }
        if (isBuffering) {
            unregisterReceiver(broadcastReceiver);
            isBuffering = false;
        }
        super.onPause();
    }

    /**
     * Called when returning to the activity
     */
    @Override
    public void onResume() {
        super.onResume();
        if (bannerAd != null) {
            bannerAd.resume();
        }
        defaultUI();
        if (!isBuffering) {
            registerReceiver(broadcastReceiver, new IntentFilter(MyRadioPlayer.BROADCAST_BUFFER));
            isBuffering = true;
        }
    }

    /**
     * Called before the activity is destroyed
     */
    @Override
    public void onDestroy() {
        if (bannerAd != null) {
            bannerAd.destroy();
        }
        if (isMyServiceRunning(MyRadioPlayer.class)) {
            try {
                stopService(radioPlayerServices);
            } catch (Exception e) {
                Log.v(TAG, "Stop playing");
            }
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {

        //super.onBackPressed();
        exitBuilder = new AlertDialog.Builder(this);
        exitBuilder.setMessage("Are you sure?\nYou want to Exit.");

        exitBuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                finish();
                dialog.dismiss();
            }
        });
        exitBuilder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        exitBuilder.setNeutralButton("Play in Background", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                MainActivity.this.moveTaskToBack(true);
                dialog.dismiss();
            }
        });

        exitAlert = exitBuilder.create();
        exitAlert.show();

    }

    public void aboutDev(View view) {
        Intent about = new Intent(this, About.class);
        startActivity(about);
        overridePendingTransition(R.anim.slide_from, R.anim.slide_to);
    }

    private void showProgressDialog(Intent bufferIntent) {
        String bufferValue = bufferIntent.getStringExtra("buffering");
        int bufferIntValue = Integer.parseInt(bufferValue);
        switch (bufferIntValue) {
            case 0:
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                break;
            case 1:
                bufferDialog();
                break;
        }
    }

    private void bufferDialog() {
        progressDialog = ProgressDialog.show(this, "Buffering...", "Acquiring...", true);
        progressDialog.setCancelable(true);
        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                onClick( imgPlay);
                progressDialog.dismiss();

            }
        });
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            showProgressDialog(intent);
        }
    };
}