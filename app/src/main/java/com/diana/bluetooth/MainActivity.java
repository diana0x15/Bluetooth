package com.diana.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;




public class MainActivity extends ActionBarActivity {


    private static final int REQUEST_ENABLE_BLUETOOTH = 3;

    public static String EXTRA_DEVICE_ADDRESS = "device_address";

    ConnectThread mConnectThread = null;

    BluetoothDevice selectedDevice = null;

    ListView mListView;
    ArrayAdapter<String> adapter;
    static BluetoothAdapter mBluetoothAdapter;
    static Set<BluetoothDevice> mPairedDevices; // All paired devices
    ArrayList<BluetoothDevice> devices; // Found devices that are paired


    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When discovery finds a device
            if(BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                String S = device.getName();

                for(BluetoothDevice d : mPairedDevices) {
                    if(d.getName().equals(device.getName())) {
                        S += " (Paired)";
                        devices.add(device);
                        break;
                    }
                }
                S += "\n" + device.getAddress();
                adapter.add(S);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Init();
        mBluetoothAdapter.startDiscovery();

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mBluetoothAdapter.cancelDiscovery();

                for(BluetoothDevice d : devices) {
                    if (adapter.getItem(position).equals(d.getName() + "\n" + d.getAddress()) ||
                            adapter.getItem(position).equals(d.getName() + " (Paired)\n" + d.getAddress())) {
                        selectedDevice = d;
                        break;
                    }
                }

                if(selectedDevice == null) {
                    Toast.makeText(getApplicationContext(), "Device is not paired!", Toast.LENGTH_LONG).show();
                }
                else {
                    // Get the device MAC address, which is the last 17 chars in the View
                    String info = ((TextView) view).getText().toString();
                    String address = info.substring(info.length() - 17);

                    // Create the result Intent and include the MAC address
                    Intent intent = new Intent(MainActivity.this, MessagesActivity.class);
                    intent.putExtra(EXTRA_DEVICE_ADDRESS, address);
                    startActivity(intent);
                }
            }
        });
    }



    private void Init() {
        mListView = (ListView) findViewById(R.id.listView);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, 0);
        mListView.setAdapter(adapter);
        devices = new ArrayList<BluetoothDevice>();

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(!mBluetoothAdapter.isEnabled()) {
            Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBluetooth, REQUEST_ENABLE_BLUETOOTH);
        }
        mPairedDevices = mBluetoothAdapter.getBondedDevices();

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(receiver, filter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case REQUEST_ENABLE_BLUETOOTH:
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_CANCELED) {
                    // User did not enable Bluetooth or an error occurred
                    finish();
                }
                break;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
