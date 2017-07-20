package ar.com.leandrolopez.mediosdepago;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import ar.com.leandrolopez.mediosdepago.network.NetworkCallback;
import ar.com.leandrolopez.mediosdepago.network.NetworkError;
import ar.com.leandrolopez.mediosdepago.network.NetworkManager;
import ar.com.leandrolopez.mediosdepago.network.model.PaymentMethod;
import ar.com.leandrolopez.mediosdepago.network.services.PaymentMethodServices;
import ar.com.leandrolopez.mediosdepago.viewmodel.PaymentViewModel;

public class MainActivity extends AppCompatActivity {

    private PaymentViewModel mModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mModel = new PaymentViewModel();

        navigateFragmentOne();
    }

    private void showFragment(Fragment fragment) {
        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        tx.replace(R.id.frameContainer, fragment, fragment.getClass().getSimpleName());
        tx.addToBackStack(fragment.getClass().getSimpleName()).commit();
    }

    private void navigateFragmentOne() {
        final PaymentStep1Fragment fragment = PaymentStep1Fragment.newInstance();
        fragment.setListener(new PaymentStep1Fragment.Callback() {
            @Override
            public void onNextButtonPressed(float monto) {
                mModel.setMonto(monto);
                navigateFragmentTwo();
            }
        });
        showFragment(fragment);
    }

    private void navigateFragmentTwo() {
        final PaymentStep2Fragment fragment = PaymentStep2Fragment.newInstance(mModel.getPaymentMethod());
        fragment.setListener(new PaymentStep2Fragment.Callback() {
            @Override
            public void onNextButtonPressed() {
                //mModel.setPaymentMethod(paymentMethod);
                Toast.makeText(MainActivity.this, "Go step 3", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onBeforeLeave(PaymentMethod paymentMethod) {
                mModel.setPaymentMethod(paymentMethod);
            }
        });
        showFragment(fragment);
    }
}