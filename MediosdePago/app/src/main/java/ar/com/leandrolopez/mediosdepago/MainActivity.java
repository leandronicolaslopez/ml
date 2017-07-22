package ar.com.leandrolopez.mediosdepago;

import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import ar.com.leandrolopez.mediosdepago.network.NetworkCallback;
import ar.com.leandrolopez.mediosdepago.network.NetworkError;
import ar.com.leandrolopez.mediosdepago.network.NetworkManager;
import ar.com.leandrolopez.mediosdepago.network.model.CardIssuer;
import ar.com.leandrolopez.mediosdepago.network.model.PayerCost;
import ar.com.leandrolopez.mediosdepago.network.model.PaymentMethod;
import ar.com.leandrolopez.mediosdepago.network.services.PaymentMethodServices;
import ar.com.leandrolopez.mediosdepago.viewmodel.PaymentViewModel;

public class MainActivity extends AppCompatActivity {

    private PaymentViewModel mModel;
    private List<String> mFragmentStack = new Stack<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mModel = new PaymentViewModel();

        navigateFragmentOne();
    }

    private void showFragment(Fragment fragment) {
        showFragment(fragment, true);
    }

    private void showFragment(Fragment fragment, boolean addToBackStack) {
        String tag = fragment.getClass().getSimpleName();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction tx = fm.beginTransaction();
        tx.setCustomAnimations(R.anim.anim_left_in, R.anim.anim_left_out, R.anim.anim_right_in, R.anim.anim_right_out);
        if (mFragmentStack.contains(tag)) {
            Fragment fg = fm.findFragmentByTag(tag);
            if (fg != null) {
                tx.show(fg);
            } else {
                mFragmentStack.add(tag);
                tx.replace(R.id.frameContainer, fragment, tag);
                if (addToBackStack) {
                    tx.addToBackStack(fragment.getClass().getSimpleName());
                } else {
                    fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }
            }
            tx.commitAllowingStateLoss();
        } else {
            mFragmentStack.add(tag);
            tx.replace(R.id.frameContainer, fragment, tag);
            if (addToBackStack) {
                tx.addToBackStack(fragment.getClass().getSimpleName());
            } else {
                fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
            tx.commit();
        }
    }

    private void clearStack() {
        mFragmentStack.clear();
        getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
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
        showFragment(fragment, false);
    }

    private void navigateFragmentTwo() {
        final PaymentStep2Fragment fragment = PaymentStep2Fragment.newInstance(mModel.getPaymentMethod());
        fragment.setListener(new PaymentStep2Fragment.Callback() {
            @Override
            public void onNextButtonPressed(PaymentMethod paymentMethod) {
                //El paymentMethod cambió, hay que borrar los datos de los flows siguientes
                if (mModel.getPaymentMethod() != null && paymentMethod != null && !mModel.getPaymentMethod().getId().equals(paymentMethod.getId())) {
                    mModel.setCardIssuer(null);
                    mModel.setPayerCost(null);
                }
                mModel.setPaymentMethod(paymentMethod);
                navigateFragmentThree();
            }
        });
        showFragment(fragment);
    }

    private void navigateFragmentThree() {
        final PaymentStep3Fragment fragment = PaymentStep3Fragment.newInstance(mModel.getPaymentMethod().getId(), mModel.getCardIssuer());
        fragment.setListener(new PaymentStep3Fragment.Callback() {
            @Override
            public void onNextButtonPressed(CardIssuer cardIssuer) {
                mModel.setCardIssuer(cardIssuer);
                mModel.setPayerCost(null);
                navigateFragmentFour();
            }
        });
        showFragment(fragment);
    }

    private void navigateFragmentFour() {
        final PaymentStep4Fragment fragment = PaymentStep4Fragment.newInstance(mModel.getPaymentMethod().getId(), mModel.getCardIssuer().getId(), mModel.getMonto(), mModel.getPayerCost());
        fragment.setListener(new PaymentStep4Fragment.Callback() {
            @Override
            public void onNextButtonPressed(PayerCost payerCost) {
                mModel.setPayerCost(payerCost);
                showAlert();
            }
        });
        showFragment(fragment);
    }

    private void showAlert() {

        final Dialog d = new Dialog(this);
        d.setContentView(R.layout.dialog_layout);
        d.setCanceledOnTouchOutside(false);

        String formattedMonto = getString(R.string.summary_amount, mModel.getMonto());
        ((TextView) d.findViewById(R.id.txtMonto)).setText(formattedMonto);

        ((TextView) d.findViewById(R.id.txtMedioPago)).setText(mModel.getPaymentMethod().getName());
        ((TextView) d.findViewById(R.id.txtBanco)).setText(mModel.getCardIssuer().getName());
        ((TextView) d.findViewById(R.id.txtCuotas)).setText(mModel.getPayerCost().getRecommended_message());
        d.findViewById(R.id.btnOk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });
        d.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                clearStack();
                navigateFragmentOne();
            }
        });

        d.show();
    }
}