package ar.com.leandrolopez.mediosdepago.network.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Nico on 20/7/2017.
 */

public class PayerCost implements Parcelable {

    private String installment_amount;

    private String recommended_message;

    private String installments;

    public String getInstallment_amount() {
        return installment_amount;
    }

    public void setInstallment_amount(String installment_amount) {
        this.installment_amount = installment_amount;
    }

    public String getRecommended_message() {
        return recommended_message;
    }

    public void setRecommended_message(String recommended_message) {
        this.recommended_message = recommended_message;
    }

    public String getInstallments() {
        return installments;
    }

    public void setInstallments(String installments) {
        this.installments = installments;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.installment_amount);
        dest.writeString(this.recommended_message);
        dest.writeString(this.installments);
    }

    public PayerCost() {
    }

    protected PayerCost(Parcel in) {
        this.installment_amount = in.readString();
        this.recommended_message = in.readString();
        this.installments = in.readString();
    }

    public static final Parcelable.Creator<PayerCost> CREATOR = new Parcelable.Creator<PayerCost>() {
        @Override
        public PayerCost createFromParcel(Parcel source) {
            return new PayerCost(source);
        }

        @Override
        public PayerCost[] newArray(int size) {
            return new PayerCost[size];
        }
    };
}
