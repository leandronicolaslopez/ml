package ar.com.leandrolopez.mediosdepago;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.security.spec.PSSParameterSpec;

import ar.com.leandrolopez.mediosdepago.viewmodel.PaymentViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class PaymentStep1Fragment extends Fragment {
    
    private Button mBtnNext;
    private EditText mEditText;
    private Callback mListener;

    public interface Callback {
        void onNextButtonPressed(float monto);
    }

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
                if (actionId == EditorInfo.IME_ACTION_DONE && getMonto() > 0) {
                    dispatchNextButtonPressed();
                }
                return false;
            }
        });
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mBtnNext.setEnabled(getMonto() > 0);
            }

            @Override
            public void afterTextChanged(Editable s) {

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
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
        if (mListener != null) {
            mListener.onNextButtonPressed(getMonto());
        }
    }

    float getMonto() {
        String strValue = mEditText.getText().toString();
        try {
            if (!strValue.equals(""))
                return Float.parseFloat(strValue);
            else
                return 0;
        } catch (Exception e) {
            return 0;
        }
    }
}