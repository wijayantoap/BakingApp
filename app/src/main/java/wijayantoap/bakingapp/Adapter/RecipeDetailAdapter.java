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

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import wijayantoap.bakingapp.Model.Baking;
import wijayantoap.bakingapp.Model.Step;
import wijayantoap.bakingapp.R;

/**
 * Created by Wijayanto A.P on 9/8/2017.
 */

public class RecipeDetailAdapter extends RecyclerView.Adapter<RecipeDetailAdapter.RecyclerViewHolder> {

    List<Step> mStep;
    private String recipeName;
    Context context;

    final private ListItemClickListener listItemClickListener;

    public interface ListItemClickListener {
        void onListItemClick(List<Step> stepsOut, int clickeditemIndex, String recipeName);
    }

    public RecipeDetailAdapter(ListItemClickListener listener) {
        listItemClickListener = listener;
    }

    public void setRecipeData(List<Baking> bakingIn, Context context) {
        mStep = bakingIn.get(0).getSteps();
        recipeName = bakingIn.get(0).getName();
        notifyDataSetChanged();
    }

    @Override
    public RecipeDetailAdapter.RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();

        int layoutDetail = R.layout.recipe_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutDetail, parent, false);

        RecyclerViewHolder viewHolder = new RecyclerViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecipeDetailAdapter.RecyclerViewHolder holder, int position) {
        holder.txtRecipe.setText(mStep.get(position).getId() + ". " + mStep.get(position).getShortDescription());
        String imageUrl = mStep.get(position).getThumbnailUrl();

        if (imageUrl != "") {
            Uri builtUri = Uri.parse(imageUrl).buildUpon().build();
            Picasso.with(context).load(builtUri).into(holder.stepThumbnail);
        }
    }

    @Override
    public int getItemCount() {
        return mStep != null ? mStep.size() : 0;
    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.txtRecipe)
        TextView txtRecipe;
        @BindView(R.id.imgThumbnail) ImageView stepThumbnail;


        public RecyclerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            listItemClickListener.onListItemClick(mStep, clickedPosition, recipeName);
        }
    }

    public void setMasterRecipeData(List<Baking> bakingIn, Context context) {
        mStep = bakingIn.get(0).getSteps();
        recipeName = bakingIn.get(0).getName();
        notifyDataSetChanged();
    }
}
