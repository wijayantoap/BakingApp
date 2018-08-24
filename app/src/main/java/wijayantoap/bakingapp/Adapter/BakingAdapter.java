package wijayantoap.bakingapp.Adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import wijayantoap.bakingapp.Model.Baking;
import wijayantoap.bakingapp.R;

/**
 * Created by Wijayanto A.P on 9/7/2017.
 */

public class BakingAdapter extends RecyclerView.Adapter<BakingAdapter.RecyclerViewHolder> {

    ArrayList<Baking> mBaking;
    Context mContext;
    final private ListItemClickListener listItemClickListener;

    @Override
    public BakingAdapter.RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.baking_item;

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForListItem, parent, false);

        RecyclerViewHolder viewHolder = new RecyclerViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(BakingAdapter.RecyclerViewHolder holder, int position) {
        holder.name.setText(mBaking.get(position).getName());
        String imageUrl = mBaking.get(position).getImage();

        if (imageUrl != "") {
            Uri builtUri = Uri.parse(imageUrl).buildUpon().build();
            Picasso.with(mContext).load(builtUri).into(holder.imageBaking);
        }
    }

    @Override
    public int getItemCount() {
        return mBaking != null ? mBaking.size() : 0;
    }

    public interface ListItemClickListener {
        void onListItemClick(Baking clickedItemIndex);
    }

    public BakingAdapter(ListItemClickListener listener) {
        listItemClickListener = listener;
    }

    public void setBakingData(ArrayList<Baking> bakingsIn, Context context) {
        mBaking = bakingsIn;
        mContext = context;
        notifyDataSetChanged();
    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.txtNameBaking)
        TextView name;
        @BindView(R.id.imgBaking)
        ImageView imageBaking;

        public RecyclerViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            listItemClickListener.onListItemClick(mBaking.get(clickedPosition));
        }
    }
}
