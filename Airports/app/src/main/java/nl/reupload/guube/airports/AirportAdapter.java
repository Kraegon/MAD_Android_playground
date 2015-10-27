package nl.reupload.guube.airports;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by guube on 26/10/2015.
 */
public class AirportAdapter  extends BaseAdapter {

    Context mContext;
    LayoutInflater mInflator;
    ArrayList mAirportArrayList;

    public AirportAdapter(Context context, LayoutInflater layoutInflater, ArrayList<Airport> airportArrayList)
    {
        mContext = context;
        mInflator = layoutInflater;
        mAirportArrayList = airportArrayList;
    }

    @Override
    public int getCount() {
        int size = mAirportArrayList.size();
        return size;
    }

    @Override
    public Object getItem(int position) {
        return mAirportArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;


        // Create new of gebruik een al bestaande (recycled by Android)
        if(convertView == null) {

            //
            convertView = mInflator.inflate(R.layout.airport_list_cell, null);

            //
            viewHolder = new ViewHolder();
            viewHolder.name = (TextView) convertView.findViewById(R.id.name);
            viewHolder.icao = (TextView) convertView.findViewById(R.id.icao);
            viewHolder.country = (TextView) convertView.findViewById(R.id.country);

            //
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // En nu de viewHolder invullen met de juiste persons
        Airport airport = (Airport) mAirportArrayList.get(position);

        viewHolder.name.setText(airport.name);
        viewHolder.icao.setText(airport.icao);
        viewHolder.country.setText(airport.country);

//        imageLoader.displayImage(person.imageURLThumbnail, viewHolder.imageView);

        return convertView;
    }

    // Holds all data to the view. Wordt evt. gerecycled door Android
    private static class ViewHolder {
        public TextView name;
        public TextView icao;
        public TextView country;
    }
}