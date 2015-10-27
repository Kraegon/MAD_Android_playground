package comfycorp.hueapp;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Kraegon on 27/10/2015.
 */
public class HueArrayAdapter extends BaseAdapter{
    LayoutInflater mInflator;

    public HueArrayAdapter(LayoutInflater layoutInflater) {
        this.mInflator = layoutInflater;
    }

    @Override
    public int getCount() {
        return MainActivity.hueDebugList.size(); //TODO: Change to true
    }

    @Override
    public Object getItem(int position) {
        return MainActivity.hueDebugList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return MainActivity.hueDebugList.get(position).id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        // Create new of gebruik een al bestaande (recycled by Android)
        if(convertView == null) {
            convertView = mInflator.inflate(R.layout.hue_list_cell, null);

            viewHolder = new ViewHolder();
            viewHolder.name = (TextView) convertView.findViewById(R.id.HueNameView);
            viewHolder.idView = (TextView) convertView.findViewById(R.id.IDView);
            viewHolder.isOn = (Switch) convertView.findViewById(R.id.quickOnOffSwitch);
            viewHolder.colorDisplay = (SurfaceView) convertView.findViewById(R.id.surfaceView);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // En nu de viewHolder invullen met de juiste persons
        HueLight huelight = MainActivity.hueDebugList.get(position);

        viewHolder.name.setText(huelight.name);
        viewHolder.idView.setText(Integer.toString(huelight.id));
        viewHolder.isOn.setChecked(huelight.isOn);
        viewHolder.colorDisplay.setBackgroundColor(huelight.GetColor());

//        imageLoader.displayImage(person.imageURLThumbnail, viewHolder.imageView);

        return convertView;
    }

    // Holds all data to the view. Wordt evt. gerecycled door Android
    private static class ViewHolder {
        public TextView name;
        public TextView idView;
        public Switch isOn;
        public SurfaceView colorDisplay; //COLOUR
    }
}

