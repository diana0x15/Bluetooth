package com.diana.bluetooth;

import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;

public class ConnectedThread extends Thread {

    private final BluetoothSocket mSocket;
    private final InputStream is;
    private final OutputStream os;
    BufferedReader r;

    protected static final int MESSAGE_READ = 1;
    protected static final int MESSAGE_WRITE = 2;

    Handler mHandler;
    Reader reader;

    public ConnectedThread(BluetoothSocket socket, Handler handler) {
        mSocket = socket;
        InputStream tmpIn = null;
        OutputStream tmpOut = null;

        // Get the input and output streams, using temp objects because
        // member streams are final
        try {
            tmpIn = socket.getInputStream();
            tmpOut = socket.getOutputStream();
        } catch (IOException e) { }

        is = tmpIn;
        os = tmpOut;
        mHandler = handler;
    }

    /*public void run() {
        int bytes;
        // Keep listening to the InputStream until an exception occurs
        while(true)  {
            try {
                bytes = is.read();
                mHandler.obtainMessage(MESSAGE_READ, bytes).sendToTarget();
                break;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }*/
    public void run() {
        int bytes = 0, i = 0;
        char ch[] = new char[100];
        // Keep listening to the InputStream until an exception occurs
        while(true)  {
            try {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                while(is.available() > 0) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    bytes = is.read();
                    ch[i] = (char) bytes;
                    i++;
                }
                if(bytes > 0) {
                    mHandler.obtainMessage(MESSAGE_READ, i, -1, ch).sendToTarget();
                    ch = new char[100];
                    i = 0;
                    bytes = 0;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /* Call this from the main activity to send data to the remote device */
    public void write(byte[] bytes){

        try {
            os.write(bytes);
           // mHandler.obtainMessage(MESSAGE_WRITE, -1, -1, bytes)
           //         .sendToTarget();
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
