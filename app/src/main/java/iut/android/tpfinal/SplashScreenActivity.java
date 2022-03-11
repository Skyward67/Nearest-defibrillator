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
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
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

import iut.android.tpfinal.objects.Defibrilator;

public class SplashScreenActivity extends AppCompatActivity implements LocationListener {

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
            requestAndSwitchAtivity();
        }


    }

    private void requestAndSwitchAtivity() {

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) this);
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        AsyncTaskRequest request = new AsyncTaskRequest();
        request.execute(location, defibrilators);
        try {
            request.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (Defibrilator def : defibrilators) {
            Log.d("list :", def.toString());
        }


        Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
        intent.putExtra("list", defibrilators);
        startActivity(intent);
        finish();
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            //Log.d("permission1", Integer.toString(grantResults[1]));

            //Get Location & Request
            requestAndSwitchAtivity();

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
}

//private class asynchStart
class AsyncTaskRequest extends AsyncTask<Object, Void, String> {

    private Location location;
    private ArrayList<Defibrilator> defibrilators;

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected String doInBackground(Object... objects) {
        this.location = (Location) objects[0];
        this.defibrilators = (ArrayList<Defibrilator>) objects[1];


        double latitude = 46.205262;//location.getLatitude();
        double longitude = 5.224705;//location.getLongitude();
        long distance = 1000;

        String toparse = "";
        JSONArray data = null;
        JSONObject answer = null;
        //Request
        try {
            URL url = new URL("https://data.opendatasoft.com/api/records/1.0/search/?dataset=osm-aed-fr%40babel&q=&facet=etat_fonct&facet=departement&facet=commune&geofilter."
                    +"distance="+latitude+"%2C+"+longitude+"%2C+" + 1000);
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
                defibrilators.add(new Defibrilator(obj.getString("com_nom"),
                                                    obj.getString("departement"),
                                                    Double.parseDouble(obj.getString("lat_coor1")),
                                                    Double.parseDouble(obj.getString("long_coor1"))));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("test", String.valueOf(answer));
        return toparse;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}