package wijayantoap.bakingapp.Fragment;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import wijayantoap.bakingapp.Activity.RecipeDescriptionActivity;
import wijayantoap.bakingapp.Adapter.RecipeDetailAdapter;
import wijayantoap.bakingapp.Model.Baking;
import wijayantoap.bakingapp.Model.Ingredient;
import wijayantoap.bakingapp.R;
import wijayantoap.bakingapp.Widget.BakingAppWidgetProvider;

import static wijayantoap.bakingapp.Activity.MainActivity.SELECTED_RECIPES;

public class IngredientsFragment extends Fragment {
    public static final String TITLE = "Title";
    public static final String ADDED_TO_WIDGET_RESIZE_YOUR_WIDGET_TO_SHOW_EVERY_INGREDIENTS = "Added to widget, resize your widget to show every ingredients";
    public static final String NAME = "name";
    public static final String INGREDIENTS = "ingredients";
    private static final String BUNDLE_RECYCLER_LAYOUT = "LayoutPosition";

    ArrayList<Baking> baking;
    String recipeName;
    @BindView(R.id.recyclerViewSteps)
    RecyclerView recyclerView;
    @BindView(R.id.txtRecipes)
    TextView textView;
    @BindView(R.id.txtTitle)
    TextView textTitle;
    @BindView(R.id.widgetButton)
    FloatingActionButton addWidget;
    public static String ingredientsForWidget;
    public static String nameForWidget;
    LinearLayoutManager mLayoutManager;

    RecipeDetailAdapter mRecipeDetailAdapter;
    List<Ingredient> ingredients;

    public IngredientsFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        baking = new ArrayList<>();
        View rootView = inflater.inflate(R.layout.fragment_ingredients, container, false);
        ButterKnife.bind(this, rootView);
        mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);

        if (savedInstanceState != null) {
            baking = savedInstanceState.getParcelableArrayList(SELECTED_RECIPES);
            Parcelable state = savedInstanceState.getParcelable(BUNDLE_RECYCLER_LAYOUT);
            mLayoutManager.onRestoreInstanceState(state);
        } else {
            baking = getArguments().getParcelableArrayList(SELECTED_RECIPES);
        }

        ingredients = baking.get(0).getIngredients();
        recipeName = baking.get(0).getName();

        textTitle.setText(recipeName);
        final int[] b = {1};
        StringBuilder stringBuilder = new StringBuilder(100);

        ingredients.forEach((a) ->
        {
            // set the ingredients list for layout
            textView.append(String.valueOf(b[0]) + ". ");
            textView.append(a.getIngredient() + "\n    ");
            textView.append("~ " + a.getQuantity().toString() + " ");
            textView.append(a.getMeasure() + ",\n");

            // set the ingredients list for widget
            stringBuilder.append(String.valueOf(b[0]) + ". ");
            stringBuilder.append(a.getIngredient() + " ");
            stringBuilder.append("~ " + a.getQuantity().toString() + " ");
            stringBuilder.append(a.getMeasure() + ",\n");

            b[0]++;
        });

        nameForWidget = textTitle.getText().toString();
        ingredientsForWidget = stringBuilder.toString();

        mRecipeDetailAdapter = new RecipeDetailAdapter((RecipeDescriptionActivity) getActivity());
        recyclerView.setAdapter(mRecipeDetailAdapter);
        mRecipeDetailAdapter.setMasterRecipeData(baking, getContext());

        // update widget
        addWidget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(INGREDIENTS, ingredientsForWidget);
                editor.putString(NAME, nameForWidget);
                editor.commit();

                int[] ids = AppWidgetManager.getInstance(getContext().getApplicationContext()).getAppWidgetIds(new ComponentName(getContext().getApplicationContext(), BakingAppWidgetProvider.class));
                BakingAppWidgetProvider myWidget = new BakingAppWidgetProvider();
                myWidget.onUpdate(getActivity(), AppWidgetManager.getInstance(getActivity()), ids);

                Toast.makeText(getActivity(), ADDED_TO_WIDGET_RESIZE_YOUR_WIDGET_TO_SHOW_EVERY_INGREDIENTS, Toast.LENGTH_LONG).show();
            }
        });
        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle currentState) {
        super.onSaveInstanceState(currentState);
        currentState.putParcelableArrayList(SELECTED_RECIPES, baking);
        currentState.putString(TITLE, recipeName);
        currentState.putParcelable(BUNDLE_RECYCLER_LAYOUT, mLayoutManager.onSaveInstanceState());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            Parcelable state = savedInstanceState.getParcelable(BUNDLE_RECYCLER_LAYOUT);
            mLayoutManager.onRestoreInstanceState(state);
        }
    }
}
