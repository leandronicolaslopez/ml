package ar.com.leandrolopez.mediosdepago.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import ar.com.leandrolopez.mediosdepago.R;
import ar.com.leandrolopez.mediosdepago.network.model.CardIssuer;

/**
 * Created by Nico on 19/7/2017.
 */

public class CardIssuerAdapter extends RecyclerView.Adapter<CardIssuerAdapter.Holder> {
    private List<CardIssuer> mList;
    private Context mContext;

    //Inicializa sin selección
    private int mSelectedIndex = -1;

    private CardIssueListener mListener;

    public interface CardIssueListener {
        void onValueChanged(CardIssuer cardIssuer);
    }

    public void setListener(CardIssueListener listener) {
        mListener = listener;
    }

    public CardIssuerAdapter(Context ctx, List<CardIssuer> list, CardIssuer selected) {
        mList = list;
        if (selected != null) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getId().equals(selected.getId())) {
                    mSelectedIndex = i;
                    break;
                }
            }
        }

        mContext = ctx;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_text_item, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, final int position) {
        CardIssuer item = mList.get(position);
        holder.txtName.setText(item.getName());
        Picasso.with(mContext).load(item.getThumbnail()).into(holder.imgImage);

        if (position == mSelectedIndex) {
            holder.rootView.setBackgroundColor(mContext.getResources().getColor(R.color.list_item_selected));
        } else {
            holder.rootView.setBackgroundColor(mContext.getResources().getColor(R.color.list_item_unselected));
        }

        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelectedIndex = position;
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

        private TextView txtName;
        private ImageView imgImage;
        private ViewGroup rootView;

        public Holder(View itemView) {
            super(itemView);
            txtName = (TextView) itemView.findViewById(R.id.txtName);
            imgImage = (ImageView) itemView.findViewById(R.id.imgImage);
            rootView = (ViewGroup) itemView.findViewById(R.id.itemRootView);
        }
    }
}
