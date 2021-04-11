package hackku2021.trackify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.net.URISyntaxException;

public class ArActivity extends AppCompatActivity {

    private WebView webView;
    private LocationManager locationManager;
    private ProgressBar progressBar;
  //  private Location lastKnowLocation;
  //  private LocationListener locationListener;;
  //  private Double firebaseLat, firebaseLon;
  //  private TextView textView;
    private String serviceRequired = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ar);

        getSupportActionBar().hide();

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        webView = findViewById(R.id.ar_webView);
        progressBar = findViewById(R.id.arActivityProgressBar);
      //  textView = findViewById(R.id.arTextView);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("intent://")) {
                    try {
                        Context context = view.getContext();
                        Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);

                        if (intent != null) {
                            view.stopLoading();

                            PackageManager packageManager = context.getPackageManager();
                            ResolveInfo info = packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
                            if (info != null) {
                                context.startActivity(intent);
                            } else {
                                String fallbackUrl = intent.getStringExtra("browser_fallback_url");
                                view.loadUrl(fallbackUrl);

                                // or call external broswer
//                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(fallbackUrl));
//                    context.startActivity(browserIntent);
                            }

                            return true;
                        }
                    } catch (URISyntaxException e) {
                        Toast.makeText(ArActivity.this, "Error Loading", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }

                return false;
            }
        });

        getLatLong();

      //  webView.loadUrl("https://console.echoar.xyz/webar?key=lingering-brook-2924&entry=cde87ef9-6e17-48d6-aa7f-190709c4d6a7");
       // checkLocationPermission();
       // addLocationListener();
    }

    private void getLatLong()
    {
        FirebaseDatabase.getInstance().getReference().child("maint")
                .child("0000001").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try{
                    /*
                    String lat = snapshot.child("lat").getValue().toString();
                    String lon = snapshot.child("long").getValue().toString();
                    firebaseLat = Double.parseDouble(lat);
                    firebaseLon = Double.parseDouble(lon);

                     */
                    progressBar.setVisibility(View.VISIBLE);
                    serviceRequired = snapshot.child("serviceRequire").getValue().toString();
                  //  Log.i("Location", firebaseLat +" "+ firebaseLon);
                 //   Log.i("Status", serviceRequired);
                    calculateDistance();
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

/*
    private double distance(double lat1, double lat2,
                            double lon1, double lon2)
    {
        lon1 = Math.toRadians(lon1);
        lon2 = Math.toRadians(lon2);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        double dlon = lon2 - lon1;
        double dlat = lat2 - lat1;
        double a = Math.pow(Math.sin(dlat / 2), 2)
                + Math.cos(lat1) * Math.cos(lat2)
                * Math.pow(Math.sin(dlon / 2),2);

        double c = 2 * Math.asin(Math.sqrt(a));

        double r = 6371;

        return(c * r);
    }

 */

    private void calculateDistance()
    {
        /*
        if(lastKnowLocation==null)
            return;

        double dist = distance(lastKnowLocation.getLatitude(), firebaseLat,
                lastKnowLocation.getLatitude(), firebaseLon);

        Log.i("Location dist", Double.toString(dist));

         */


         //   textView.setVisibility(View.GONE);
            webView.setVisibility(View.VISIBLE);

            if(serviceRequired.equals("true"))
                webView.loadUrl("https://console.echoar.xyz/webar?key=lingering-brook-2924&entry=cde87ef9-6e17-48d6-aa7f-190709c4d6a7");
            else
                webView.loadUrl("https://console.echoar.xyz/webar?key=lingering-brook-2924&entry=3accc035-3055-48c7-8640-f96de16b6e4f");

            progressBar.setVisibility(View.GONE);
    }
/*
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                lastKnowLocation = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
                if (lastKnowLocation == null)
                    lastKnowLocation = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);

                getLatLong();
            }
        }
    }

    private void checkLocationPermission()
    {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            lastKnowLocation = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
            if (lastKnowLocation == null)
                lastKnowLocation = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);

            LatLng latLng = new LatLng(lastKnowLocation.getLatitude(), lastKnowLocation.getLongitude());
            Log.i("Location", latLng.toString());

            getLastKnowLocation();
        }
    }

    private void getLastKnowLocation()
    {
        if (lastKnowLocation != null) {
            getLatLong();
        }
    }

    private void addLocationListener()
    {
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                lastKnowLocation = location;
                calculateDistance();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
    }
*/
}