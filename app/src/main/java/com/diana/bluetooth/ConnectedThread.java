package com.diana.bluetooth;

import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ConnectedThread extends Thread {

    private final BluetoothSocket mSocket;
    private final InputStream is;
    private final OutputStream os;

    protected static final int MESSAGE_READ = 1;
    protected static final int MESSAGE_WRITE = 2;

    Handler mHandler;

    static int X = 0, Y = 0, H = 0, W = 0, R = 0, BYTE, NR;
    static String Bin = "";

    public ConnectedThread(BluetoothSocket socket, Handler handler) {
        mSocket = socket;
        InputStream tmpIn = null;
        OutputStream tmpOut = null;
        try {
            tmpIn = socket.getInputStream();
            tmpOut = socket.getOutputStream();
        } catch (IOException e) { }

        is = tmpIn;
        os = tmpOut;
        mHandler = handler;
    }

    public void run() {
        int bytes = 0;

        while (true) {

            bytes = 0;
            try {
                if(is.available() > 0){// && BYTE == 0) {
                 try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                    bytes = is.read();

                        if (MessagesActivity.BUTTONTYPE == true) {
                            if (bytes < 32) {
                                BYTE = bytes;
                                mHandler.obtainMessage(MESSAGE_READ, -3).sendToTarget();
                            }
                        } else {
                            if (bytes < 32 && bytes != 13 && bytes != 4) {
                                shape(bytes);
                            }

                            if (bytes >= 32) {
                                BYTE = bytes;
                                mHandler.obtainMessage(MESSAGE_READ, -1).sendToTarget();
                            }
                            if (bytes == 13) {
                                BYTE = 13;
                                mHandler.obtainMessage(MESSAGE_READ, -2).sendToTarget();
                            }
                            if (bytes == 4) {
                                try {
                                    Thread.sleep(50);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                int count = is.read();
                                count *= 256;

                                while (count > 0) {
                                    BYTE = ' ';
                                    mHandler.obtainMessage(MESSAGE_READ, -1).sendToTarget();
                                    count--;
                                }
                            }

                        }

                }

            } catch (IOException e) {
                break;
            }
        }
    }
    public void shape(int type){
        int x = 0, y = 0, h = 0, w = 0, r = 0;
        if(type == 1) { //  Set Screen
            for (int i = 1; i <= 2; ++i) {
                try {
                    x += is.read();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(i < 2)
                x *= 256;
            }

            X = x;
            for (int i = 1; i <= 2; ++i) {
                try {
                    y += is.read();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(i < 2)
                    y *= 256;
            }
            Y = y;
            for (int i = 1; i <= 2; ++i) {
                try {
                    h += is.read();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(i < 2)
                    h *= 256;
            }
            H = h;
            for (int i = 1; i <= 2; ++i) {
                try {
                    w += is.read();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(i < 2)
                    w *= 256;
            }
            W = w;
        }
        if(type == 2 || type == 3){ // Text & Monospaced Text
            for (int i = 1; i <= 2; ++i) {
                try {
                    x += is.read();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(i < 2)
                    x *= 256;
            }
            X = x;
            for (int i = 1; i <= 2; ++i) {
                try {
                    y += is.read();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(i < 2)
                    y *= 256;
            }
            Y = y;
        }
        if(type == 5 || type == 6) { // Circle & Filled Circle
            for (int i = 1; i <= 2; ++i) {
                try {
                    x += is.read();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(i < 2)
                x *= 256;
            }
            X = x ;
            for (int i = 1; i <= 2; ++i) {
                try {
                    y += is.read();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(i < 2)
                y *= 256;
            }
            Y = y;
            for (int i = 1; i <= 2; ++i) {
                try {
                    r += is.read();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(i < 2)
                r *= 256;
            }
            R = r;
        }
        if(type == 7 || type == 8) { // Rectangle & Filled Rectangle
            for (int i = 1; i <= 2; ++i) {
                try {
                    x += is.read();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(i < 2)
                x *= 256;
            }
            X = x;
            for (int i = 1; i <= 2; ++i) {
                try {
                    y += is.read();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(i < 2)
                y *= 256;
            }
            Y = y;
            for (int i = 1; i <= 2; ++i) {
                try {
                    h += is.read();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(i < 2)
                h *= 256;
            }
            H = h;
            for (int i = 1; i <= 2; ++i) {
                try {
                    w += is.read() ;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(i < 2)
                w *= 256;
            }
            W = w;
        }
        if(type == 9){ // Line
            for (int i = 1; i <= 2; ++i) {
                try {
                    x += is.read();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(i < 2)
                x *= 256;
            }
            X = x;
            for (int i = 1; i <= 2; ++i) {
                try {
                    y += is.read();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(i < 2)
                y *= 256;
            }
            Y = y;
            for (int i = 1; i <= 2; ++i) {
                try {
                    h += is.read();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(i < 2)
                h *= 256;
            }
            H = h;
            for (int i = 1; i <= 2; ++i) {
                try {
                    w += is.read();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(i < 2)
                w *= 256;
            }
            W = w;
        }
        if(type == 10){ // Font Size
            for(int i = 1; i <= 1; ++i) {
                try {
                    x += is.read();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            X = x;
        }
        if(type == 11){ // Color
            for(int i = 1; i <= 1; ++i) {
                try {
                    r += is.read();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            R = r;
            for(int i = 1; i <= 1; ++i) {
                try {
                    x += is.read();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            X = x;
            for(int i = 1; i <= 1; ++i) {
                try {
                    y += is.read();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            Y = y;
        }
        if(type == 15) { // Button
            for (int i = 1; i <= 2; ++i) {
                try {
                    x += is.read();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(i < 2)
                x *= 256;
            }
            X = x;
            for (int i = 1; i <= 2; ++i) {
                try {
                    y += is.read();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(i < 2)
                y *= 256;
            }
            Y = y;
        }
        if(type == 18) { // 1 byte number
            NR = 0;

            try {
                NR += is.read();
            } catch (IOException e) {
                e.printStackTrace();
            }
            NR *= 256;
        }

        if(type == 19) { // 2 bytes number
            NR = 0;
            for (int i = 1; i <= 2; ++i) {
                try {
                    NR += is.read();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(i < 2)
                    NR *= 256;
            }
        }
        if(type == 20) { // 4 bytes number
            NR = 0;
            for (int i = 1; i <= 4; ++i) {
                try {
                    NR += is.read();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(i < 4)
                    NR *= 256;
            }

        }
        if(type == 21) { // binary
            NR = 0;
            Bin = "";
            try {
                NR += is.read();
            } catch (IOException e) {
                e.printStackTrace();
            }
            NR *= 256;

            Bin = toBinary(NR);
        }
        mHandler.obtainMessage(MESSAGE_READ, type).sendToTarget();

    }

    private String toBinary(int nr) {
        int r = 0;
        if(nr == 0){
            return "0";
        }
        String bin = "";
        while(nr > 0){
            r = nr % 2;
            if(r == 0)
                bin = "0" + bin;
            if(r == 1)
                bin = "1" + bin;
            nr /= 2;
        }
        return bin;
    }

    /* Call this from the main activity to send data to the remote device */
    public void write(byte[] bytes){

        try {
            os.write(bytes);
            mHandler.obtainMessage(MESSAGE_WRITE, -1, -1, bytes).sendToTarget();
        } catch (IOException e) { }

    }

    /* Call this from the main activity to shutdown the connection */
    public void cancel(Context context) {
        try {
            mSocket.close();
            Toast.makeText(context, "DISCONNECT", Toast.LENGTH_LONG).show();
        } catch (IOException e) { }
    }
}
