package com.example.android.icummenical.Activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


import com.example.android.icummenical.Classes.Adapter;
import com.example.android.icummenical.Classes.Oferta.Config;
import com.example.android.icummenical.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalPaymentDetails;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Dizimo extends AppCompatActivity {

    public static final int PAYPAL_REQUEST_CODE = 7171;

    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(Config.PAYPAL_CLIENT_ID);

private Button btn_Pay,btn_teste;
private String valores ="";
private EditText valor;
private Spinner spinner;

    @Override
    protected void onDestroy() {

        stopService(new Intent(this,PayPalService.class));
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dizimo);


        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);

        btn_teste = (Button) findViewById(R.id.btn_teste);

        spinner = (Spinner) findViewById(R.id.spinnerIgrejas);
        btn_teste.setVisibility(View.INVISIBLE);
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.Teste, android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 1) {
                    btn_teste.setVisibility(View.VISIBLE);
                } else {
                    btn_teste.setVisibility(View.INVISIBLE);
                    Toast.makeText(Dizimo.this, "Paróquia ainda não possui conta cadastrada para receber ofertas!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btn_teste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://pag.ae/7UnYvCtX7"));
                startActivity(i);
                finish();
            }
        });


//        btn_Pay.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (valor.getText().toString().trim().equals("")|| valor.length()>4) {
//                    valor.setError("Valor inválido");
//                    valor.requestFocus();
//
//
//                }else
//                processPayment();
//
//            }
//        });
//
//    }
//
//
//    private void processPayment() {
//
//
//valores = valor.getText().toString();
//        PayPalPayment payPalPayment =  new PayPalPayment(new BigDecimal(String.valueOf(valores)),"BRL","Valor Ofertado!",
//                PayPalPayment.PAYMENT_INTENT_SALE);
//        Intent intent = new Intent(this, PaymentActivity.class);
//        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
//        intent.putExtra(PaymentActivity.EXTRA_PAYMENT,payPalPayment);
//        startActivityForResult(intent,PAYPAL_REQUEST_CODE);
//
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//        if(requestCode == PAYPAL_REQUEST_CODE)
//        {
//            if(resultCode == RESULT_OK)
//            {
//                PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
//
//if(confirmation != null)
//{
//    try
//    {
//        String paymentDetails = confirmation.toJSONObject().toString(4);
//        startActivity(new Intent(this, PaymentDetails.class)
//
//                .putExtra("PaymentDetails",paymentDetails)
//                .putExtra("PaymentValor", valores)
//
//        );
//
//
//
//    } catch (JSONException e) {
//        e.printStackTrace();
//    }
//}
//else if(resultCode == Activity.RESULT_CANCELED)
//
//        Toast.makeText(this, "Cancelado", Toast.LENGTH_SHORT).show();
//
//
//            }else if(resultCode ==  PaymentActivity.RESULT_EXTRAS_INVALID)
//                Toast.makeText(this, "Valor Inválido", Toast.LENGTH_SHORT).show();
//        }
//    }
    }
}