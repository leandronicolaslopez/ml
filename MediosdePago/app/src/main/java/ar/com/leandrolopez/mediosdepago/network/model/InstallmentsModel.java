package ar.com.leandrolopez.mediosdepago.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nico on 20/7/2017.
 */

public class InstallmentsModel implements Parcelable {
    private List<PayerCost> payer_costs;

    public List<PayerCost> getPayer_costs() {
        return payer_costs;
    }

    public void setPayer_costs(List<PayerCost> payer_costs) {
        this.payer_costs = payer_costs;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.payer_costs);
    }

    public InstallmentsModel() {
    }

    protected InstallmentsModel(Parcel in) {
        this.payer_costs = new ArrayList<PayerCost>();
        in.readList(this.payer_costs, PayerCost.class.getClassLoader());
    }

    public static final Parcelable.Creator<InstallmentsModel> CREATOR = new Parcelable.Creator<InstallmentsModel>() {
        @Override
        public InstallmentsModel createFromParcel(Parcel source) {
            return new InstallmentsModel(source);
        }

        @Override
        public InstallmentsModel[] newArray(int size) {
            return new InstallmentsModel[size];
        }
    };
}
