package ar.com.leandrolopez.mediosdepago;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
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

    private Button mBtnNext;
    private RecyclerView mRecycler;
    private Callback mListener;

    private String mPaymentMethodIdBundle;
    private CardIssuer mCardIssuerBundle;
    private List<CardIssuer> mList;

    private TextView mTxtEmptyState;
    private ViewGroup mLayoutData;

    private final static String EXTRA_ISSUER = "ExtraCardIssuer", EXTRA_PAYMENT_METHOD = "ExtraPaymentMethod", EXTRA_LIST = "ExtraList";

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
        if (savedInstanceState != null) {
            mCardIssuerBundle = savedInstanceState.getParcelable(EXTRA_ISSUER);
            mPaymentMethodIdBundle = savedInstanceState.getString(EXTRA_PAYMENT_METHOD);
            mList = savedInstanceState.getParcelableArrayList(EXTRA_LIST);
        }

        View v = inflater.inflate(R.layout.fragment_payment_step3, container, false);
        attachViews(v);
        callService();
        return v;
    }

    private void callService() {
        if (mList == null) {
            final ProgressDialog pd = MercadoDialog.newProgress(getActivity());
            pd.show();
            CardIssuerServices.getCardIssuers(getActivity(), new NetworkCallback<List<CardIssuer>>() {
                @Override
                public void onSuccess(List<CardIssuer> response) {
                    mList = response;

                    pd.dismiss();
                    if (response != null && response.size() > 0) {
                        configureAdapter(response);
                    } else {
                        mTxtEmptyState.setVisibility(View.VISIBLE);
                        mLayoutData.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onError(NetworkError error) {
                    pd.dismiss();
                    Toast.makeText(getActivity(), getString(R.string.default_service_error), Toast.LENGTH_SHORT).show();
                }
            }, mPaymentMethodIdBundle);
        } else {
            configureAdapter(mList);
        }
    }

    private void attachViews(View v) {
        mRecycler = (RecyclerView) v.findViewById(R.id.recycler);

        mTxtEmptyState = (TextView) v.findViewById(R.id.empty_state);
        mLayoutData = (ViewGroup) v.findViewById(R.id.layoutData);

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
        outState.putString(EXTRA_PAYMENT_METHOD, mPaymentMethodIdBundle);
        outState.putParcelableArrayList(EXTRA_LIST, new ArrayList<>(mList));

        super.onSaveInstanceState(outState);
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

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecycler.setAdapter(mAdapter);
        mRecycler.setLayoutManager(layoutManager);
    }

    private void dispatchButtonNext() {
        if (mListener != null) {
            mListener.onNextButtonPressed(mCardIssuerBundle);
        }
    }
}
