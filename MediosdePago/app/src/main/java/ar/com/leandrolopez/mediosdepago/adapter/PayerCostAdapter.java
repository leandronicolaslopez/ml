package ar.com.leandrolopez.mediosdepago.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ar.com.leandrolopez.mediosdepago.R;
import ar.com.leandrolopez.mediosdepago.network.model.PayerCost;

/**
 * Created by Nico on 19/7/2017.
 */

public class PayerCostAdapter extends RecyclerView.Adapter<PayerCostAdapter.Holder> {
    private List<PayerCost> mList;
    private Context mContext;

    //Inicializa sin selección
    private int mSelectedIndex = -1;

    private PayerCostListener mListener;

    public PayerCost getValue() {
        return mList.get(mSelectedIndex);
    }

    public interface PayerCostListener {
        void onValueChanged(PayerCost payerCost);
    }

    public void setListener(PayerCostListener listener) {
        mListener = listener;
    }

    public PayerCostAdapter(Context ctx, List<PayerCost> list, PayerCost selected) {
        mList = list;
        if (selected != null) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getInstallments().equals(selected.getInstallments())) {
                    mSelectedIndex = i;
                    break;
                }
            }
        }

        mContext = ctx;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.payer_cost_item, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        final int pos = position;
        PayerCost item = mList.get(pos);
        holder.txtMessage.setText(item.getRecommended_message());

        if (pos == mSelectedIndex) {
            holder.rootView.setBackgroundColor(mContext.getResources().getColor(R.color.list_item_selected));
        } else {
            holder.rootView.setBackgroundColor(mContext.getResources().getColor(R.color.list_item_unselected));
        }

        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelectedIndex = pos;
                if (mListener != null) {
                    mListener.onValueChanged(mList.get(mSelectedIndex));
                }
                notifyDataSetChanged();
            }
        });
    }

    /**
     * Returns the total number of items in the data set hold by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return mList != null ? mList.size() : 0;
    }

    public class Holder extends RecyclerView.ViewHolder {

        private TextView txtMessage;
        private ViewGroup rootView;

        public Holder(View itemView) {
            super(itemView);
            txtMessage = (TextView) itemView.findViewById(R.id.txtMessage);
            rootView = (ViewGroup) itemView.findViewById(R.id.itemRootView);
        }
    }
}
