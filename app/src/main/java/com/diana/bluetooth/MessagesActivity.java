package com.diana.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;


public class MessagesActivity extends Activity {

    protected static final int SUCCESS_CONNECT = 0;
    protected static final int MESSAGE_READ = 1;
    protected static final int MESSAGE_WRITE = 2;
    public static String EXTRA_DEVICE_ADDRESS = "device_address";

    Set<BluetoothDevice> mPairedDevices = MainActivity.mPairedDevices;
    BluetoothDevice connectedDevice = null;
    ConnectThread mConnectThread;
    ConnectedThread mConnectedThread;
    String deviceAddress;

    static TextView text;

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub

            super.handleMessage(msg);

            switch(msg.what){
                case SUCCESS_CONNECT:
                    mConnectedThread = new ConnectedThread((BluetoothSocket)msg.obj, mHandler);
                    Toast.makeText(MessagesActivity.this, "Connected to " + connectedDevice.getName(),
                            Toast.LENGTH_SHORT).show();
                    mConnectedThread.start();
                    break;

                case MESSAGE_READ:
                    byte[] readBuf = (byte[])msg.obj;
                    String string = new String(readBuf, 0, msg.arg1);
                    text.append(string);
                    break;

                case MESSAGE_WRITE:
                    byte[] writeBuf = (byte[]) msg.obj;
                    // construct a string from the buffer
                    String writeMessage = new String(writeBuf);
                    Toast.makeText(MessagesActivity.this, "Write", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(mConnectThread != null) {
            mConnectThread.cancel(getApplicationContext());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //setContentView(new MyView(this));
        setContentView(R.layout.activity_messages);

        deviceAddress = getIntent().getStringExtra(EXTRA_DEVICE_ADDRESS);
        for(BluetoothDevice device : mPairedDevices) {
            if(device.getAddress().equals(deviceAddress)) {
                connectedDevice = device;
                break;
            }
        }
        if(connectedDevice == null) {
            finish();
        }

        mConnectThread = new ConnectThread(connectedDevice, mHandler);
        mConnectThread.start();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_messages, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
