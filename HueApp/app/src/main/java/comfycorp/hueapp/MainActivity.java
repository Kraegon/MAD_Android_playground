package comfycorp.hueapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    BridgeMiddleMan bridgeMiddleMan = BridgeMiddleMan.getInstance();
    Boolean loaded = false;
    Boolean lampsVisible = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bridgeMiddleMan.mContext = this.getApplicationContext();
        setContentView(R.layout.activity_main);
        bridgeMiddleMan.connectToBridge();
        final ListView huelistview = (ListView) findViewById(R.id.hueListView);

        final HueArrayAdapter adapter = new HueArrayAdapter(getLayoutInflater());
        huelistview.setAdapter(adapter);

        bridgeMiddleMan.listeners.add(new BridgeMiddleMan.LightsChangedListener() {
            @Override
            public void onLightsChangedEvent() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });

        // Enable listener
        huelistview.setOnItemClickListener(this);
        loaded = true;
    }

    @Override
    protected void onResume () {
        super.onResume();
        if (loaded) {
            bridgeMiddleMan.getAllLamps(BridgeMiddleMan.ActionThreadType.THREAD_TYPE_ASYNC);
            bridgeMiddleMan.getAllLampGroups(BridgeMiddleMan.ActionThreadType.THREAD_TYPE_ASYNC);
        }
    }

    //
    // Click on selected item in list
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent i = new Intent(getApplicationContext(), HueDetailActivity.class);
        HueLight hueLight;
        if (lampsVisible)
            hueLight = bridgeMiddleMan.lightArray.get(position);
        else
            hueLight = bridgeMiddleMan.lightsGroupArray.get(position);

        i.putExtra("ID", hueLight.id);
        i.putExtra("ISON", hueLight.isOn);
        i.putExtra("NAME", hueLight.name);
        i.putExtra("HUE", hueLight.hue);
        i.putExtra("SATURATION", hueLight.saturation);
        i.putExtra("BRIGHTNESS", hueLight.brightness);
        i.putExtra("ISINDIVIDUALLIGHT", lampsVisible);
        startActivity(i);
    }

    public void onClick(View v) {
        final ListView huelistview = (ListView) findViewById(R.id.hueListView);
        final Button toggleButton = (Button) findViewById(R.id.buttonViewLightGroups);
        if (lampsVisible) {
            final HueGroupArrayAdapter adapter = new HueGroupArrayAdapter(getLayoutInflater());
            huelistview.setAdapter(adapter);
            toggleButton.setText("Show individual lights");
        } else {
            final HueArrayAdapter adapter = new HueArrayAdapter(getLayoutInflater());
            huelistview.setAdapter(adapter);
            toggleButton.setText("Show light groups");
        }
        lampsVisible = !lampsVisible;
    }

}
