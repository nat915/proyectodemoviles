package com.eduardorivera.navegador.ui.ubicacion;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.eduardorivera.navegador.R;

public class UbicacionFragment extends Fragment {

    private static final int LOCATION_PERMISSION_REQUEST = 1;

    private LocationManager locationManager;

    private TextView longitudeValueGPS, latitudeValueGPS;
    private TextView longitudeValueNetwork, latitudeValueNetwork;
    private TextView longitudeValueBest, latitudeValueBest;

    private boolean isGPSUpdating = false;
    private boolean isNetworkUpdating = false;
    private boolean isBestUpdating = false;

    private final LocationListener gpsListener = location -> updateLocationUI(location, longitudeValueGPS, latitudeValueGPS);
    private final LocationListener networkListener = location -> updateLocationUI(location, longitudeValueNetwork, latitudeValueNetwork);
    private final LocationListener bestListener = location -> updateLocationUI(location, longitudeValueBest, latitudeValueBest);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ubicacion, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        locationManager = (LocationManager) requireContext().getSystemService(getContext().LOCATION_SERVICE);

        longitudeValueGPS = view.findViewById(R.id.longitudeValueGPS);
        latitudeValueGPS = view.findViewById(R.id.latitudeValueGPS);
        longitudeValueNetwork = view.findViewById(R.id.longitudeValueNetwork);
        latitudeValueNetwork = view.findViewById(R.id.latitudeValueNetwork);
        longitudeValueBest = view.findViewById(R.id.longitudeValueBest);
        latitudeValueBest = view.findViewById(R.id.latitudeValueBest);

        view.findViewById(R.id.locationControllerGPS).setOnClickListener(v -> toggleGPSUpdates());
        view.findViewById(R.id.locationControllerNetwork).setOnClickListener(v -> toggleNetworkUpdates());
        view.findViewById(R.id.locationControllerBest).setOnClickListener(v -> toggleBestUpdates());

        requestPermissions();
    }

    private void requestPermissions() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            }, LOCATION_PERMISSION_REQUEST);
        }
    }

    private boolean checkLocationPermission() {
        return ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void toggleGPSUpdates() {
        if (isGPSUpdating) {
            locationManager.removeUpdates(gpsListener);
            isGPSUpdating = false;
            Toast.makeText(getContext(), "GPS Pausado", Toast.LENGTH_SHORT).show();
        } else {
            if (checkLocationPermission()) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, gpsListener);
                isGPSUpdating = true;
                Toast.makeText(getContext(), "GPS Activado", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void toggleNetworkUpdates() {
        if (isNetworkUpdating) {
            locationManager.removeUpdates(networkListener);
            isNetworkUpdating = false;
            Toast.makeText(getContext(), "Red Pausada", Toast.LENGTH_SHORT).show();
        } else {
            if (checkLocationPermission()) {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, networkListener);
                isNetworkUpdating = true;
                Toast.makeText(getContext(), "Red Activada", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void toggleBestUpdates() {
        if (isBestUpdating) {
            locationManager.removeUpdates(bestListener);
            isBestUpdating = false;
            Toast.makeText(getContext(), "Best Pausado", Toast.LENGTH_SHORT).show();
        } else {
            if (checkLocationPermission()) {
                locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 1000, 1, bestListener);
                isBestUpdating = true;
                Toast.makeText(getContext(), "Best Activado", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void updateLocationUI(Location location, TextView longitudeView, TextView latitudeView) {
        longitudeView.setText(String.format("%.4f", location.getLongitude()));
        latitudeView.setText(String.format("%.4f", location.getLatitude()));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (checkLocationPermission()) {
            locationManager.removeUpdates(gpsListener);
            locationManager.removeUpdates(networkListener);
            locationManager.removeUpdates(bestListener);
        }
    }
}
