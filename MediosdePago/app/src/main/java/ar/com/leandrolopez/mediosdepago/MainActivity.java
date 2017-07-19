package ar.com.leandrolopez.mediosdepago;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import ar.com.leandrolopez.mediosdepago.network.NetworkManager;
import ar.com.leandrolopez.mediosdepago.network.model.PaymentMethod;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        callServicePaymentMethods();
    }

    //TODO Prueba, quitar
    private void callServicePaymentMethods() {
        NetworkManager.getInstance(this)
                .call("https://api.mercadopago.com/v1/payment_methods?public_key=444a9ef5-8a6b-429f-abdf-587639155d88"
                        , PaymentMethod.class, new Response.Listener<PaymentMethod>() {
                            @Override
                            public void onResponse(PaymentMethod response) {
                                Toast.makeText(MainActivity.this,"SERVICIO OK",Toast.LENGTH_SHORT).show();
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(MainActivity.this,"Error en el servicio",Toast.LENGTH_SHORT).show();
                            }
                        });
    }
}
