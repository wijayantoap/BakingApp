package wijayantoap.bakingapp.Activity;

import android.os.Parcelable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import wijayantoap.bakingapp.Adapter.RecipeDetailAdapter;
import wijayantoap.bakingapp.Fragment.IngredientsFragment;
import wijayantoap.bakingapp.Fragment.StepFragment;
import wijayantoap.bakingapp.Model.Baking;
import wijayantoap.bakingapp.Model.Step;
import wijayantoap.bakingapp.R;

public class RecipeDescriptionActivity extends AppCompatActivity implements RecipeDetailAdapter.ListItemClickListener {

    public static final String TITLE = "Title";
    public static final String PRESS_FAB_TO_RETURN = "Press FAB to return";
    public static String SELECTED_RECIPES = "Selected_Recipes";
    public static String SELECTED_STEPS = "Selected_Steps";
    public static String SELECTED_INDEX = "Selected_Index";

    private ArrayList<Baking> baking;
    public String recipeName;
    IngredientsFragment fragmentIngredients;
    StepFragment fragmentStep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_description);

        if (savedInstanceState == null) {
            Bundle selectedRecipeBundle = getIntent().getExtras();

            baking = new ArrayList<>();
            baking = selectedRecipeBundle.getParcelableArrayList(SELECTED_RECIPES);
            recipeName = baking.get(0).getName();

            fragmentIngredients = new IngredientsFragment();

            fragmentIngredients.setArguments(selectedRecipeBundle);
            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.frameDetail, fragmentIngredients)
                    //.addToBackStack(SELECTED_RECIPES)
                    .commit();
        } else {
            recipeName = savedInstanceState.getString(TITLE);
            baking = savedInstanceState.getParcelableArrayList(SELECTED_RECIPES);
        }
    }

    @Override
    public void onListItemClick(List<Step> stepsOut, int clickeditemIndex, String recipeName) {
        fragmentStep = new StepFragment();
        Bundle stepBundle = new Bundle();
        stepBundle.putParcelableArrayList(SELECTED_STEPS, (ArrayList<Step>) stepsOut);
        stepBundle.putInt(SELECTED_INDEX, clickeditemIndex);
        stepBundle.putString(TITLE, recipeName);
        fragmentStep.setArguments(stepBundle);

        //check the screen size
        if (getResources().getConfiguration().smallestScreenWidthDp >= 600) { // if using tablet
            android.support.v4.app.FragmentManager fragmentManager2 = getSupportFragmentManager();
            fragmentManager2.beginTransaction()
                    .replace(R.id.frameDetail2, fragmentStep) // replace the second framelayout from sw600dp layout
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .addToBackStack(null)
                    .commit();
        } else { // normal phone
            android.support.v4.app.FragmentManager fragmentManager2 = getSupportFragmentManager();
            fragmentManager2.beginTransaction()
                    .replace(R.id.frameDetail, fragmentStep) // use only one fragment
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .addToBackStack(null)
                    //.hide(fragmentIngredients)
                    .commit();
            Toast.makeText(getApplicationContext(), PRESS_FAB_TO_RETURN, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) { // if there's any backstack
            getFragmentManager().popBackStack(); // shows the previous fragment
        } else {
            super.onBackPressed();
        }
    }
}
