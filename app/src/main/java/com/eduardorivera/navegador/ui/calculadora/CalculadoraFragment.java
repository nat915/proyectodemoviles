package com.eduardorivera.navegador.ui.calculadora;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.eduardorivera.navegador.R;

public class CalculadoraFragment extends Fragment {

    EditText numero1, numero2;
    Button btnSumar, btnRestar, btnMultiplicar, btnDividir;
    TextView txtResultado, txtEstado;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calculadora, container, false);

        numero1 = view.findViewById(R.id.numero1);
        numero2 = view.findViewById(R.id.numero2);
        btnSumar = view.findViewById(R.id.btnSumar);
        btnRestar = view.findViewById(R.id.btnRestar);
        btnMultiplicar = view.findViewById(R.id.btnMultiplicar);
        btnDividir = view.findViewById(R.id.btnDividir);
        txtResultado = view.findViewById(R.id.txtResultado);
        txtEstado = view.findViewById(R.id.txtEstado);

        btnSumar.setOnClickListener(v -> calcular("sumar"));
        btnRestar.setOnClickListener(v -> calcular("restar"));
        btnMultiplicar.setOnClickListener(v -> calcular("multiplicar"));
        btnDividir.setOnClickListener(v -> calcular("dividir"));

        return view;
    }

    private void calcular(String operacion) {
        try {
            double n1 = Double.parseDouble(numero1.getText().toString());
            double n2 = Double.parseDouble(numero2.getText().toString());
            double resultado = 0;

            switch (operacion) {
                case "sumar":
                    resultado = n1 + n2;
                    break;
                case "restar":
                    resultado = n1 - n2;
                    break;
                case "multiplicar":
                    resultado = n1 * n2;
                    break;
                case "dividir":
                    if (n2 != 0) {
                        resultado = n1 / n2;
                    } else {
                        txtResultado.setText("Resultado: Error");
                        txtEstado.setText("Estado: División por cero");
                        return;
                    }
                    break;
            }

            txtResultado.setText("Resultado: " + resultado);
            if (resultado < 0) {
                txtEstado.setText("Estado: Negativo");
            } else if (resultado > 0) {
                txtEstado.setText("Estado: Positivo");
            } else {
                txtEstado.setText("Estado: Cero");
            }

        } catch (NumberFormatException e) {
            txtResultado.setText("Resultado: Error");
            txtEstado.setText("Estado: Entrada inválida");
        }
    }
}
