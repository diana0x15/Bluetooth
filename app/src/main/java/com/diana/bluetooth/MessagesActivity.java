package com.diana.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;

public class MessagesActivity extends Activity {

    protected static final int SUCCESS_CONNECT = 0;
    protected static final int MESSAGE_READ = 1;
    protected static final int MESSAGE_WRITE = 2;
    protected static final int FAIL_CONNECT = 3;
    public static String EXTRA_DEVICE_ADDRESS = "device_address";

    Set<BluetoothDevice> mPairedDevices = MainActivity.mPairedDevices;
    BluetoothDevice connectedDevice = null;
    ConnectThread mConnectThread;
    ConnectedThread mConnectedThread;
    String deviceAddress;

    int SIZE = -1, R, G, B;

    static TextView text;
    FrameLayout layout;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case SUCCESS_CONNECT:
                    mConnectedThread = new ConnectedThread((BluetoothSocket) msg.obj, mHandler);
                    text.setText("");
                    Toast.makeText(MessagesActivity.this, "Connected to " + connectedDevice.getName(),
                            Toast.LENGTH_SHORT).show();
                    mConnectedThread.start();
                    break;

                case MESSAGE_READ:
                    char[] c = (char[])msg.obj;
                    text.setText(c, 0, msg.arg1);
                    decode(c);
                    break;

                case MESSAGE_WRITE:
                    byte[] writeBuf = (byte[]) msg.obj;
                    // construct a string from the buffer
                    String writeMessage = new String(writeBuf);
                    break;

                case FAIL_CONNECT:
                    Toast.makeText(MessagesActivity.this, "Can't connect to device", Toast.LENGTH_LONG).show();
                    finish();
                    break;
            }
        }
    };

    private void decode(char[] code) {

        int x = 0, y = 0, r, x2, y2, h, w;
        switch(code[0]){
            case(1):// Set screen
                x = code[1] - 48;
                x *= 10;
                x += code[2] - 48;
                x*=10;
                x += code[3] - 48;
                x *= 10;
                x += code[4] - 48;

                y = code[5] - 48;
                y *= 10;
                y += code[6] - 48;
                y *= 10;
                y += code[7] - 48;
                y *= 10;
                y += code[8] - 48;

                layout.getLayoutParams().height = x;
                layout.getLayoutParams().width = y;
                layout.setVisibility(View.VISIBLE);
                if(SIZE == -1)
                    SIZE = layout.getHeight()/30;
                break;
            case(2): // Word
                x = code[1] - 48;
                x *= 10;
                x += code[2] - 48;
                x*=10;
                x += code[3] - 48;
                x *= 10;
                x += code[4] - 48;

                y = code[5] - 48;
                y *= 10;
                y += code[6] - 48;
                y *= 10;
                y += code[7] - 48;
                y *= 10;
                y += code[8] - 48;

                String s = "";
                for(int i = 9; i < code.length; ++i) {
                    s += code[i];
                }
                layout.addView(new Text(getApplicationContext(), x, y, SIZE, s));
                break;
            default:
                break;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mConnectThread != null) {
            mConnectThread.cancel(getApplicationContext());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        deviceAddress = getIntent().getStringExtra(EXTRA_DEVICE_ADDRESS);
        for (BluetoothDevice device : mPairedDevices) {
            if (device.getAddress().equals(deviceAddress)) {
                connectedDevice = device;
                break;
            }
        }
        if (connectedDevice == null) {
            finish();
        }

        mConnectThread = new ConnectThread(connectedDevice, mHandler);
        mConnectThread.start();
        setContentView(com.diana.bluetooth.R.layout.activity_messages);
        text = (TextView) findViewById(com.diana.bluetooth.R.id.textView);
        text.setText("Connecting...");
        layout = (FrameLayout) findViewById(com.diana.bluetooth.R.id.frameLayout);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(com.diana.bluetooth.R.menu.menu_messages, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == com.diana.bluetooth.R.id.action_codes) {
            Intent intent = new Intent(MessagesActivity.this, Help.class);
            startActivity(intent);
            overridePendingTransition(com.diana.bluetooth.R.anim.right_slide_in, com.diana.bluetooth.R.anim.right_slide_out);
        }
        return super.onOptionsItemSelected(item);
    }
}

