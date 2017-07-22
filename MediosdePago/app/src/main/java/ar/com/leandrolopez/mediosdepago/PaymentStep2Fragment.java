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

import java.util.Iterator;
import java.util.List;

import ar.com.leandrolopez.mediosdepago.adapter.PaymentMethodAdapter;
import ar.com.leandrolopez.mediosdepago.network.NetworkCallback;
import ar.com.leandrolopez.mediosdepago.network.NetworkError;
import ar.com.leandrolopez.mediosdepago.network.model.PaymentMethod;
import ar.com.leandrolopez.mediosdepago.network.services.PaymentMethodServices;
import ar.com.leandrolopez.mediosdepago.ui.MercadoDialog;
import ar.com.leandrolopez.mediosdepago.viewmodel.PaymentViewModel;


/**
 * A simple {@link Fragment} subclass.
 */
public class PaymentStep2Fragment extends Fragment {

    private Button mBtnNext;
    private RecyclerView mRecycler;
    private Callback mListener;
    private PaymentMethod mPaymentMethodBundle;
    private List<PaymentMethod> mList;
    private final static String EXTRA = "Extra";

    PaymentMethodAdapter mAdapter;

    public static PaymentStep2Fragment newInstance(PaymentMethod paymentMethod) {
        PaymentStep2Fragment fragment = new PaymentStep2Fragment();

        Bundle b = new Bundle();
        b.putParcelable(EXTRA, paymentMethod);
        fragment.setArguments(b);

        return fragment;
    }

    public interface Callback {
        void onNextButtonPressed(PaymentMethod paymentMethod);
    }

    public PaymentStep2Fragment() {
        // Required empty public constructor
    }

    public void setListener(Callback listener) {
        mListener = listener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPaymentMethodBundle = getArguments().getParcelable(EXTRA);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_payment_step2, container, false);
        attachViews(v);
        callService();
        return v;
    }

    private void callService() {
        if (mList == null) {
            final ProgressDialog pd = MercadoDialog.newProgress(getActivity());
            pd.show();

            PaymentMethodServices.getPaymentMethods(getActivity(), new NetworkCallback<List<PaymentMethod>>() {
                @Override
                public void onSuccess(List<PaymentMethod> response) {
                    pd.dismiss();
                    if (response != null && response.size() > 0) {
                        //Filtro solo los de tipo credit_card
                        for (Iterator<PaymentMethod> iterator = response.listIterator(); iterator.hasNext(); ) {
                            PaymentMethod item = iterator.next();
                            if (!item.getPayment_type_id().equals("credit_card")) {
                                iterator.remove();
                            }
                        }
                        mList = response;
                        configureAdapter(response);
                    } else {
                        Toast.makeText(getActivity(), "La consulta no produjo resultados", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onError(NetworkError error) {
                    pd.dismiss();
                }
            });
        } else {
            configureAdapter(mList);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(EXTRA, mPaymentMethodBundle);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if(savedInstanceState != null) {
            mPaymentMethodBundle = savedInstanceState.getParcelable(EXTRA);
        }
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
        //Inicializo el botón si tiene seleccionado
        mBtnNext.setEnabled(mPaymentMethodBundle != null);
    }

    private void configureAdapter(List<PaymentMethod> list) {
        mAdapter = new PaymentMethodAdapter(getActivity(), list, mPaymentMethodBundle);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        mRecycler.setAdapter(mAdapter);
        mRecycler.setLayoutManager(layoutManager);
        mAdapter.setPaymentMethodListener(new PaymentMethodAdapter.PaymentMethodListener() {
            @Override
            public void onValueChanged(PaymentMethod paymentMethod) {
                mPaymentMethodBundle = paymentMethod;
                mBtnNext.setEnabled(mPaymentMethodBundle != null);
            }
        });
    }

    private void dispatchButtonNext() {
        if (mListener != null) {
            mListener.onNextButtonPressed(mAdapter.getValue());
        }
    }
}