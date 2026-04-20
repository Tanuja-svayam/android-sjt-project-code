// Copyright 2020 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.shrikantelectronics;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowCloseListener;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;


import java.util.Map;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

public class MapActivity extends AppCompatActivity implements
        OnMapReadyCallback,
        OnMarkerClickListener,
        OnInfoWindowClickListener,
        OnMarkerDragListener,
        OnInfoWindowLongClickListener,
        OnInfoWindowCloseListener,
        OnMapAndViewReadyListener.OnGlobalLayoutAndMapReadyListener {

    private static String URL = Config.WEBSERVICE_URL;
    private FusedLocationProviderClient fusedLocationProviderClient;
    public int noofleads = 0;

    private GoogleMap mMap;
    private double latitude, longitude;
    public Location location;
    private TextView mTopText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        mTopText = (TextView) findViewById(R.id.top_text);

        // Initialize FusedLocationProviderClient
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // Check permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Request location permission if not granted
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    100);
        } else {
            getLastLocation();
        }

    }

    private void getLastLocation() {
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
        fusedLocationProviderClient.getLastLocation()
                .addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful() && task.getResult() != null) {


                            location = task.getResult();

                            // location.setLatitude(21.199685);
                            // location.setLongitude(72.782802);

                            latitude = location.getLatitude();
                            longitude = location.getLongitude();

                            // Check if the map is already ready, and if so, update it
                            if (mMap != null) {
                                updateMap();
                            }
                        } else {
                            Toast.makeText(MapActivity.this, "Unable to get location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Initialize the GoogleMap object
        mMap = googleMap;

        // After the map is ready, check if we have the location
        if (latitude != 0 && longitude != 0) {
            updateMap();
        }

        mMap.setOnMarkerClickListener(this);
        mMap.setOnInfoWindowClickListener(this);
        mMap.setOnMarkerDragListener(this);
        mMap.setOnInfoWindowCloseListener(this);
        mMap.setOnInfoWindowLongClickListener(this);

    }

    private void updateMap() {
        // Create a LatLng object from the latitude and longitude
        LatLng nlocation = new LatLng(latitude, longitude);

        // Add a marker at the current location
        mMap.addMarker(new MarkerOptions().position(nlocation).title("You are here").icon(BitmapDescriptorFactory.fromResource(R.drawable.you_are_here)));

        // Move the camera to the current location and zoom in
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(nlocation, 15));


        fetchAndDisplaybrandes(location);
    }

    // Handle permission result
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getLastLocation();
        } else {
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
        }
    }

    public void fetchAndDisplaybrandes(Location currentLocation) {

        try {
            Map<String, String> paramsMap = new HashMap<>();

            paramsMap.put("fromdate", "fromdate");
            paramsMap.put("todate", "todate");

            final int DEFAULT_TIMEOUT = 200000 * 1000000000;
            //AsyncHttpClient client = new AsyncHttpClient();
            //client.setTimeout(DEFAULT_TIMEOUT);

            ApiHelper.post(URL + "Service1.asmx/GetDeliveryLocationFromInvoiceOrderDetail", paramsMap, new ApiHelper.ApiCallback() {
                @Override

                public void onSuccess(JSONObject response) {
                    try {

                        JSONObject obj = response;
                        JSONArray new_array = obj.getJSONArray("followup_register");

                        processPinsInBatches(new_array, currentLocation, 0, 100);


                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        //Toast.makeText(getApplicationContext(), "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                        e.printStackTrace();

                        Toast.makeText(getApplicationContext(), "Status code :" + e.toString() + "errmsg : " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }

                // When the response returned by REST has Http response code other than '200'
                @Override
                public void onFailure(String errorMessage) {
                    Toast.makeText(getApplicationContext(), "API Error: " + errorMessage, Toast.LENGTH_LONG).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            //	Toast.makeText(getApplicationContext(), e.getMessage() , Toast.LENGTH_LONG).show();
        }

    }


    private void processPinsInBatches(JSONArray array, Location currentLocation, int startIndex, int batchSize) {
        int endIndex = Math.min(startIndex + batchSize, array.length());

        for (int i = startIndex; i < endIndex; i++) {
            try {
                JSONObject jsonObject = array.getJSONObject(i);

                brand brandData = new brand();
                brandData.category = jsonObject.getString("topcategoryname");
                brandData.brand = jsonObject.getString("brandname");
                brandData.modelname = jsonObject.getString("modelname");
                brandData.custname = jsonObject.getString("custname");
                brandData.productvalue = jsonObject.getString("netamt");
                brandData.latitude = jsonObject.getString("latitude");
                brandData.longitude = jsonObject.getString("longitude");

                placeMarkerOnMap(brandData, currentLocation);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        // update text
        mTopText.setText("Loaded " + noofleads + " pins so far");

        // schedule next batch if still items left
        if (endIndex < array.length()) {
            final int nextStart = endIndex;
            mTopText.postDelayed(() -> {
                processPinsInBatches(array, currentLocation, nextStart, batchSize);
            }, 200); // small delay lets UI refresh
        } else {
            mTopText.setText("Finished loading " + noofleads + " customers");
        }
    }

    private void placeMarkerOnMap(brand brandData, Location currentLocation) {

        double latitude, longitude;
        latitude = Double.parseDouble(brandData.latitude) ;
        longitude = Double.parseDouble(brandData.longitude) ;

        LatLng latLng = new LatLng(latitude, longitude);

        // Check if the brand is within 5 km of the user's location
        Location brandLocation = new Location("");
        brandLocation.setLatitude(latitude);
        brandLocation.setLongitude(longitude);

        float distance = currentLocation.distanceTo(brandLocation) / 1000; // in kilometers

       // if (distance <= 100) {

            int markerIconResId = getMarkerIconBasedOnStatus(brandData.getcategory());

            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title(brandData.getcategory())
                    .snippet("Distance: " + distance + " km")
                    .icon(BitmapDescriptorFactory.fromResource(markerIconResId)));

            String category = "Category : " +  brandData.getcategory();
            String brand = "Brand : " +  brandData.getbrand();
            String custname = "Customer : " +  brandData.getcustname();
            String productvalue = "Invoice Value : " +  brandData.getproductvalue();
            String sdistance = "Distance: " + distance + " km";

            // Attach custom data to the marker
            CustomData customData = new CustomData(category ,brand, brandData.getmodelname(),custname, productvalue,sdistance,123);
            marker.setTag(customData);

            // Set up InfoWindow adapter for custom display
            mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                @Nullable
                @Override
                public View getInfoWindow(@NonNull Marker marker) {
                    return null; // Default InfoWindow frame
                }

                @Nullable
                @Override
                public View getInfoContents(@NonNull Marker marker) {
                    CustomData data = (CustomData) marker.getTag();
                    if (data == null) return null;

                    View view = LayoutInflater.from(MapActivity.this)
                            .inflate(R.layout.custom_info_window, null);

                    TextView category = view.findViewById(R.id.category);
                    TextView brand = view.findViewById(R.id.brand);
                    TextView modelname = view.findViewById(R.id.modelname);
                    TextView custname = view.findViewById(R.id.custname);
                    TextView productvalue = view.findViewById(R.id.productvalue);
                    TextView distance = view.findViewById(R.id.distance);

                    category.setText(data.category);
                    brand.setText(data.brand);
                    modelname.setText(data.modelname);
                    custname.setText(data.custname);
                    productvalue.setText(data.productvalue);
                    distance.setText(data.distance);

                    return view;
                }
            });

            // Handle marker click
            mMap.setOnMarkerClickListener(marker1 -> {
                CustomData data = (CustomData) marker1.getTag();
                if (data != null) {
                    //Toast.makeText(this, "Clicked on: " + data.category, Toast.LENGTH_SHORT).show();
                }
                return false;
            });

            noofleads = noofleads + 1;

        //}
    }

    private int getMarkerIconBasedOnStatus(String leadstatus) {
        int iconResId;

        switch (leadstatus) {
            case "01":
                iconResId = R.drawable.pin_red; // Add your custom icon for 'new'
                break;
            case "02":
                iconResId = R.drawable.pin_blue; // Add your custom icon for 'in-progress'
                break;
            case "03":
                iconResId = R.drawable.pin_blue; // Add your custom icon for 'in-progress'
                break;
            case "04":
                iconResId = R.drawable.pin_blue; // Add your custom icon for 'in-progress'
                break;
            case "05":
                iconResId = R.drawable.pin_blue; // Add your custom icon for 'in-progress'
                break;
            case "06":
                iconResId = R.drawable.pin_blue; // Add your custom icon for 'in-progress'
                break;
            case "07":
                iconResId = R.drawable.pin_blue; // Add your custom icon for 'in-progress'
                break;
            case "10":
                iconResId = R.drawable.pin_green; // Add your custom icon for 'closed'
                break;
            default:
                iconResId = R.drawable.pin_red; // Fallback icon
                break;
        }

        return iconResId;
    }


    public class CustomData {
        String category;
        String brand;
        String  modelname;
        String  custname;
        String  distance;
        String  productvalue;
        int id;

        public CustomData(String category, String brand, String modelname,String custname, String productvalue, String distance, int id) {
            this.category = category;
            this.brand = brand;
            this.modelname = modelname;
            this.custname = custname;
            this.productvalue = productvalue;
            this.distance = distance;
            this.id = id;
        }
    }

    public class brand {
        private String category;
        private String brand;
        private String modelname;
        private String custname;
        private String productvalue;
        private String latitude;
        private String longitude;

        public brand() {}

        public brand(String category, String brand, String modelname, String custname, String productvalue, String latitude, String longitude)
        {
            this.category = category;
            this.brand = brand;
            this.modelname = modelname;
            this.custname = custname;
            this.productvalue = productvalue;
            this.latitude = latitude;
            this.longitude = longitude;

        }

        public String getcategory() { return category; }
        public String getbrand() { return brand; }
        public String getmodelname() { return modelname; }
        public String getcustname() { return custname; }
        public String getproductvalue() { return productvalue; }
        public String getlatitude() { return latitude; }
        public String getlongitude() { return longitude; }


    }

    @Override
    public boolean onMarkerClick(final Marker marker) {


        return false;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {


        LatLng destination = marker.getPosition();

        // Create a Uri for navigation
        String uri = "google.navigation:q=" + destination.latitude + "," + destination.longitude;
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setPackage("com.google.android.apps.maps"); // Ensure it opens in Google Maps

        // Check if Google Maps is installed
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent); // Start navigation
        } else {
            Toast.makeText(this, "Google Maps is not installed", Toast.LENGTH_SHORT).show();
        }
        Toast.makeText(this, "Clicked on: " + marker.getTitle() , Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onInfoWindowClose(Marker marker) {
        //Toast.makeText(this, "Close Info Window", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onInfoWindowLongClick(Marker marker) {
        Toast.makeText(this, "Info Window long click", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMarkerDragStart(Marker marker) {
        mTopText.setText("onMarkerDragStart");
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        mTopText.setText("onMarkerDragEnd");
    }

    @Override
    public void onMarkerDrag(Marker marker) {
        mTopText.setText("onMarkerDrag.  Current Position: " + marker.getPosition());
    }

}
// [END maps_android_sample_marker]


    /*
    private void resolvebrandToLatLong(brand brandData, Location currentLocation) {
        String url = GEOCODING_API_URL + "?brand=" + brandData.getbrand().replace(" ", "+") + "&key=" + API_KEY;

        new Thread(() -> {
            try {
                // Make the HTTP request
                Request request = new Request.Builder().url(url).build();
                Response response = httpClient.newCall(request).execute();

                if (response.isSuccessful() && response.body() != null) {
                    String responseBody = Objects.requireNonNull(response.body()).string();
                    JSONObject jsonResponse = new JSONObject(responseBody);
                    JSONArray results = jsonResponse.getJSONArray("results");

                    if (results.length() > 0) {
                        JSONObject location = results.getJSONObject(0)
                                .getJSONObject("geometry")
                                .getJSONObject("location");

                        double latitude = location.getDouble("lat");
                        double longitude = location.getDouble("lng");

                        // Place a marker on the map
                        runOnUiThread(() -> placeMarkerOnMap(brandData,  currentLocation));
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, "Error resolving brand: " + brandData.getbrand(), e);
            }
        }).start();
    }


    private BitmapDescriptor vectorToBitmap(@DrawableRes int id, @ColorInt int color) {
        Drawable vectorDrawable = ResourcesCompat.getDrawable(getResources(), id, null);
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        DrawableCompat.setTint(vectorDrawable, color);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

*/

