package ar.com.leandrolopez.mediosdepago;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

import ar.com.leandrolopez.mediosdepago.adapter.CardIssuerAdapter;
import ar.com.leandrolopez.mediosdepago.network.NetworkCallback;
import ar.com.leandrolopez.mediosdepago.network.NetworkError;
import ar.com.leandrolopez.mediosdepago.network.model.CardIssuer;
import ar.com.leandrolopez.mediosdepago.network.model.PaymentMethod;
import ar.com.leandrolopez.mediosdepago.network.services.CardIssuerServices;
import ar.com.leandrolopez.mediosdepago.ui.MercadoDialog;


/**
 * A simple {@link Fragment} subclass.
 */
public class PaymentStep3Fragment extends Fragment {

    private String mPaymentMethodIdBundle;
    private Button mBtnNext;
    private RecyclerView mRecycler;
    private Callback mListener;
    private CardIssuer mCardIssuerBundle;
    private final static String EXTRA_ISSUER = "ExtraCardIssuer", EXTRA_PAYMENT_METHOD = "ExtraPaymentMethod";

    CardIssuerAdapter mAdapter;

    public static PaymentStep3Fragment newInstance(String paymentMethodId, CardIssuer cardIssuer) {
        PaymentStep3Fragment frag = new PaymentStep3Fragment();

        Bundle b = new Bundle();
        b.putString(EXTRA_PAYMENT_METHOD, paymentMethodId);
        b.putParcelable(EXTRA_ISSUER, cardIssuer);
        frag.setArguments(b);

        return frag;
    }

    public interface Callback {
        void onNextButtonPressed(CardIssuer cardIssuer);
    }

    public PaymentStep3Fragment() {
        // Required empty public constructor
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
            mCardIssuerBundle = b.getParcelable(EXTRA_ISSUER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_payment_step3, container, false);
        attachViews(v);
        callService();
        return v;
    }

    private void callService() {
        final ProgressDialog pd = MercadoDialog.newProgress(getActivity());
        pd.show();
        CardIssuerServices.getCardIssuers(getActivity(), new NetworkCallback<List<CardIssuer>>() {
            @Override
            public void onSuccess(List<CardIssuer> response) {
                pd.dismiss();
                if (response != null && response.size() > 0) {
                    configureAdapter(response);
                } else {
                    Toast.makeText(getActivity(), "La consulta no produjo resultados", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(NetworkError error) {
                pd.dismiss();
            }
        }, mPaymentMethodIdBundle);
    }

    private void attachViews(View v) {
        mRecycler = (RecyclerView) v.findViewById(R.id.recycler);

        mBtnNext = (Button) v.findViewById(R.id.btnNext);
        mBtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchButtonNext();
            }
        });
        mBtnNext.setEnabled(mCardIssuerBundle != null);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(EXTRA_ISSUER, mCardIssuerBundle);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            Bundle b = new Bundle();
            mCardIssuerBundle = b.getParcelable(EXTRA_ISSUER);
        }
    }

    private void configureAdapter(List<CardIssuer> list) {
        mAdapter = new CardIssuerAdapter(getActivity(), list, mCardIssuerBundle);

        mAdapter.setListener(new CardIssuerAdapter.CardIssueListener() {
            @Override
            public void onValueChanged(CardIssuer cardIssuer) {
                mCardIssuerBundle = cardIssuer;
                mBtnNext.setEnabled(mCardIssuerBundle != null);
            }
        });

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        mRecycler.setAdapter(mAdapter);
        mRecycler.setLayoutManager(layoutManager);
    }

    private void dispatchButtonNext() {
        if (mListener != null)
            mListener.onNextButtonPressed(mAdapter.getValue());
    }
}
