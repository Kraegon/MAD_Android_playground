package comfycorp.hueapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    BridgeMiddleMan bridgeMiddleMan = BridgeMiddleMan.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bridgeMiddleMan.mContext = this.getApplicationContext();
        setContentView(R.layout.activity_main);

        final Button button = (Button) findViewById(R.id.navTestButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent maliciousIntent = new Intent(getApplicationContext(), HueDetailActivity.class);
                startActivity(maliciousIntent);
            }
        });

    }


}
