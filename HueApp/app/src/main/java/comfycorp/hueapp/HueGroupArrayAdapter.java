package comfycorp.hueapp;

import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

/**
 * Created by Guube on 29/10/2015.
 */
public class HueGroupArrayAdapter extends BaseAdapter {
    LayoutInflater mInflator;
    BridgeMiddleMan bridgeMiddleMan = BridgeMiddleMan.getInstance();

    public HueGroupArrayAdapter(LayoutInflater layoutInflater) {
        this.mInflator = layoutInflater;
    }

    @Override
    public int getCount() {
        return BridgeMiddleMan.getInstance().lightsGroupArray.size();
    }

    @Override
    public Object getItem(int position) {
        return BridgeMiddleMan.getInstance().lightsGroupArray.get(position);
    }

    @Override
    public long getItemId(int position) {
        return BridgeMiddleMan.getInstance().lightsGroupArray.get(position).id;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        final HueGroupArrayAdapter adapter = this;

        // Create new of gebruik een al bestaande (recycled by Android)
        if (convertView == null) {
            convertView = mInflator.inflate(R.layout.hue_list_cell, null);

            viewHolder = new ViewHolder();
            viewHolder.name = (TextView) convertView.findViewById(R.id.HueNameView);
            viewHolder.idView = (TextView) convertView.findViewById(R.id.IDView);
            viewHolder.isOn = (Switch) convertView.findViewById(R.id.quickOnOffSwitch);
            viewHolder.colorDisplay = (ImageView) convertView.findViewById(R.id.imageViewColour);

            convertView.setTag(viewHolder);

            //Preferably I would not handle this here, but due to access restrictions I have t
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.isOn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                bridgeMiddleMan.lightsGroupArray.get(position).isOn = isChecked;
                adapter.notifyDataSetChanged(); //HAHA
                bridgeMiddleMan.setLampsGroupState(bridgeMiddleMan.lightsGroupArray.get(position));
            }
        });

        // En nu de viewHolder invullen met de juiste persons
        HueLight cellHue = bridgeMiddleMan.lightsGroupArray.get(position);
        viewHolder.name.setText(cellHue.name);
        viewHolder.idView.setText(Integer.toString(cellHue.id));
        viewHolder.isOn.setChecked(cellHue.isOn);
        viewHolder.colorDisplay.setColorFilter(cellHue.GetColor());

        return convertView;
    }

    // Holds all data to the view. Wordt evt. gerecycled door Android
    private static class ViewHolder {
        public TextView name;
        public TextView idView;
        public Switch isOn;
        public ImageView colorDisplay; //COLOUR
    }
}

