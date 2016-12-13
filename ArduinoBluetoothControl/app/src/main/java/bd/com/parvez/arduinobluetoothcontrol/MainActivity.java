package bd.com.parvez.arduinobluetoothcontrol;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import services.MyServices;

public class MainActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback, AdapterView.OnItemClickListener, View.OnClickListener {
    private static final int REQUEST = 0;
    private static String BLUETOOTH = Manifest.permission.BLUETOOTH;
    private static String BLUETOOTH_ADMIN = Manifest.permission.BLUETOOTH_ADMIN;
    private static String[] PERMISSIONS = {BLUETOOTH, BLUETOOTH_ADMIN};
    private boolean isBtConnected = false;
    private static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    public BluetoothAdapter bluetoothAdapter;
    BluetoothSocket btSocket;
    private StringBuilder sb = new StringBuilder();
    ArrayList list;
    ArrayAdapter<String> adapter;
    AlertDialog dialog;
    ListView lv;

    ImageButton forward,backward,right,left;
    Button auto,stop,send,temperature,light_on,light_off,night_mode,play;
    EditText et_send;
    TextView tv_temperature;

    ConnectedThread connectedThread;
    Handler handler;
    final int RECIEVE_MESSAGE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //button initialize
        btn_ini();
        handeler();
        MyServices.log("OnCreated Called.");
        //Check Android Version
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            MyServices.log("Marshamallaw Device. Get Permission.");
            if (isAccepted()) {
                requestPermissions(PERMISSIONS, REQUEST);
            }
        }
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            // Device does not support Bluetooth
            Toast.makeText(this, "!!!Your Device does not support Bluetooth!!!", Toast.LENGTH_LONG).show();
        } else {
            if (!bluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);
                Toast.makeText(this, "!!!Please turn on your bluetooth device!!!", Toast.LENGTH_LONG).show();
            }
        }

    }
private void handeler(){
    handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case RECIEVE_MESSAGE:													// if receive massage
                    String readMessage = (String) msg.obj;
                    sb.append(readMessage);
                    int endOfLineIndex = sb.indexOf("#");
                    if(endOfLineIndex>0){
                        int start = sb.indexOf("*");
                        int end = sb.indexOf("#");
                        String dataInPrint = sb.substring(start+1, end);
                        MyServices.log("Receive:"+dataInPrint);
                        tv_temperature.setText(dataInPrint.toString()+"\u00B0"+"C");
                        tv_temperature.setTextColor(Color.RED);
                        sb.delete(0,sb.length());
                        MyServices.log("Delete:"+sb.toString());
                    }
                 //   if(sb.charAt()) {
//                        int start = sb.indexOf("*");
//                        int end = sb.indexOf("#");
//                        sb.subSequence(start,end);
//                        MyServices.log("Receive::"+sb.toString());
//                        int eoi = sb.length();
//                        tv_temperature.setText(sb.toString()+"\u00B0"+"C");
//                        tv_temperature.setTextColor(Color.RED);
//                        sb.delete(0,eoi);
                 //   }
                    break;
            }
        }
    };
}
    private void btn_ini() {
        forward = (ImageButton) findViewById(R.id.imgbtn_forward);
        backward = (ImageButton) findViewById(R.id.imgbtn_back);
        right = (ImageButton) findViewById(R.id.imgbtn_right);
        left = (ImageButton) findViewById(R.id.imgbtn_left);

        auto = (Button) findViewById(R.id.btn_auto);
        stop = (Button) findViewById(R.id.btn_stop);
        send = (Button) findViewById(R.id.btn_send);
        temperature= (Button) findViewById(R.id.btn_temp);
        light_on= (Button) findViewById(R.id.btn_light_on);
        light_off= (Button) findViewById(R.id.btn_light_off);
        night_mode= (Button) findViewById(R.id.btn_night_mode);
        play= (Button) findViewById(R.id.btn_play);

        et_send = (EditText) findViewById(R.id.et_input);
        tv_temperature = (TextView) findViewById(R.id.tv_temp);

        forward.setOnClickListener(this);
        backward.setOnClickListener(this);
        right.setOnClickListener(this);
        left.setOnClickListener(this);

        auto.setOnClickListener(this);
        stop.setOnClickListener(this);
        send.setOnClickListener(this);
        temperature.setOnClickListener(this);
        light_off.setOnClickListener(this);
        night_mode.setOnClickListener(this);
        play.setOnClickListener(this);
        light_on.setOnClickListener(this);

        et_send.setOnClickListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        MyServices.log("OnStart Called.");
        if (bluetoothAdapter.isEnabled()) {
            MyServices.log("Bluetooth is enabled.");
            bluetoothList();
        } else {
            Toast.makeText(this, "!!!Please turn on your bluetooth device!!!", Toast.LENGTH_LONG).show();
        }
    }

    private void bluetoothList() {
        //
        list = new ArrayList();
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                list.add(device.getName() + "\n" + device.getAddress());
                MyServices.log("MAC::" + device.getAddress());
            }
        }


        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View convertView = (View) inflater.inflate(R.layout.custom_listview_dialog, null);
        alertDialog.setView(convertView);
        alertDialog.setTitle("Paired Devices List:");
        lv = (ListView) convertView.findViewById(R.id.list_view);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
        lv.setAdapter(adapter);
        //dialog.setCancelable(false);
        dialog = alertDialog.create();
        dialog.show();
        lv.setOnItemClickListener(this);
    }

    @TargetApi(Build.VERSION_CODES.M)
    private boolean isAccepted() {
        return checkSelfPermission(BLUETOOTH) != PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST) {
            if (PermissionUtil.verifyPermissions(grantResults)) {
                Toast.makeText(this, "!!!Permission Granted!!!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "!!!Permission Denied!!!", Toast.LENGTH_LONG).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        // Get the device MAC address, the last 17 chars in the View
        String info = ((TextView) view).getText().toString();
        String address = info.substring(info.length() - 17);
        new ConnectBT(address).execute();
        //handeler();
        dialog.dismiss();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.imgbtn_forward:
                connectedThread.write(MyServices.FORWARD_CMD.toString());
                break;
            case R.id.imgbtn_back:
                connectedThread.write(MyServices.BACKWARD_CMD.toString());
                break;
            case R.id.imgbtn_right:
                connectedThread.write(MyServices.RIGHT_CMD.toString());
                break;
            case R.id.imgbtn_left:
                connectedThread.write(MyServices.LEFT_CMD.toString());
                break;
            case R.id.btn_auto:
                connectedThread.write(MyServices.AUTO_CMD.toString());
                break;
            case R.id.btn_stop:
                connectedThread.write(MyServices.STOP_CMD.toString());
                break;
            case R.id.btn_light_on:
                connectedThread.write(MyServices.LIGHT_ON_CMD.toString());
                break;
            case R.id.btn_light_off:
                connectedThread.write(MyServices.LIGHT_OFF_CMD.toString());
                break;
            case R.id.btn_play:
                connectedThread.write(MyServices.PLAY_CMD.toString());
                break;
            case R.id.btn_night_mode:
                connectedThread.write(MyServices.NIGHT_MODE_CMD.toString());
                break;
            case R.id.btn_send:
                String val = et_send.getText().toString().trim();
                connectedThread.write(val);
                break;
            case R.id.btn_temp:
                connectedThread.write(MyServices.TEMPERATURE_CMD.toString());
                break;


        }
    }

    public void about(View view) {
        Intent intent = new Intent(this,About.class);
        startActivity(intent);
    }


    private class ConnectBT extends AsyncTask<Void, Void, Void>  // UI thread
    {
        private String address;
        private ProgressDialog progress;
        private boolean ConnectSuccess = true; //if it's here, it's almost connected

        public ConnectBT(String address){
            this.address = address;
        }
        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(MainActivity.this, "Connecting...", "Please wait!!!");  //show a progress dialog
        }

        @Override
        protected Void doInBackground(Void... devices) //while the progress dialog is shown, the connection is done in background
        {
            try {
                if (btSocket == null || !isBtConnected) {
                    //inStream = btSocket.getInputStream();
                    //bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                    BluetoothDevice dispositivo = bluetoothAdapter.getRemoteDevice(address);//connects to the device's address and checks if it's available
                    btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);//create a RFCOMM (SPP) connection
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    btSocket.connect();//start connection
                }
            } catch (IOException e) {
                ConnectSuccess = false;//if the try failed, you can check the exception here
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) //after the doInBackground, it checks if everything went fine
        {
            super.onPostExecute(result);

            if (!ConnectSuccess) {
                MyServices.log("Connection Failed. Is it a SPP Bluetooth? Try again.");
                finish();
            } else {
                MyServices.log("Connected.");
                connectedThread = new ConnectedThread(btSocket);
                connectedThread.start();
                isBtConnected = true;
            }
            progress.dismiss();
        }
    }

////////////////todo class Connected Thread
    private class ConnectedThread extends Thread{
    private final InputStream mmInStream;
    private final OutputStream mmOutStream;
    private BluetoothSocket socket;

    //creation of the connect thread
    public ConnectedThread(BluetoothSocket socket) {
        this.socket =socket;
        InputStream tmpIn = null;
        OutputStream tmpOut = null;

        try {
            //Create I/O streams for connection
            tmpIn = socket.getInputStream();
            tmpOut = socket.getOutputStream();
        } catch (IOException e) { }

        mmInStream = tmpIn;
        mmOutStream = tmpOut;
    }

    public void write(String message) {
        byte[] msgBuffer = message.getBytes();
        try {
            mmOutStream.write(msgBuffer);
        } catch (IOException e) {
        }
    }
    public void run() {
        byte[] buffer = new byte[256];
        int bytes;

        // Keep looping to listen for received messages
        while (true) {
            try {
                bytes = mmInStream.read(buffer);            //read bytes from input buffer
                String readMessage = new String(buffer, 0, bytes);
                // Send the obtained bytes to the UI Activity via handler
                handler.obtainMessage(RECIEVE_MESSAGE, bytes, -1, readMessage).sendToTarget();
            } catch (IOException e) {
                break;
            }
        }
    }
    public void cancel() {
        try {
            socket.close();
        } catch (IOException e) { }
    }
}
}
