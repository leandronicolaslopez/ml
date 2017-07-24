package ar.com.leandrolopez.mediosdepago;

import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import ar.com.leandrolopez.mediosdepago.network.model.CardIssuer;
import ar.com.leandrolopez.mediosdepago.network.model.PayerCost;
import ar.com.leandrolopez.mediosdepago.network.model.PaymentMethod;
import ar.com.leandrolopez.mediosdepago.viewmodel.PaymentViewModel;

public class MainActivity extends AppCompatActivity {

    private PaymentViewModel mModel;
    private ArrayList<String> mFragmentStack = new ArrayList<>();
    private String mCurrentFragmentTag;
    private boolean mSuccess;

    private static final String EXTRA_CURRENT_FRAGMENT_TAG = "ExtraCurrentFragmentTag", EXTRA_MODEL = "ExtraModel", EXTRA_STACK = "ExtraStack", EXTRA_SUCCESS = "ExtraSuccess";

    private PaymentStep1Fragment.Callback callbackStep1 = new PaymentStep1Fragment.Callback() {
        @Override
        public void onNextButtonPressed(float monto) {
            mModel.setMonto(monto);
            navigateFragmentTwo();
        }
    };

    private PaymentStep2Fragment.Callback callbackStep2 = new PaymentStep2Fragment.Callback() {
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
    };

    private PaymentStep3Fragment.Callback callbackStep3 = new PaymentStep3Fragment.Callback() {
        @Override
        public void onNextButtonPressed(CardIssuer cardIssuer) {
            //El cardIssuer cambió, hay que borrar los datos de los flows siguientes
            if (mModel.getCardIssuer() != null && cardIssuer != null && !mModel.getCardIssuer().getId().equals(cardIssuer.getId())) {
                mModel.setPayerCost(null);
            }

            mModel.setCardIssuer(cardIssuer);
            navigateFragmentFour();
        }
    };

    private PaymentStep4Fragment.Callback callbackStep4 = new PaymentStep4Fragment.Callback() {
        @Override
        public void onNextButtonPressed(PayerCost payerCost) {
            mModel.setPayerCost(payerCost);
            mSuccess = true;
            showAlert();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            mModel = new PaymentViewModel();

            navigateFragmentOne();
        } else {
            mCurrentFragmentTag = savedInstanceState.getString(EXTRA_CURRENT_FRAGMENT_TAG);
            mModel = savedInstanceState.getParcelable(EXTRA_MODEL);
            mFragmentStack = savedInstanceState.getStringArrayList(EXTRA_STACK);
            mSuccess = savedInstanceState.getBoolean(EXTRA_SUCCESS);

            //Agrego el callback a todos los fragments del stack
            for (Fragment frag : getSupportFragmentManager().getFragments()) {
                attachFragmentCallback(frag);
            }

            if (mSuccess) {
                showAlert();
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(EXTRA_CURRENT_FRAGMENT_TAG, mCurrentFragmentTag);
        outState.putParcelable(EXTRA_MODEL, mModel);
        outState.putStringArrayList(EXTRA_STACK, mFragmentStack);
        outState.putBoolean(EXTRA_SUCCESS, mSuccess);

        super.onSaveInstanceState(outState);
    }

    private void showFragment(Fragment fragment) {
        showFragment(fragment, true);
    }

    private void showFragment(Fragment fragment, boolean addToBackStack) {
        String tag = fragment.getClass().getSimpleName();
        mCurrentFragmentTag = tag;

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction tx = fm.beginTransaction();
        tx.setCustomAnimations(R.anim.anim_left_in, R.anim.anim_left_out, R.anim.anim_right_in, R.anim.anim_right_out);

        //Si el fragment ya está instanciado reutilizo la instancia.
        if (mFragmentStack.contains(tag)) {
            Fragment fg = fm.findFragmentByTag(tag);
            if (fg != null) {
                tx.show(fg);
                attachFragmentCallback(fg);
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
            attachFragmentCallback(fragment);
        } else {
            mFragmentStack.add(tag);
            tx.replace(R.id.frameContainer, fragment, tag);
            if (addToBackStack) {
                tx.addToBackStack(fragment.getClass().getSimpleName());
            } else {
                fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
            tx.commit();
            attachFragmentCallback(fragment);
        }
    }

    private void attachFragmentCallback(Fragment frag) {
        if (frag instanceof PaymentStep1Fragment) {
            ((PaymentStep1Fragment) frag).setListener(callbackStep1);
        } else if (frag instanceof PaymentStep2Fragment) {
            ((PaymentStep2Fragment) frag).setListener(callbackStep2);
        } else if (frag instanceof PaymentStep3Fragment) {
            ((PaymentStep3Fragment) frag).setListener(callbackStep3);
        } else if (frag instanceof PaymentStep4Fragment) {
            ((PaymentStep4Fragment) frag).setListener(callbackStep4);
        }
    }

    private void clearStack() {
        mFragmentStack.clear();
        getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    private void navigateFragmentOne() {
        final PaymentStep1Fragment fragment = PaymentStep1Fragment.newInstance();
        fragment.setListener(callbackStep1);
        showFragment(fragment, false);
    }

    private void navigateFragmentTwo() {
        final PaymentStep2Fragment fragment = PaymentStep2Fragment.newInstance(mModel.getPaymentMethod());
        fragment.setListener(callbackStep2);
        showFragment(fragment);
    }

    private void navigateFragmentThree() {
        final PaymentStep3Fragment fragment = PaymentStep3Fragment.newInstance(mModel.getPaymentMethod().getId(), mModel.getCardIssuer());
        fragment.setListener(callbackStep3);
        showFragment(fragment);
    }

    private void navigateFragmentFour() {
        final PaymentStep4Fragment fragment = PaymentStep4Fragment.newInstance(mModel.getPaymentMethod().getId(), mModel.getCardIssuer().getId(), mModel.getMonto(), mModel.getPayerCost());
        fragment.setListener(callbackStep4);
        showFragment(fragment);
    }

    private void showAlert() {

        final Dialog d = new Dialog(this);
        d.setContentView(R.layout.dialog_layout);
        d.setCanceledOnTouchOutside(false);

        String formattedMonto = getString(R.string.summary_amount_value, mModel.getMonto());
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
                mModel = new PaymentViewModel();
                mSuccess = false;
            }
        });

        d.show();
    }
}