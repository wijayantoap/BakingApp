package wijayantoap.bakingapp.Activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

import wijayantoap.bakingapp.Adapter.BakingAdapter;
import wijayantoap.bakingapp.Model.Baking;
import wijayantoap.bakingapp.R;
import wijayantoap.bakingapp.EspressoTest.SimpleIdlingResource;

public class MainActivity extends AppCompatActivity implements BakingAdapter.ListItemClickListener{

    public static final String ALL_RECIPES = "All_Recipes";
    public static final String SELECTED_RECIPES = "Selected_Recipes";
    public static final String SELECTED_STEPS = "Selected_Steps";
    public static final String SELECTED_INDEX = "Selected_Index";

    @Nullable
    private SimpleIdlingResource mIdlingResource;

    /**
     * Only called from test, creates and returns a new {@link SimpleIdlingResource}.
     */
    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getIdlingResource();

    }

    @Override
    public void onListItemClick(Baking clickedItemIndex) {
        Bundle selectedBakingBundle = new Bundle();
        ArrayList<Baking> selectedBaking = new ArrayList<>();
        selectedBaking.add(clickedItemIndex);
        selectedBakingBundle.putParcelableArrayList(SELECTED_RECIPES, selectedBaking);

        final Intent intent = new Intent(this, RecipeDescriptionActivity.class);
        intent.putExtras(selectedBakingBundle);
        startActivity(intent);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

}
