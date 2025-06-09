package com.eduardorivera.navegador.ui.generador;

import com.eduardorivera.navegador.R;
import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import net.glxn.qrgen.android.QRCode;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class GeneradorFragment extends Fragment {

    private static final int CODIGO_PERMISO_ESCRIBIR = 100;
    private static final int ALTURA_CODIGO = 500, ANCHURA_CODIGO = 500;

    private EditText etTextoParaCodigo;
    private boolean tienePermisoParaEscribir = false;

    private String obtenerTextoParaCodigo() {
        etTextoParaCodigo.setError(null);
        String texto = etTextoParaCodigo.getText().toString();
        if (texto.isEmpty()) {
            etTextoParaCodigo.setError("Escribe el texto del código QR");
            etTextoParaCodigo.requestFocus();
        }
        return texto;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_generador, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        etTextoParaCodigo = view.findViewById(R.id.etTextoParaCodigo);
        ImageView imagenCodigo = view.findViewById(R.id.ivCodigoGenerado);
        Button btnGenerar = view.findViewById(R.id.btnGenerar);
        Button btnGuardar = view.findViewById(R.id.btnGuardar);

        btnGenerar.setOnClickListener(v -> {
            String texto = obtenerTextoParaCodigo();
            if (texto.isEmpty()) return;

            Bitmap bitmap = QRCode.from(texto).withSize(ANCHURA_CODIGO, ALTURA_CODIGO).bitmap();
            imagenCodigo.setImageBitmap(bitmap);
        });

        btnGuardar.setOnClickListener(v -> {
            String texto = obtenerTextoParaCodigo();
            if (texto.isEmpty()) return;

            if (!tienePermisoParaEscribir) {
                verificarYPedirPermisos();
                return;
            }

            ByteArrayOutputStream byteArrayOutputStream = QRCode.from(texto).withSize(ANCHURA_CODIGO, ALTURA_CODIGO).stream();
            try {
                File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                File file = new File(downloadsDir, "codigo_" + System.currentTimeMillis() + ".png");

                try (FileOutputStream fos = new FileOutputStream(file)) {
                    byteArrayOutputStream.writeTo(fos);
                    Toast.makeText(requireContext(), "Guardado en: " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(requireContext(), "Error al guardar: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        verificarYPedirPermisos();
    }

    private void verificarYPedirPermisos() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            tienePermisoParaEscribir = true;
        } else {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, CODIGO_PERMISO_ESCRIBIR);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CODIGO_PERMISO_ESCRIBIR) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                tienePermisoParaEscribir = true;
                Toast.makeText(requireContext(), "Permiso concedido", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(requireContext(), "No diste permiso para guardar", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
