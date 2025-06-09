package com.eduardorivera.navegador.ui.escaner;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.app.AlertDialog;

import com.blikoon.qrcodescanner.QrCodeActivity;
import com.eduardorivera.navegador.R;

public class EscanerFragment extends Fragment {

    private static final int REQUEST_CODE_QR_SCAN = 101;
    private static final int CAMERA_PERMISSION_CODE = 102;

    public EscanerFragment() {
        // Constructor vacío requerido
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Infla el layout del fragmento
        return inflater.inflate(R.layout.fragment_escaner, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button buttonScan = view.findViewById(R.id.button_start_scan);

        buttonScan.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(requireActivity(),
                        new String[]{Manifest.permission.CAMERA},
                        CAMERA_PERMISSION_CODE);
            } else {
                Intent i = new Intent(requireContext(), QrCodeActivity.class);
                startActivityForResult(i, REQUEST_CODE_QR_SCAN);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK || data == null) return;

        if (requestCode == REQUEST_CODE_QR_SCAN) {
            String result = data.getStringExtra("com.blikoon.qrcodescanner.got_qr_scan_relult");
            if (result != null) {
                new AlertDialog.Builder(requireContext())
                        .setTitle("Resultado")
                        .setMessage(result)
                        .setPositiveButton("OK", null)
                        .show();
            }
        }
    }
}
