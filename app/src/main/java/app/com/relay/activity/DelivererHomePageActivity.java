package app.com.relay.activity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import app.com.relay.R;
import app.com.relay.utils.Constant;
import app.com.relay.utils.Tools;

public class DelivererHomePageActivity extends AppCompatActivity {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deliverer_home_page);
        initMapFragment();
        Tools.setSystemBarColor(this, R.color.colorPrimary);
    }

    private void initMapFragment() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = Tools.configActivityMaps(googleMap);
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    mMap.setMyLocationEnabled(true);
                    mMap.moveCamera(zoomingLocation());
                }
               /* MarkerOptions markerOptions = new MarkerOptions().position(new LatLng(37.7610237, -122.4217785));
                mMap.addMarker(markerOptions);
                mMap.moveCamera(zoomingLocation());
                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        try {
                            mMap.animateCamera(zoomingLocation());
                        } catch (Exception e) {
                        }
                        return true;
                    }
                });*/
            }
        });
    }

    public void getStore(Context context){
        String URL = Constant.URL_STORES  ;
        System.out.println(URL);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONArray>() {
                    HashMap<String, String> storeMap = new HashMap<>();

                    @Override
                    public void onResponse(JSONArray response) {
                        ArrayList<HashMap<String, String>> list2 = new ArrayList<>();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                // product = new Product(jsonObject);
                                Log.e("Respence Json: ", String.valueOf(jsonObject));
                                Log.e("Respence Store: ",  jsonObject.getString("store_name"));
                                storeMap.put("store_name", jsonObject.getString("store_name"));
                                storeMap.put("phone_number",jsonObject.getString("phone_number"));
                                storeMap.put("address", jsonObject.getString("address"));
                                storeMap.put("mail",jsonObject.getString("mail"));
                                storeMap.put("image",Constant.URL_IMAGE+jsonObject.getString("image"));
                                storeMap.put("user_type",jsonObject.getString("user_type"));
                                storeMap.put("store_type",jsonObject.getString("store_type"));
                                storeMap.put("id", String.valueOf(jsonObject.getString("id")));
                                //System.out.println("Size of list is  : "+ list.size());
                                list2.add(storeMap);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        getAllMarkers(getApplicationContext(),list2);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("ERROR", "Oups Something went wrong !");
                    }
                }
        );
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(jsonArrayRequest);
    }

    private void getAllMarkers(Context applicationContext, ArrayList<HashMap<String, String>> storeMap) {
        Geocoder coder = new Geocoder(this);
        List<Address> address;

        LatLng  p1 = null;

        try {
            for(HashMap<String, String> store : storeMap) {
                address = coder.getFromLocationName(store.get("address"), 5);
                if (address == null) {
                    return;
                }
                Address location = address.get(0);
                location.getLatitude();
                location.getLongitude();

                p1 = new LatLng((double) (location.getLatitude() * 1E6),
                        (double) (location.getLongitude() * 1E6));
                System.out.println("p1" + p1);
            }
        }catch (Exception e){
            e.getMessage();
        }

    }


    private CameraUpdate zoomingLocation() {
        LocationManager locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();

        return CameraUpdateFactory.newLatLngZoom(new LatLng(
                31, 10), 20);
    }


    public void clickAction(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.map_button:
                Toast.makeText(getApplicationContext(), "Map Clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.list_button:
                Toast.makeText(getApplicationContext(), "List Clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.add_button:
                Toast.makeText(getApplicationContext(), "Add Clicked", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
