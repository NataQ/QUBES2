package id.co.qualitas.qubes.activity;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.model.Customer;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private MapView mMapView;
    private ImageView imgDirect;
    private UiSettings mUiSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps2);

        imgDirect = findViewById(R.id.imgDirect);
        mMapView = findViewById(R.id.map);

        mMapView.onCreate(savedInstanceState);

        initMap();

        imgDirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                            Uri.parse("http://maps.google.com/maps?saddr=" + -6.090263984566263 + "," + 106.74593288657607 + "&daddr=" + -6.088542162422348 + "," + 106.74239952686823));
                    startActivity(intent);
                } catch (ActivityNotFoundException ane) {
                    Toast.makeText(getApplicationContext(), "Please Install Google Maps ", Toast.LENGTH_LONG).show();
                } catch (Exception ex) {
                    ex.getMessage();
                }
            }
        });
    }

    private void initMap() {
        mMapView.onResume();
        mMapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mUiSettings = mMap.getUiSettings();
        mUiSettings.setZoomControlsEnabled(true);
        mUiSettings.setCompassEnabled(true);
        mUiSettings.setScrollGesturesEnabled(true);
        mUiSettings.setZoomGesturesEnabled(true);
        mUiSettings.setTiltGesturesEnabled(true);
        mUiSettings.setRotateGesturesEnabled(true);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);

        // Add a marker in Sydney and move the camera

        List<Customer> custList = new ArrayList<>();
        custList.add(new Customer("BO", "Black Owl", "Golf Island Beach Theme Park, Jl. Pantai Indah Kapuk No.77, Kamal Muara, DKI Jakarta 14470", true, new LatLng(-6.090263984566263, 106.74593288657607)));
        custList.add(new Customer("PCP", "Pantjoran Chinatown PIK", "Unnamed Road, 14460", false, new LatLng(-6.09047339393416, 106.74535959301855)));
        custList.add(new Customer("CMP", "Central Market PIK", "Golf Island, Kawasan Pantai Maju, Jl, Jl. Boulevard Raya, Kamal Muara, Kec. Penjaringan, Daerah Khusus Ibukota Jakarta 14470", true, new LatLng(-6.09102018270127, 106.74661148098058)));
        custList.add(new Customer("CHGI", "Cluster Harmony, Golf Island", "WP6W+7JR, Pantai Indah Kapuk St, Kamal Muara, Penjaringan, North Jakarta City, Jakarta 14460", false, new LatLng(-6.089065696336256, 106.74676357552187)));
        custList.add(new Customer("MSGIP", "Monsieur Spoon Golf Island PIK", "Urban Farm, Unit 5, Kawasan Pantai Maju Jl. The Golf Island Boulevard, Kel, Kamal Muara, Kec. Penjaringan, Daerah Khusus Ibukota Jakarta 14460", true, new LatLng(-6.09032214182743, 106.74191982249332)));
        custList.add(new Customer("KMPP", "K3 Mart PIK Pantjoran", "Golf Island, Ruko Blok D No.02A, Kamal Muara, Jkt Utara, Daerah Khusus Ibukota Jakarta 11447", false, new LatLng(-6.088542162422348, 106.74239952686823)));

        for (Customer cust : custList) {
//            Marker marker =
            mMap.addMarker(new MarkerOptions()
                    .position(cust.getPosition())
//                    .title(cust.getIdCustomer() + " - " + cust.getNameCustomer())
//                    .snippet(cust.getAddress())
                    .icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(cust))));
//                    .icon(BitmapDescriptorFactory.fromResource(cust.isRoute() ? R.drawable.marker_red : R.drawable.marker_blue)));
//            marker.showInfoWindow();

        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(custList.get(0).getPosition(), 15));
    }

    private Bitmap getMarkerBitmapFromView(Customer cust) {
        //HERE YOU CAN ADD YOUR CUSTOM VIEW
        View customMarkerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.map_marker, null);

        //IN THIS EXAMPLE WE ARE TAKING TEXTVIEW BUT YOU CAN ALSO TAKE ANY KIND OF VIEW LIKE IMAGEVIEW, BUTTON ETC.
        TextView txt_name = customMarkerView.findViewById(R.id.txt_name);
        TextView txt_add = customMarkerView.findViewById(R.id.txt_add);
        ImageView imgStore = customMarkerView.findViewById(R.id.imgStore);

        txt_name.setText(cust.getIdCustomer() + " - " + cust.getNameCustomer());
        txt_add.setText(cust.getAddress());
        if (cust.isRoute()) {
            imgStore.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_store_test));
        } else {
            imgStore.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_store_test2));
        }

        customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        customMarkerView.layout(0, 0, customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight());
        customMarkerView.buildDrawingCache();
        Bitmap returnedBitmap = Bitmap.createBitmap(customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
        Drawable drawable = customMarkerView.getBackground();
        if (drawable != null) drawable.draw(canvas);
        customMarkerView.draw(canvas);
        return returnedBitmap;
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mMapView != null) {
            mMapView.onDestroy();
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }
}