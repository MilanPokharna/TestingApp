package com.collegeapp.collegeapp.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.collegeapp.collegeapp.R;
import com.collegeapp.collegeapp.models.PathJSONParser;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    public static final int code=100;
    ArrayList<LatLng> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        list=new ArrayList<>();

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.getUiSettings().setZoomControlsEnabled(true);

        if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MapsActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},code);

            return;
        }

        list.add(new LatLng(24.5176,73.7516));
        list.add(new LatLng(24.5270,73.7586));
        list.add(new LatLng(24.5400,73.7686));
        list.add(new LatLng(24.550,73.7575));
        list.add(new LatLng(24.5993,73.7763));
        mMap.setMyLocationEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.zoomBy(10));

        for(int i=0;i<list.size()-1;++i) {

            String url = getURL(list.get(i), list.get(i+1));

            Log.i("list",list.toString());

            Log.i("url",url);
            ReadTask readTask=new ReadTask();
            readTask.execute(url);

        }

    }

    private String getURL(LatLng origin, LatLng dest) {

        String str_origin = "origin="+origin.latitude+","+origin.longitude;

        String str_dest = "destination="+dest.latitude+","+dest.longitude;

        String sensor = "sensor=false";

        String parameters = str_origin+"&"+str_dest+"&"+sensor;

        String output = "json";

        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;

        return url;
    }

    public String getURLDirection(String urlString) throws IOException {
        String data = "";
        InputStream istream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            istream = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(istream));
            StringBuffer sb = new StringBuffer();
            String line ="";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            data = sb.toString();
            br.close();


        }
        catch (Exception e) {
            e.printStackTrace();
        } finally {
            istream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    private class ReadTask extends AsyncTask<String, Void , String> {

        @Override
        protected String doInBackground(String... url) {
            // TODO Auto-generated method stub
            String data = "";
            try {
                data = getURLDirection(url[0]);
            } catch (Exception e) {
                // TODO: handle exception
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            new ParserTask().execute(result);
        }

    }

    @SuppressLint("StaticFieldLeak")
    private class ParserTask extends AsyncTask<String,Integer, List<List<HashMap<String , String >>>> {
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(
                String... jsonData) {
            // TODO Auto-generated method stub
            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;
            try {
                jObject = new JSONObject(jsonData[0]);
                PathJSONParser parser = new PathJSONParser();
                routes = parser.parse(jObject);


            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> routes) {
            ArrayList<LatLng> points = null;
            PolylineOptions polyLineOptions = null;

            // traversing through routes
            for (int i = 0; i < routes.size(); i++) {
                points = new ArrayList<LatLng>();
                polyLineOptions = new PolylineOptions();
                List<HashMap<String, String>> path = routes.get(i);

                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                polyLineOptions.addAll(points);
                polyLineOptions.width(20);
                polyLineOptions.color(Color.BLUE);
            }

            mMap.addPolyline(polyLineOptions);

        }}


    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==code){

            if(grantResults.length>0&& grantResults[0]==PackageManager.PERMISSION_GRANTED){
                mMap.setMyLocationEnabled(true);
            }

        }

    }
}
