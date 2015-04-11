package com.diana.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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

    static int r = 255, g = 255, b = 255;
    static int SIZE = 30, COUNT = 0;
    int X = 0, Y = 0, H = 0, W = 0, R = 0, LAYWIDTH = 0, yy = 0;
    char[] C = new char[100000];
    int index = 0;
    String nr = "";
    String text1 = "", text2 = "";

    Typeface temp;
    int lines;
    Display display;
    Point size;

    View view = null;

    TextView text, LAST;
    EditText edit1, edit2;
    static boolean LONG = false, BUTTON = false, BUTTONTYPE = false;
    Button[] buttons = new Button[40];
    int btnIndex = 0;
    static FrameLayout layout;
    RelativeLayout relativeLayout;


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
                    int type = (int)msg.obj;
                    if( LAYWIDTH == 0 && ((type > 1 && type < 10) || type == 22))
                        break;
                    switch(type){
                        case(-3): // Button Type
                            btnIndex = ConnectedThread.BYTE;
                            buttons[btnIndex].setX(ConnectedThread.X);
                            buttons[btnIndex].setY(ConnectedThread.Y);
                            buttons[btnIndex].setVisibility(View.VISIBLE);
                            BUTTONTYPE = false;
                            BUTTON = true;
                            break;
                        case(-2):// NewLine
                            if(BUTTON == true){
                                buttons[btnIndex].append("\n");
                            }
                            else {
                                if (LONG == true) {
                                    LAST.append("\n");
                                    C[index] = 13;
                                    index++;
                                }
                                else {
                                    yy = (int) LAST.getY();
                                    temp = LAST.getTypeface();
                                    lines = LAST.getLineCount();
                                    LAST = new TextView(getApplicationContext());
                                    LAST.setX(0);
                                    LAST.setY(yy + SIZE + 2);
                                    LAST.setTextSize(SIZE);
                                    LAST.setTextColor(Color.rgb(r, g, b));
                                    LAST.setSingleLine();
                                    LAST.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                                            LinearLayout.LayoutParams.WRAP_CONTENT));
                                    LAST.setTypeface(temp);
                                    layout.addView(LAST);
                                    view = LAST;
                                }
                            }
                            break;
                        case(-1): // Letter
                            char ch = (char) ConnectedThread.BYTE;
                            if(BUTTON == true){
                                buttons[btnIndex].append(ch + "");
                            }
                            else {
                                LAST.append(ch + "");
                                    if (LAST.getRight() + LAST.getX() >= LAYWIDTH - SIZE) {
                                        if (ch == ' ') break;
                                        yy = (int) LAST.getY();
                                        temp = LAST.getTypeface();
                                        LAST.setSingleLine();
                                        LAST = new TextView(getApplicationContext());
                                        LAST.setX(0);
                                        LAST.setY(yy + SIZE + 2);
                                        LAST.setTextSize(SIZE);
                                        LAST.setTextColor(Color.rgb(r, g, b));
                                        LAST.setTypeface(temp);
                                        LAST.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                                                LinearLayout.LayoutParams.WRAP_CONTENT));
                                        layout.addView(LAST);
                                        view = LAST;
                                    }
                            }
                            break;
                        case(1): // Set Screen
                            relativeLayout.removeAllViews();
                            layout = new FrameLayout(getApplicationContext());
                            layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.MATCH_PARENT));
                            layout.setBackgroundColor(Color.rgb(15, 15, 15));
                            layout.setVisibility(View.INVISIBLE);
                            X = ConnectedThread.X;
                            Y = ConnectedThread.Y;
                            H = ConnectedThread.H;
                            W = ConnectedThread.W;
                            layout.getLayoutParams().height = X;
                            layout.getLayoutParams().width = Y;

                            layout.setX(H);
                            layout.setY(W);
                            layout.setVisibility(View.VISIBLE);
                            relativeLayout.addView(layout);
                            buttonsInit();
                            LAYWIDTH = Y;
                            break;
                        case(2): // Text
                            if(LONG == true){
                                layout.removeView(LAST);
                                LONG = false;
                            }
                            BUTTON = false;
                            LAST = new TextView(getApplicationContext());
                            LAST.setX(ConnectedThread.X);
                            LAST.setY(ConnectedThread.Y);
                            LAST.setTextSize(SIZE);
                            LAST.setSingleLine();
                            LAST.setTextColor(Color.rgb(r, g, b));
                            LAST.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT));
                            layout.addView(LAST);
                            view = LAST;
                            break;
                        case(3): // Monospaced Text
                            if(LONG == true){
                                layout.removeView(LAST);
                                LONG = false;
                            }
                            BUTTON = false;
                            LAST = new TextView(getApplicationContext());
                            LAST.setX(ConnectedThread.X);
                            LAST.setY(ConnectedThread.Y);
                            LAST.setTextSize(SIZE);
                            LAST.setTypeface(Typeface.MONOSPACE);
                            LAST.setTextColor(Color.rgb(r, g, b));
                            LAST.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT));
                            layout.addView(LAST);
                            view = LAST;
                            break;
                        case(5): // Oval
                            if(LONG == true){
                                layout.removeView(LAST);
                                LONG = false;
                            }
                            X = ConnectedThread.X;
                            Y = ConnectedThread.Y;
                            R = ConnectedThread.R;
                            view = new Oval(getApplicationContext(), X, Y, R, 0);
                            layout.addView(view);
                            break;
                        case(6): // Filled Oval
                            if(LONG == true){
                                layout.removeView(LAST);
                                LONG = false;
                            }
                            X = ConnectedThread.X;
                            Y = ConnectedThread.Y;
                            R = ConnectedThread.R;
                            view = new Oval(getApplicationContext(), X, Y, R, 1);
                            layout.addView(view);
                            break;
                        case(7): // Rectangle
                            if(LONG == true){
                                layout.removeView(LAST);
                                LONG = false;
                            }
                            X = ConnectedThread.X;
                            Y = ConnectedThread.Y;
                            H = ConnectedThread.H;
                            W = ConnectedThread.W;
                            view = new Rectangle(getApplicationContext(), X, Y, W, H, 0);
                            layout.addView(view);
                            break;
                        case(8): // Filled Rectangle
                            if(LONG == true){
                                layout.removeView(LAST);
                                LONG = false;
                            }
                            X = ConnectedThread.X;
                            Y = ConnectedThread.Y;
                            H = ConnectedThread.H;
                            W = ConnectedThread.W;
                            view = new Rectangle(getApplicationContext(), X, Y, W, H, 1);
                            layout.addView(view);
                            break;
                        case(9): // Line
                            if(LONG == true){
                                layout.removeView(LAST);
                                LONG = false;
                            }
                            X = ConnectedThread.X;
                            Y = ConnectedThread.Y;
                            H = ConnectedThread.H;
                            W = ConnectedThread.W;
                            view = new Line(getApplicationContext(), X, Y, H, W);
                            layout.addView(view);
                            break;
                        case(10): // Font Size
                            SIZE = ConnectedThread.X;
                            break;
                        case(11): // Color
                            r = ConnectedThread.R;
                            g = ConnectedThread.X;
                            b = ConnectedThread.Y;
                            break;
                        case(12): // Undo
                            if(view != null) {
                                layout.removeView(view);
                            }
                            break;
                        case(14): // Clear Screen
                            layout.removeAllViews();
                            break;
                        case(15):
                            LONG = false;
                            BUTTONTYPE = true;
                            break;
                        case(16):
                            edit1.requestFocus();
                            edit1.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                }

                                @Override
                                public void onTextChanged(CharSequence s, int start, int before, int count) {

                                    mConnectedThread.write((edit1.getText().toString().charAt(edit1.getText().length() - 1) + "").getBytes());

                                }

                                @Override
                                public void afterTextChanged(Editable s) {
                                }
                            });
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                            break;
                        case(17):
                            edit2.requestFocus();
                            edit2.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                }

                                @Override
                                public void onTextChanged(CharSequence s, int start, int before, int count) {

                                    mConnectedThread.write((edit2.getText().toString().charAt(edit2.getText().length() - 1) + "").getBytes());

                                }

                                @Override
                                public void afterTextChanged(Editable s) {
                                }
                            });
                            InputMethodManager imm2 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm2.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                            break;
                        case(18):
                            nr = "";
                            nr = Integer.toString(ConnectedThread.NR);
                            for(int i = 0; i < nr.length(); ++i) {
                                ch = nr.charAt(i);
                                if(BUTTON == true){
                                    buttons[btnIndex].append(ch+"");
                                }
                                else {
                                    LAST.append(ch+"");
                                    if (LAST.getRight() + LAST.getX() >= LAYWIDTH - SIZE) {
                                        yy = (int) LAST.getY();
                                        temp = LAST.getTypeface();
                                        LAST.setSingleLine();
                                        LAST = new TextView(getApplicationContext());
                                        LAST.setX(0);
                                        LAST.setY(yy + SIZE + 2);
                                        LAST.setTextSize(SIZE);
                                        LAST.setTextColor(Color.rgb(r, g, b));
                                        LAST.setTypeface(temp);
                                        LAST.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                                                LinearLayout.LayoutParams.WRAP_CONTENT));
                                        layout.addView(LAST);
                                        view = LAST;
                                    }
                                }
                            }
                            break;
                        case(19):
                            nr = "";
                            nr = Integer.toString(ConnectedThread.NR);
                            for(int i = 0; i < nr.length(); ++i) {
                                ch = nr.charAt(i);
                                if(BUTTON == true){
                                    buttons[btnIndex].append(ch+"");
                                }
                                else {
                                    LAST.append(ch+"");
                                    if (LAST.getRight() + LAST.getX() >= LAYWIDTH - SIZE) {
                                        yy = (int) LAST.getY();
                                        temp = LAST.getTypeface();
                                        LAST.setSingleLine();
                                        LAST = new TextView(getApplicationContext());
                                        LAST.setX(0);
                                        LAST.setY(yy + SIZE + 2);
                                        LAST.setTextSize(SIZE);
                                        LAST.setTextColor(Color.rgb(r, g, b));
                                        LAST.setTypeface(temp);
                                        LAST.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                                                LinearLayout.LayoutParams.WRAP_CONTENT));
                                        layout.addView(LAST);
                                        view = LAST;
                                    }
                                }
                            }
                            break;
                        case(20):
                            nr = "";
                            nr = Integer.toString(ConnectedThread.NR);
                            for(int i = 0; i < nr.length(); ++i) {
                                ch = nr.charAt(i);
                                if(BUTTON == true){
                                    buttons[btnIndex].append(ch+"");
                                }
                                else {
                                    LAST.append(ch+"");
                                    if (LAST.getRight() + LAST.getX() >= LAYWIDTH - SIZE) {
                                        yy = (int) LAST.getY();
                                        temp = LAST.getTypeface();
                                        LAST.setSingleLine();
                                        LAST = new TextView(getApplicationContext());
                                        LAST.setX(0);
                                        LAST.setY(yy + SIZE + 2);
                                        LAST.setTextSize(SIZE);
                                        LAST.setTextColor(Color.rgb(r, g, b));
                                        LAST.setTypeface(temp);
                                        LAST.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                                                LinearLayout.LayoutParams.WRAP_CONTENT));
                                        layout.addView(LAST);
                                        view = LAST;
                                    }
                                }
                            }

                            break;
                        case(21):
                            nr = "";
                            nr = ConnectedThread.Bin;
                            for(int i = 0; i < nr.length(); ++i) {
                                ch = nr.charAt(i);
                                if(BUTTON == true){
                                    buttons[btnIndex].append(ch+"");
                                }
                                else {
                                    LAST.append(ch+"");
                                    if (LAST.getRight() + LAST.getX() >= LAYWIDTH - SIZE) {
                                        yy = (int) LAST.getY();
                                        temp = LAST.getTypeface();
                                        LAST.setSingleLine();
                                        LAST = new TextView(getApplicationContext());
                                        LAST.setX(0);
                                        LAST.setY(yy + SIZE + 2);
                                        LAST.setTextSize(SIZE);
                                        LAST.setTextColor(Color.rgb(r, g, b));
                                        LAST.setTypeface(temp);
                                        LAST.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                                                LinearLayout.LayoutParams.WRAP_CONTENT));
                                        layout.addView(LAST);
                                        view = LAST;
                                    }
                                }
                            }
                            break;
                        case(22): // Long Text
                            if(LONG == true){
                                layout.removeView(LAST);
                                LONG = false;
                            }
                            BUTTON = false;
                            LONG = true;
                            layout.removeAllViews();
                            LAST = new TextView(getApplicationContext());
                            LAST.setX(0);
                            LAST.setY(0);
                            LAST.setTextSize(SIZE);
                            LAST.setTypeface(Typeface.MONOSPACE);
                            LAST.setTextColor(Color.rgb(r, g, b));
                            LAST.setBackgroundColor(Color.rgb(15, 15, 15));
                            LAST.setMovementMethod(new ScrollingMovementMethod());
                            LAST.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT));
                            layout.addView(LAST);
                            view = LAST;
                            break;
                    }
                    break;

                case MESSAGE_WRITE:

                    break;

                case FAIL_CONNECT:
                    Toast.makeText(MessagesActivity.this, "Can't connect to device", Toast.LENGTH_LONG).show();
                    finish();
                    break;
            }
        }
    };

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
        text.setTextSize(15);
        text.setText("Connecting...");

        relativeLayout = (RelativeLayout) findViewById(com.diana.bluetooth.R.id.relativeLayout);

        display = getWindowManager().getDefaultDisplay();
        size = new Point();
        display.getSize(size);

        buttonsInit();

        edit1 = (EditText) findViewById(com.diana.bluetooth.R.id.editText);
        edit2 = (EditText) findViewById(com.diana.bluetooth.R.id.editText2);
        edit1.clearFocus();
        edit2.clearFocus();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

    }

    private void buttonsInit() {

        for(int i = 1; i < 32; ++i){
            buttons[i] = new Button(getApplicationContext());
            buttons[i].setVisibility(View.INVISIBLE);
            buttons[i].setBackground(getResources().getDrawable(com.diana.bluetooth.R.drawable.button));
            buttons[i].setTextColor(Color.BLACK);
            relativeLayout.addView(buttons[i]);
        }
        buttons[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mConnectedThread.write("1".getBytes());
            }
        });
        buttons[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mConnectedThread.write("2".getBytes());
            }
        });
        buttons[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mConnectedThread.write("3".getBytes());
            }
        });
        buttons[4].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mConnectedThread.write("4".getBytes());
            }
        });
        buttons[5].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mConnectedThread.write("5".getBytes());
            }
        });
        buttons[6].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mConnectedThread.write("6".getBytes());
            }
        });
        buttons[7].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mConnectedThread.write("7".getBytes());
            }
        });
        buttons[8].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mConnectedThread.write("8".getBytes());
            }
        });
        buttons[9].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mConnectedThread.write("9".getBytes());
            }
        });
        buttons[10].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mConnectedThread.write("10".getBytes());
            }
        });
        buttons[11].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mConnectedThread.write("11".getBytes());
            }
        });
        buttons[12].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mConnectedThread.write("12".getBytes());
            }
        });
        buttons[14].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mConnectedThread.write("14".getBytes());
            }
        });
        buttons[15].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mConnectedThread.write("15".getBytes());
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mConnectThread != null) {
            mConnectThread.cancel(getApplicationContext());
        }
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

