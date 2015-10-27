package nl.reupload.guube.airports;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

/**
 * Created by guube on 26/10/2015.
 */
public class AirportsDatabase extends SQLiteAssetHelper {

    private static final String DATABASE_NAME = "airports.db";
    private static final int DATABASE_VERSION = 1;

    public AirportsDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    // Hier de CRUD methoden
    public Cursor getAirports() {
        SQLiteDatabase db = getReadableDatabase();

//        String query = "SELECT latitude, longitude, iso_country, icao, name FROM airports WHERE iso_country = \"BE\" OR iso_country = \"NL\" ORDER BY name ASC";
        String query = "SELECT latitude, longitude, iso_country, icao, name FROM airports ORDER BY name ASC";
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        return c;
    }
}

