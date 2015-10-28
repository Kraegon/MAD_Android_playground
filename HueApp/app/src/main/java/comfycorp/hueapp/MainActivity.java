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

public class MainActivity extends AppCompatActivity {
    BridgeMiddleMan bridgeMiddleMan = BridgeMiddleMan.getInstance();

//    public static ArrayList<HueLight> hueDebugList; //TODO: delete me
//
//    private void populateHueDebugList(){ //TODO: delete me
//        HueLight h1 = new HueLight();
//        h1.id = 0;
//        h1.name = "Light A";
//        h1.isOn = true;
//        h1.hue = 65535;
//        h1.saturation = 255;
//        h1.brightness = 255;
//
//        HueLight h2 = new HueLight();
//        h2.id = 1;
//        h2.name = "Light B";
//        h2.isOn = false;
//        h2.hue = 1235;
//        h2.saturation = 24;
//        h2.brightness = 124;
//
//        HueLight h3 = new HueLight();
//        h3.id = 2;
//        h3.name = "Light C";
//        h3.isOn = false;
//        h3.hue = 35535;
//        h3.saturation = 125;
//        h3.brightness = 232;
//
//        HueLight h4 = new HueLight();
//        h4.id = 3;
//        h4.name = "Light D";
//        h4.isOn = true;
//        h4.saturation = 195;
//        h4.brightness = 255;
//        hueDebugList = new ArrayList<HueLight>();
//        hueDebugList.add(h1);
//        hueDebugList.add(h2);
//        hueDebugList.add(h3);
//        hueDebugList.add(h4);
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bridgeMiddleMan.mContext = this.getApplicationContext();
        setContentView(R.layout.activity_main);

        //populateHueDebugList();
        BridgeMiddleMan.getInstance().getAllLamps();

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

        huelistview.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                final String item = (String) parent.getItemAtPosition(position);
                view.animate().setDuration(2000).alpha(0).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                        view.setAlpha(1);
                    }
                });
            }
        });
    }
}
