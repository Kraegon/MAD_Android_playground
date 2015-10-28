package comfycorp.hueapp;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.SurfaceView;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 *
 *
 * Created by Kraegon on 27/10/2015.
 */
public class HueDetailActivity  extends FragmentActivity {

    private HueLight selectedHueLight = new HueLight();

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hue_details);

        Bundle extras = getIntent().getExtras();

        selectedHueLight.name = extras.getString("NAME");
        selectedHueLight.id = extras.getInt("ID");
        selectedHueLight.hue = extras.getInt("HUE");
        selectedHueLight.saturation = extras.getInt("SATURATION");
        selectedHueLight.brightness = extras.getInt("BRIGHTNESS");
        selectedHueLight.isOn = extras.getBoolean("ISON");

        updateView();
    }

    private void updateView() {

        TextView labelName = (TextView) findViewById(R.id.textViewName);
        TextView labelId = (TextView) findViewById(R.id.textViewId);
        SeekBar seekBarHue = (SeekBar) findViewById(R.id.seekBarHue);
        SeekBar seekBarSaturation = (SeekBar) findViewById(R.id.seekBarSat);
        SeekBar seekBarBrightness = (SeekBar) findViewById(R.id.seekBarBright);
        SurfaceView colorDisplay = (SurfaceView) findViewById(R.id.surfaceViewColor);

        labelName.setText(selectedHueLight.name);
        labelId.setText(Integer.toString(selectedHueLight.id));
        seekBarHue.setProgress(selectedHueLight.hue);
        seekBarSaturation.setProgress(selectedHueLight.saturation);
        seekBarBrightness.setProgress(selectedHueLight.brightness);
        colorDisplay.setBackgroundColor(selectedHueLight.GetColor());

    }
}
