package nl.reupload.guube.contactcardapp;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by guube on 10/19/2015.
 */
public class RandomUserTask extends AsyncTask<String, Void, String> {

    // Call back
    private OnRandomUserAvailable listener = null;

    // Static's
    private static final String TAG = "RandomUserTask";
    private static final String urlString = "https://randomuser.me/api/";

    // Constructor, set listener
    public RandomUserTask(OnRandomUserAvailable listener) {
        this.listener = listener;
    }

    @Override
    protected String doInBackground(String... params) {

        InputStream inputStream = null;
        int responsCode = -1;

        String response = "";

        for(String url : params) {
            Log.i(TAG, url);
        }

        try {
            URL url = new URL(urlString);
            URLConnection urlConnection = url.openConnection();

            if (!(urlConnection instanceof HttpURLConnection)) {
                // Url
                return null;
            }

            HttpURLConnection httpConnection = (HttpURLConnection) urlConnection;
            httpConnection.setAllowUserInteraction(false);
            httpConnection.setInstanceFollowRedirects(true);
            httpConnection.setRequestMethod("GET");
            httpConnection.connect();

            responsCode = httpConnection.getResponseCode();

            if (responsCode == HttpURLConnection.HTTP_OK) {
                inputStream = httpConnection.getInputStream();
                response = getStringFromInputStream(inputStream);
                //Log.i(TAG, response);
            }
        } catch (MalformedURLException e) {
            Log.e("TAG", e.getLocalizedMessage());
            return null;
        } catch (IOException e) {
            Log.e("TAG", e.getLocalizedMessage());
            return null;
        }

        return response;
    }


    protected void onProgressUpdate(Integer... progress) {
        Log.i(TAG, progress.toString());
    }

    protected void onPostExecute(String response) {

        //Log.i(TAG, response);

        // parse JSON and inform caller
        JSONObject jsonObject;

        try {
            // Top level json object
            jsonObject = new JSONObject(response);

            // Get all users and start looping
            JSONArray users = jsonObject.getJSONArray("results");
            for(int idx = 0; idx < users.length(); idx++) {
                // array level objects and get user
                JSONObject array = users.getJSONObject(idx);
                JSONObject user = array.getJSONObject("user");

                // Get title, first and last name
                JSONObject name = user.getJSONObject("name");
                String firstName = name.getString("first");
                String surname = name.getString("last");

                // Capitalise first character of the names

                firstName = capitaliseFirstCharacter(firstName);
                surname = smartCapitaliseSurname(surname);
                // Get email address

                String email = user.getString("email");

                // Get nationality

                String nationality = jsonObject.getString("nationality");
                System.out.println(nationality);

                // Get image url
                JSONObject picture = user.getJSONObject("picture");
                String imageurlhigh = picture.getString("large");
                String imageurlthumbnail = picture.getString("thumbnail");


                // Create new Person object
                Person p = new Person();
                p.firstName = firstName;
                p.surname = surname;
                p.email = email;
                p.imageURLHigh = imageurlhigh;
                p.imageURLThumbnail = imageurlthumbnail;
                p.nationality = nationality;

                // call back with new person data
                listener.onRandomUserAvailable(p);

            }
        } catch( JSONException ex) {
            Log.e(TAG, ex.getLocalizedMessage());
        }
    }


    //
    // convert InputStream to String
    //
    private static String getStringFromInputStream(InputStream is) {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {

            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sb.toString();
    }

    // Call back interface
    public interface OnRandomUserAvailable {
        void onRandomUserAvailable(Person person);
    }

    public String capitaliseFirstCharacter(String inputString) {
        char[] charArray = inputString.toCharArray();
        charArray[0] = (char) (charArray[0] - 32);
        return String.copyValueOf(charArray);
    }

    public String smartCapitaliseSurname(String inputString) {
        char[] charArray = inputString.toCharArray();
        int spaceIndex = 0;
        if (inputString.contains(" ")) {
            for (int i = charArray.length; i > 0; i--) {
                if (charArray[i] == 32) spaceIndex = i;
                break;
            }
        spaceIndex++;
        }
        charArray[spaceIndex] = (char) (charArray[spaceIndex] - 32);
        return String.copyValueOf(charArray);
    }
}