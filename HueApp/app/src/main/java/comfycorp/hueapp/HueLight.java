package comfycorp.hueapp;

import android.graphics.Color;

/**
 * Model class
 *
 * Created by Kraegon on 27/10/2015.
 */
public class HueLight {
    public int id;
    public String name;
    public boolean isOn;
    public int hue;
    public int saturation;
    public int brightness;

    public int GetColor()
    {
        if (isOn) {
            float[] hsb = new float[3]; //We say brightness instead of value
            hsb[0] = (float) (hue / 65535.0);
            hsb[1] = (float) (saturation / 255.0);
            hsb[2] = (float) (brightness / 255.0);
            return Color.HSVToColor(hsb);
        } else{
            return Color.GRAY;
        }
    }

}
