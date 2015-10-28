package comfycorp.hueapp;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{
    BridgeMiddleMan bridgeMiddleMan = BridgeMiddleMan.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bridgeMiddleMan.mContext = this.getApplicationContext();
        setContentView(R.layout.activity_main);

        bridgeMiddleMan.getAllLamps();

        final Button button = (Button) findViewById(R.id.navTestButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent maliciousIntent = new Intent(getApplicationContext(), HueDetailActivity.class);
                startActivity(maliciousIntent);
            }
        });


        final ListView huelistview = (ListView) findViewById(R.id.hueListView);

        final HueArrayAdapter adapter = new HueArrayAdapter(getLayoutInflater());
        huelistview.setAdapter(adapter);

        bridgeMiddleMan.listeners.add(new BridgeMiddleMan.LightsChangedListener() {
            @Override
            public void onLightsChangedEvent() {
                adapter.notifyDataSetChanged();
            }
        });

        // Enable listener
        huelistview.setOnItemClickListener(this);

    }

    //
    // Click on selected item in list
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent i = new Intent(getApplicationContext(), HueDetailActivity.class);
        System.out.println("onItemClick()");
        startActivity(i);
    }

}
