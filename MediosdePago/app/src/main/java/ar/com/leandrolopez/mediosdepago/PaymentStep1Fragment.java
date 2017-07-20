package ar.com.leandrolopez.mediosdepago;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.security.spec.PSSParameterSpec;

import ar.com.leandrolopez.mediosdepago.viewmodel.PaymentViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class PaymentStep1Fragment extends Fragment {

    float getMonto() {
        String strValue = mEditText.getText().toString();
        if (!strValue.equals(""))
            return Float.parseFloat(strValue);
        else
            return 0;
    }

    public interface Callback {
        void onNextButtonPressed(float monto);
    }

    private Button mBtnNext;
    private EditText mEditText;
    private Callback mListener;

    public static PaymentStep1Fragment newInstance() {
        return new PaymentStep1Fragment();
    }

    public PaymentStep1Fragment() {
        // Required empty public constructor
    }

    public void setListener(Callback listener) {
        mListener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_payment_step1, container, false);

        attachViews(v);
        attachViewEvents();

        return v;
    }

    private void attachViews(View v) {
        mEditText = (EditText) v.findViewById(R.id.editMonto);
        mBtnNext = (Button) v.findViewById(R.id.btnNext);
    }

    private void attachViewEvents() {
        mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    dispatchNextButtonPressed();
                }
                return false;
            }
        });

        mBtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchNextButtonPressed();
            }
        });
    }

    private void dispatchNextButtonPressed() {
        if (mListener != null) {
            mListener.onNextButtonPressed(getMonto());
        }
    }
}