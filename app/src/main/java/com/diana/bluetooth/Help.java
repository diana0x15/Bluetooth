package com.diana.bluetooth;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class Help extends Activity {

    ListView list;
    ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        // getActionBar().setDisplayHomeAsUpEnabled(true);

        list = (ListView) findViewById(R.id.listView2);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, 0);

        adapter.add("set screen" + "\n" + "1xxxxyyyy");
        adapter.add("2xxxxyyyyTEXT" + "\n" +"text (at location x,y)");
        adapter.add("3xxxxyyyyTEXT" + "\n" +"aligned text");
        adapter.add("4xx"+ "\n" +"tab xx columns");
        adapter.add("5" + "\n" +"new line");
        adapter.add("6xxxxyyyyrrrr"  + "\n" +"circle");
        adapter.add( "7xxxxyyyyrrrr" + "\n" +"filled circle");
        adapter.add("8xxxxyyyyhhhhwwww" + "\n" +"rectangle");
        adapter.add("9xxxxyyyyhhhhwwww" + "\n" +"filled rectangle");
        adapter.add("10xxxxyyyy" + "\n" + "line");
        adapter.add( "11xx" + "\n" +"font size");
        adapter.add( "12rrrgggbbb" + "\n" +"color (255;255;255 by default)");
        adapter.add("13" + "\n" +"undo");
        adapter.add( "14"  + "\n" +"clear screen");
        adapter.add("15xxxxyyyyTEXT"  + "\n" +"button (type 1)");
        adapter.add("16xxxxyyyyTEXT"  + "\n" +"button (type 2)");

        list.setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_help, menu);
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
