package comfycorp.hueapp;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.SurfaceView;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 *
 *
 * Created by Kraegon on 27/10/2015.
 */
public class HueDetailActivity  extends FragmentActivity implements SeekBar.OnSeekBarChangeListener {

    private HueLight selectedHueLight = new HueLight();
    BridgeMiddleMan bridgeMiddleMan = BridgeMiddleMan.getInstance();

    TextView labelName;
    TextView labelId;
    SeekBar seekBarHue;
    SeekBar seekBarSaturation;
    SeekBar seekBarBrightness;
    SurfaceView colorDisplay;

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

        labelName = (TextView) findViewById(R.id.textViewName);
        labelId = (TextView) findViewById(R.id.textViewId);
        seekBarHue = (SeekBar) findViewById(R.id.seekBarHue);
        seekBarSaturation = (SeekBar) findViewById(R.id.seekBarSat);
        seekBarBrightness = (SeekBar) findViewById(R.id.seekBarBright);
        colorDisplay = (SurfaceView) findViewById(R.id.surfaceViewColor);

        //Set seekbar listeners
        seekBarBrightness.setOnSeekBarChangeListener(this);
        seekBarSaturation.setOnSeekBarChangeListener(this);
        seekBarHue.setOnSeekBarChangeListener(this);

        System.out.println("Hue" + selectedHueLight.hue);
        System.out.println("Sat" + selectedHueLight.saturation);
        System.out.println("Bri" + selectedHueLight.brightness);

        updateView();
    }

    private void updateView() {

        labelName.setText(selectedHueLight.name);
        labelId.setText(Integer.toString(selectedHueLight.id));
        seekBarHue.setProgress(selectedHueLight.hue);
        seekBarSaturation.setProgress(selectedHueLight.saturation);
        seekBarBrightness.setProgress(selectedHueLight.brightness);
        colorDisplay.setBackgroundColor(selectedHueLight.GetColor());

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
            selectedHueLight.brightness = seekBarBrightness.getProgress();
            selectedHueLight.saturation = seekBarSaturation.getProgress();
            selectedHueLight.hue = seekBarHue.getProgress();
            updateView();
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        bridgeMiddleMan.setLampState(selectedHueLight);
    }
}
