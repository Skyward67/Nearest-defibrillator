package iut.android.tpfinal;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import iut.android.tpfinal.callbackmethods.AsyncCallback;
import iut.android.tpfinal.objects.Defibrilator;

public class SplashScreenActivity extends AppCompatActivity implements LocationListener, AsyncCallback {

    private ArrayList<Defibrilator> defibrilators;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        defibrilators = new ArrayList<>();

        if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(SplashScreenActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            requestAndSwitchActivity();
        }


    }


    private void requestAndSwitchActivity() {

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(SplashScreenActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(SplashScreenActivity.this, new String[]{Manifest.permission.ACCESS_NETWORK_STATE}, 2);
        }
        if(!haveNetworkConnection()){
            Log.d("test Connexion", String.valueOf(haveNetworkConnection()));
            Snackbar.make(findViewById(R.id.progressBar), "Vous n'avez pas de réseaux", Snackbar.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            }, 1500);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) this);
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            AsyncTaskRequest request = new AsyncTaskRequest();
            request.execute(location, defibrilators, this);

        }
    }

    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        @SuppressLint("MissingPermission") NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            requestAndSwitchActivity();
        } else {
            Snackbar.make(findViewById(R.id.progressBar), "Vous devez autoriser la localisation", Snackbar.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            }, 1500);
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

    }

    @Override
    public void onLocationChanged(@NonNull List<Location> locations) {
        LocationListener.super.onLocationChanged(locations);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onFlushComplete(int requestCode) {
        LocationListener.super.onFlushComplete(requestCode);
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        LocationListener.super.onProviderEnabled(provider);
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        LocationListener.super.onProviderDisabled(provider);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

    @Override
    public void endAsyncTask() {
        Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
        intent.putExtra("list", defibrilators);
        startActivity(intent);
        finish();
    }
}

//private class asynchStart
class AsyncTaskRequest extends AsyncTask<Object, Void, String> {

    private Location location;
    private ArrayList<Defibrilator> defibrilators;
    private AsyncCallback callback;

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected String doInBackground(Object... objects) {
        this.location = (Location) objects[0];
        this.defibrilators = (ArrayList<Defibrilator>) objects[1];
        this.callback = (AsyncCallback) objects[2];


        double latitude = location.getLatitude();//48.88014609972204;//location.getLatitude();
        double longitude = location.getLongitude();//2.356903;//location.getLongitude();
        long distance = 10000;

        String toparse = "";
        JSONArray data = null;
        JSONObject answer = null;
        //Request
        try {
            URL url = new URL("https://data.opendatasoft.com/api/records/1.0/search/?dataset=osm-aed-fr%40babel&q=&facet=etat_fonct&facet=departement&facet=commune&geofilter."
                    +"distance="+latitude+"%2C+"+longitude+"%2C+" + distance);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            if(urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK){
                BufferedReader input = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                toparse = input.readLine();
                input.close();
            }
            urlConnection.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Traitement
        try {
            answer = new JSONObject(toparse);
            data = answer.getJSONArray("records");
            for(int i = 0; i < data.length(); i++){
                JSONObject obj = data.getJSONObject(i).getJSONObject("fields");
                if(obj.has("acc")){
                    if(obj.has("acc_complt")){
                        defibrilators.add(new Defibrilator(obj.getString("com_nom"),
                                obj.getString("departement"),
                                Double.parseDouble(obj.getString("lat_coor1")),
                                Double.parseDouble(obj.getString("long_coor1")),
                                Double.parseDouble(obj.getString("dist")),
                                obj.getString("acc"),
                                obj.getString("acc_complt")));
                    } else {
                        defibrilators.add(new Defibrilator(obj.getString("com_nom"),
                                obj.getString("departement"),
                                Double.parseDouble(obj.getString("lat_coor1")),
                                Double.parseDouble(obj.getString("long_coor1")),
                                Double.parseDouble(obj.getString("dist")),
                                obj.getString("acc")));
                    }
                } else {
                    defibrilators.add(new Defibrilator(obj.getString("com_nom"),
                            obj.getString("departement"),
                            Double.parseDouble(obj.getString("lat_coor1")),
                            Double.parseDouble(obj.getString("long_coor1")),
                            Double.parseDouble(obj.getString("dist"))));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("test", String.valueOf(answer));
        return toparse;
    }

    @Override
    protected void onPostExecute(String s) {
        callback.endAsyncTask();
        super.onPostExecute(s);
    }
}