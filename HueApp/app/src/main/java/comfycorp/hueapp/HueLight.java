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

    public int GetColor() {
        if (isOn) {
            float[] hsb = new float[3]; //We say brightness instead of value
            hsb[0] = (hue / 65535.0f) * 360f;
            hsb[1] = saturation / 255.0f;
            hsb[2] = brightness / 255.0f;
            System.out.println("Colour in RGB: " + Color.HSVToColor(hsb));
            return Color.HSVToColor(hsb);
        } else {
            return Color.GRAY;
        }
    }

}
