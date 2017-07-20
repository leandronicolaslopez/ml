package ar.com.leandrolopez.mediosdepago.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import ar.com.leandrolopez.mediosdepago.network.model.PaymentMethod;

/**
 * Created by Nico on 19/7/2017.
 */

public class PaymentViewModel implements Parcelable {
    private float monto;
    private PaymentMethod paymentMethod;

    public float getMonto() {
        return monto;
    }

    public void setMonto(float monto) {
        this.monto = monto;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(this.monto);
        dest.writeParcelable(this.paymentMethod, flags);
    }

    public PaymentViewModel() {
    }

    protected PaymentViewModel(Parcel in) {
        this.monto = in.readFloat();
        this.paymentMethod = in.readParcelable(PaymentMethod.class.getClassLoader());
    }

    public static final Parcelable.Creator<PaymentViewModel> CREATOR = new Parcelable.Creator<PaymentViewModel>() {
        @Override
        public PaymentViewModel createFromParcel(Parcel source) {
            return new PaymentViewModel(source);
        }

        @Override
        public PaymentViewModel[] newArray(int size) {
            return new PaymentViewModel[size];
        }
    };
}
