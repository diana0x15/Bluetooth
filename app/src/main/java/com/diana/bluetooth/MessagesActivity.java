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

    static int R = 255, G = 255, B = 255, COUNT = 0;
    static float SIZE = -1;

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

        int x = 0, y = 0, r = 0, x2, y2, h, w;
        View last = null;
        COUNT++;
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
                x = 0;
                y = 0;
                break;
            case(2): // Text
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
                last = new Text(getApplicationContext(), x, y, s, 0);
                layout.addView(last);
                x = 0;
                y = 0;
                s = "";

                break;
            case(3):
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

                String S = "";
                for(int i = 9; i < code.length; ++i) {
                    S += code[i];
                }
                last = new Text(getApplicationContext(), x, y, S, 1);
                layout.addView(last);

                x = 0;
                y = 0;
                S = "";
                break;
            case(6): // Oval
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

                r = code[9] - 48;
                r *= 10;
                r += code[10] - 48;
                r *= 10;
                r += code[11] - 48;
                r *= 10;
                r += code[12] - 48;

                last = new Oval(getApplicationContext(), x, y, r, 0);
                layout.addView(last);

                x = 0;
                y = 0;
                r = 0;
                break;
            case(7): // Filled Oval
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

                r = code[9] - 48;
                r *= 10;
                r += code[10] - 48;
                r *= 10;
                r += code[11] - 48;
                r *= 10;
                r += code[12] - 48;

                last = new Oval(getApplicationContext(), x, y, r, 1);
                layout.addView(last);
                x = 0;
                y = 0;
                r = 0;
                break;
            case(8): // Rectangle
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

                h = code[9] - 48;
                h *= 10;
                h += code[10] - 48;
                h *=10;
                h += code[11] - 48;
                h *= 10;
                h += code[12] - 48;

                w = code[13] - 48;
                w *= 10;
                w += code[14] - 48;
                w *= 10;
                w += code[15] - 48;
                w *= 10;
                w += code[16] - 48;

                last = new Rectangle(getApplicationContext(), x, y, h, w, 0);
                layout.addView(last);

                x = 0;
                y = 0;
                h = 0;
                w = 0;
                break;
            case(9): // Filled Rectangle
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

                h = code[9] - 48;
                h *= 10;
                h += code[10] - 48;
                h *=10;
                h += code[11] - 48;
                h *= 10;
                h += code[12] - 48;

                w = code[13] - 48;
                w *= 10;
                w += code[14] - 48;
                w *= 10;
                w += code[15] - 48;
                w *= 10;
                w += code[16] - 48;

                last = new Rectangle(getApplicationContext(), x, y, h, w, 1);
                layout.addView(last);

                x = 0;
                y = 0;
                h = 0;
                w = 0;
                break;
            case(10): // Line
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

                x2 = code[9] - 48;
                x2 *= 10;
                x2 += code[10] - 48;
                x2 *=10;
                x2 += code[11] - 48;
                x2 *= 10;
                x2 += code[12] - 48;

                y2 = code[13] - 48;
                y2 *= 10;
                y2 += code[14] - 48;
                y2 *= 10;
                y2 += code[15] - 48;
                y2 *= 10;
                y2 += code[16] - 48;

                last = new Line(getApplicationContext(), x, y, x2, y2);
                layout.addView(last);

                x = 0;
                y = 0;
                x2 = 0;
                y2 = 0;
                break;
            case(11): // Change size
                x = code[1] - 48;
                x *= 10;
                x += code[2] - 48;
                SIZE = x;
                break;
            case(12): // Change color
                x = code[1] - 48;
                x *= 10;
                x += code[2] - 48;
                x *= 10;
                x += code[3] - 48;

                y = code[4] - 48;
                y *= 10;
                y += code[5] - 48;
                y *= 10;
                y += code[6] - 48;

                x2 = code[7] - 48;
                x2 *= 10;
                x2 += code[8] - 48;
                x2 *=10;
                x2 += code[9] - 48;

                R = x;
                G = y;
                B = x2;
                x = 0;
                y = 0;
                x2 = 0;
                break;
            case (13): // Undo
                layout.removeView(last);
                break;
            case (14): // Clear Screen
                layout.removeAllViews();
                break;

            case (15): // Button type 1
                break;

            case (16): // Button type 2
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

