package ar.com.leandrolopez.mediosdepago;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

import ar.com.leandrolopez.mediosdepago.adapter.PayerCostAdapter;
import ar.com.leandrolopez.mediosdepago.network.NetworkCallback;
import ar.com.leandrolopez.mediosdepago.network.NetworkError;
import ar.com.leandrolopez.mediosdepago.network.model.CardIssuer;
import ar.com.leandrolopez.mediosdepago.network.model.InstallmentsModel;
import ar.com.leandrolopez.mediosdepago.network.model.PayerCost;
import ar.com.leandrolopez.mediosdepago.network.services.InstallmentService;
import ar.com.leandrolopez.mediosdepago.ui.MercadoDialog;


/**
 * A simple {@link Fragment} subclass.
 */
public class PaymentStep4Fragment extends Fragment {

    private String mPaymentMethodIdBundle, mCardIssuerIdBundle;
    private float mAmountBundle;
    private Button mBtnNext;
    private RecyclerView mRecycler;
    private Callback mListener;
    private PayerCost mPayerCostBundle;
    private final static String EXTRA_ISSUER = "ExtraCardIssuer", EXTRA_PAYMENT_METHOD = "ExtraPaymentMethod", EXTRA_AMOUNT = "ExtraAmount", EXTRA_PAYER_COST = "ExtraPayerCost";

    PayerCostAdapter mAdapter;

    public PaymentStep4Fragment() {
        // Required empty public constructor
    }

    public static PaymentStep4Fragment newInstance(String paymentMethodId, String cardIssuerId, float amount, PayerCost payerCost) {
        PaymentStep4Fragment frag = new PaymentStep4Fragment();

        Bundle b = new Bundle();
        b.putString(EXTRA_PAYMENT_METHOD, paymentMethodId);
        b.putString(EXTRA_ISSUER, cardIssuerId);
        b.putFloat(EXTRA_AMOUNT, amount);
        b.putParcelable(EXTRA_PAYER_COST, payerCost);

        frag.setArguments(b);

        return frag;
    }

    public interface Callback {
        void onNextButtonPressed(PayerCost payerCost);
    }

    public void setListener(Callback listener) {
        mListener = listener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getArguments();
        if (b != null) {
            mPaymentMethodIdBundle = b.getString(EXTRA_PAYMENT_METHOD);
            mCardIssuerIdBundle = b.getString(EXTRA_ISSUER);
            mAmountBundle = b.getFloat(EXTRA_AMOUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_payment_step4, container, false);
        attachViews(v);
        callService();
        return v;
    }


    private void callService() {
        final ProgressDialog pd = MercadoDialog.newProgress(getActivity());
        pd.show();

        InstallmentService.getInstallments(getActivity(), new NetworkCallback<List<InstallmentsModel>>() {
            @Override
            public void onSuccess(List<InstallmentsModel> response) {
                pd.dismiss();
                if (response != null && response.size() > 0 && response.get(0).getPayer_costs() != null && response.get(0).getPayer_costs().size() > 0) {
                    configureAdapter(response.get(0).getPayer_costs());
                } else {
                    Toast.makeText(getActivity(), "La consulta no produjo resultados", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(NetworkError error) {
                pd.dismiss();
            }
        }, mAmountBundle, mPaymentMethodIdBundle, mCardIssuerIdBundle);
    }

    private void attachViews(View v) {
        mRecycler = (RecyclerView) v.findViewById(R.id.recycler);

        mBtnNext = (Button) v.findViewById(R.id.btnNext);
        mBtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onNextButtonPressed(mPayerCostBundle);
            }
        });
        mBtnNext.setEnabled(mPayerCostBundle != null);
    }

    private void configureAdapter(List<PayerCost> list) {
        mAdapter = new PayerCostAdapter(getActivity(), list, mPayerCostBundle);

        mAdapter.setListener(new PayerCostAdapter.PayerCostListener() {
            @Override
            public void onValueChanged(PayerCost payerCost) {
                mPayerCostBundle = payerCost;
                mBtnNext.setEnabled(mPayerCostBundle != null);
            }
        });

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecycler.setAdapter(mAdapter);
        mRecycler.setLayoutManager(layoutManager);
    }
}
