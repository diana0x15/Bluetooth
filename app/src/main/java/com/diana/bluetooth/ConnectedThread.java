package com.diana.bluetooth;

import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ConnectedThread extends Thread {

    private final BluetoothSocket mmSocket;
    private final InputStream mmInStream;
    private final OutputStream mmOutStream;

    protected static final int MESSAGE_READ = 1;
    protected static final int MESSAGE_WRITE = 2;

    Handler mHandler;

    public ConnectedThread(BluetoothSocket socket, Handler handler) {
        mmSocket = socket;
        InputStream tmpIn = null;
        OutputStream tmpOut = null;

        // Get the input and output streams, using temp objects because
        // member streams are final
        try {
            tmpIn = socket.getInputStream();
            tmpOut = socket.getOutputStream();
        } catch (IOException e) { }

        mmInStream = tmpIn;
        mmOutStream = tmpOut;
        mHandler = handler;
    }

    public void run() {

        byte[] buffer = new byte[1024];
        int bytes; // bytes returned from read()

        // Keep listening to the InputStream until an exception occurs
        while (true){
            try {
                // Read from the InputStream
                bytes = mmInStream.read(buffer);

                // Reading finished
                mHandler.obtainMessage(MESSAGE_READ, bytes, 0, buffer).sendToTarget();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    /* Call this from the main activity to send data to the remote device */
    public void write(byte[] bytes){

        try {
            mmOutStream.write(bytes);
           // mHandler.obtainMessage(MESSAGE_WRITE, -1, -1, bytes)
           //         .sendToTarget();
        } catch (IOException e) { }

    }

    /* Call this from the main activity to shutdown the connection */
    public void cancel(Context context) {
        try {
            mmSocket.close();
            Toast.makeText(context, "DISCONNECT", Toast.LENGTH_LONG).show();
        } catch (IOException e) { }
    }
}
