package nl.reupload.guube.airports;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;


/**
 * Created by guube on 26/10/2015.
 */
public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private final static String TAG = "MainActivity";

    private ListView airportListView;
    AirportAdapter mAirportAdapter;
    ArrayList list = new ArrayList<Airport>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAirportAdapter = new AirportAdapter(this,
                getLayoutInflater(),
                list);

        // Inflate listview
        airportListView = (ListView) findViewById(R.id.airportList);

        // Init database and query
        AirportsDatabase adb = new AirportsDatabase(this);
        Cursor cursor = adb.getAirports();

        cursor.moveToFirst();
        while( cursor.moveToNext() ) {
            Airport airport = new Airport();
            airport.icao = cursor.getString(cursor.getColumnIndex("icao"));
            airport.name = cursor.getString(cursor.getColumnIndex("name"));
            airport.country = cursor.getString(cursor.getColumnIndex("iso_country"));
            airport.latitude = cursor.getDouble(cursor.getColumnIndex("latitude"));
            airport.longitude = cursor.getDouble(cursor.getColumnIndex("longitude"));
            list.add(airport);
        }
        Log.i(TAG, "count: " + list.size());


//        ArrayAdapter adapter = new ArrayAdapter<String>(getApplicationContext(),
//                android.R.layout.simple_list_item_1,
//                list);
        airportListView.setAdapter(mAirportAdapter);

        mAirportAdapter.notifyDataSetChanged();

        airportListView.setOnItemClickListener(this);
    }

    //
    // Click on selected item in list
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent i = new Intent(getApplicationContext(), MapsActivity.class);
        Airport airport = (Airport) mAirportAdapter.getItem(position);
        i.putExtra("AIRPORTNAME", airport.name);
        i.putExtra("AIRPORTLATITUDE", airport.latitude);
        i.putExtra("AIRPORTLONGITUDE", airport.longitude);
        startActivity(i);
    }



}