package comfycorp.hueapp;

import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

/**
 * Created by Kraegon on 27/10/2015.
 */
public class HueArrayAdapter extends BaseAdapter {
    LayoutInflater mInflator;
    BridgeMiddleMan bridgeMiddleMan = BridgeMiddleMan.getInstance();

    public HueArrayAdapter(LayoutInflater layoutInflater) {
        this.mInflator = layoutInflater;
    }

    @Override
    public int getCount() {
        return BridgeMiddleMan.getInstance().lightArray.size();
    }

    @Override
    public Object getItem(int position) {
        return BridgeMiddleMan.getInstance().lightArray.get(position);
    }

    @Override
    public long getItemId(int position) {
        return BridgeMiddleMan.getInstance().lightArray.get(position).id;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        final HueArrayAdapter adapter = this;

        // Create new of gebruik een al bestaande (recycled by Android)
        if (convertView == null) {
            convertView = mInflator.inflate(R.layout.hue_list_cell, null);

            viewHolder = new ViewHolder();
            viewHolder.name = (TextView) convertView.findViewById(R.id.HueNameView);
            viewHolder.idView = (TextView) convertView.findViewById(R.id.IDView);
            viewHolder.isOn = (Switch) convertView.findViewById(R.id.quickOnOffSwitch);
            viewHolder.colorDisplay = (SurfaceView) convertView.findViewById(R.id.surfaceView);

            convertView.setTag(viewHolder);

            //Preferably I would not handle this here, but due to access restrictions I have t
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.isOn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                bridgeMiddleMan.lightArray.get(position).isOn = isChecked;
                adapter.notifyDataSetChanged(); //HAHA
                bridgeMiddleMan.setLampState(bridgeMiddleMan.lightArray.get(position));
            }
        });

        // En nu de viewHolder invullen met de juiste persons
        HueLight cellHue = bridgeMiddleMan.lightArray.get(position);
        viewHolder.name.setText(cellHue.name);
        viewHolder.idView.setText(Integer.toString(cellHue.id));
        viewHolder.isOn.setChecked(cellHue.isOn);
        viewHolder.colorDisplay.setBackgroundColor(cellHue.GetColor());

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

