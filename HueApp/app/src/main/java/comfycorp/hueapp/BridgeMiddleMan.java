package comfycorp.hueapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Looper;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import cz.msebera.android.httpclient.*;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;

/**
 * Created by Kraegon on 27/10/2015.
 */
public class BridgeMiddleMan {
    //Used for shared preference
    private static final String PREFS_NAME = "comfycorp.hueapp.HueBridgePreferences";

    private static BridgeMiddleMan ourInstance = new BridgeMiddleMan();
    public List<LightsChangedListener> listeners = new ArrayList<LightsChangedListener>();

    //Event interface & event
    interface LightsChangedListener {
        void onLightsChangedEvent();
    }

    private void fireLightsChangedEvent() {
        // Notify everybody that may be interested
        for (LightsChangedListener l : listeners)
            l.onLightsChangedEvent();
    }

    private enum ActionThreadType{
        THREAD_TYPE_SYNC,
        THREAD_TYPE_ASYNC
    }

    public static BridgeMiddleMan getInstance() {
        return ourInstance;
    }

    public Context mContext;
    public String username;// = "1f4d16f21308d5973663850e592c5c3";
    public String bridgeIp;// = "192.168.1.179";
    public ArrayList<HueLight> lightArray = new ArrayList<HueLight>();
    private String bridgeFinderUrl = "https://www.meethue.com/api/nupnp";
    private BridgeMiddleMan() {


    }

    public void findAndSetBridgeIp(ActionThreadType threadType) {
        System.out.println("findAndSetBridgeIp()");
        JsonHttpResponseHandler action = new JsonHttpResponseHandler() {

//            @Override
//            public void onStart() {
//                // called before request is started
//            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                // called when response HTTP status is "200 OK"
                try {
                    System.out.println("Bridge IP found: " + response.getJSONObject(0).getString("internalipaddress"));
                    bridgeIp = response.getJSONObject(0).getString("internalipaddress");
                    startNewUsernameRequestThread();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

//            @Override
//            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
//                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
//            }

//            @Override
//            public void onRetry(int retryNo) {
//                // called when request is retried
//            }
        };
        if (ActionThreadType.THREAD_TYPE_ASYNC == threadType) {
            AsyncHttpClient client = new AsyncHttpClient();
            client.get(bridgeFinderUrl, action);
        } else {
            SyncHttpClient client = new SyncHttpClient();
            client.get(bridgeFinderUrl, action);
        }
    }

    private void startNewUsernameRequestThread(){
        if(username.isEmpty()) {
            ScheduledExecutorService worker = Executors.newSingleThreadScheduledExecutor();
            Runnable task = new Runnable() {
                public void run() {
                    Looper.prepare();
                    System.out.println("Do rerun");
                    createUsernameHueBridge(ActionThreadType.THREAD_TYPE_SYNC);
                }
            };
            worker.schedule(task, 2000, TimeUnit.MILLISECONDS);
        } else {
            saveBridgeSettings();
            getAllLamps(ActionThreadType.THREAD_TYPE_SYNC);
        }
    }

    public void createUsernameHueBridge(ActionThreadType threadType) {
        System.out.println("createUsernameHueBridge()");
        final StringEntity entity;
        JsonHttpResponseHandler action =  new JsonHttpResponseHandler() {

            //                @Override
//                public void onStart() {
//                    System.out.println(entity.toString());
//                }
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                // called when response HTTP status is "200 OK"
                try {
                    JSONObject unwrapped = response.getJSONObject(0);
                    try {
                        if(unwrapped.getJSONObject("error").getInt("type") == 101) {
                            System.out.println("Please press the link button");
                        }
                    } catch (JSONException e) {
                        if(e.getMessage().equals("No value for error")) {
                            username = response.getJSONObject(0).getJSONObject("success")
                                    .getString("username");
                        }
                    }
                } catch (JSONException e) {
                    System.out.println("Unwanted JSON exception");
                    e.printStackTrace();
                }
                startNewUsernameRequestThread(); //Exit to safe exit point
            }
        };

        try {
            entity = new StringEntity("{\"devicetype\":\"HueApp#ComfyCrew\"}");
            if(ActionThreadType.THREAD_TYPE_ASYNC == threadType){
                AsyncHttpClient client = new AsyncHttpClient();
                client.post(mContext, "http://" + bridgeIp + "/api/", entity, "application/json", action);
            } else {
                SyncHttpClient client = new SyncHttpClient();
                client.post(mContext, "http://" + bridgeIp + "/api/", entity, "application/json", action);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    public void getAllLamps(ActionThreadType threadType) {
        System.out.println("getAllLamps()");
        JsonHttpResponseHandler action = new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // called when response HTTP status is "200 OK"
                JSONArray lampIds = response.names();
                for (int i = 0; i < lampIds.length(); i++) {
                    try {
                        lightArray.add(
                                parseLampData(response.getJSONObject(lampIds.getString(i)),
                                        Integer.parseInt(lampIds.getString(i))));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("Lamps gotten");
                fireLightsChangedEvent();
            }
        };
        if(ActionThreadType.THREAD_TYPE_ASYNC == threadType) {
            AsyncHttpClient client = new AsyncHttpClient();
            client.get("http://" + bridgeIp + "/api/" + username + "/lights/", action);
        }  else {
            SyncHttpClient client = new SyncHttpClient();
            client.get("http://" + bridgeIp + "/api/" + username + "/lights/", action);
        }
    }

    public HueLight parseLampData(JSONObject jsonDataHue, int id) throws JSONException {
        System.out.println("parseLampData()");
        HueLight hueLight = new HueLight();
        JSONObject jsonHueLightState = jsonDataHue.getJSONObject("state");
        hueLight.id = id;
        hueLight.name = jsonDataHue.getString("name");
        hueLight.isOn = jsonHueLightState.getBoolean("on");
        if (jsonDataHue.getString("type").equals("Extended color light")) {
            hueLight.hue = jsonHueLightState.getInt("hue");
            hueLight.saturation = jsonHueLightState.getInt("sat");
        }
        hueLight.brightness = jsonHueLightState.getInt("bri");
        return hueLight;
    }

    public void setLampState(HueLight hueLight) {
        System.out.println("setLampState()");
    }

    public void saveBridgeSettings() {
        System.out.println("saveBridgeSettings()");
        SharedPreferences settings = mContext.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor settingsEditor = settings.edit();
        settingsEditor.putString("bridgeIp", this.bridgeIp);
        settingsEditor.putString("bridgeUsername", this.username);
        settingsEditor.apply();
    }

    public void loadBridgeSettings(){
        System.out.println("loadBridgeSettings()");
        SharedPreferences settings = mContext.getSharedPreferences(PREFS_NAME, 0);
        bridgeIp = settings.getString("bridgeIp", "");
        username = settings.getString("bridgeUsername", "");
        System.out.println("ALL DATA: " + settings.getAll());
        System.out.println("Loaded: "+username+" and "+bridgeIp+".");
    }

    public void resetBridgeSettings() {
        System.out.println("resetBridgeSettings()");
        SharedPreferences settings = mContext.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor settingsEditor = settings.edit();
        settingsEditor.clear();
        settingsEditor.commit();
    }

    public void connectToBridge(){
        loadBridgeSettings();
        if(bridgeIp.isEmpty()) {
            findAndSetBridgeIp(ActionThreadType.THREAD_TYPE_ASYNC);
            System.out.println("Display getting bridge dialog");
        } else if(username.isEmpty()) {
            startNewUsernameRequestThread();
            System.out.println("Display getting username dialog/request link button press");
        } else {
            getAllLamps(ActionThreadType.THREAD_TYPE_ASYNC);
        }
    }
}