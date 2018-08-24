package wijayantoap.bakingapp.Fragment;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import wijayantoap.bakingapp.Activity.MainActivity;
import wijayantoap.bakingapp.Adapter.BakingAdapter;
import wijayantoap.bakingapp.Retrofit.BakingRecipe;
import wijayantoap.bakingapp.Model.Baking;
import wijayantoap.bakingapp.R;
import wijayantoap.bakingapp.Retrofit.Retrofitz;
import wijayantoap.bakingapp.EspressoTest.SimpleIdlingResource;

import static wijayantoap.bakingapp.Activity.MainActivity.ALL_RECIPES;

public class MainFragment extends Fragment {

    public static final String HTTP_FAIL = "http fail: ";
    public static final String STATUS_CODE = "status code: ";
    public static final String FAILURE = "No internet, cannot load the data";

    @BindView(R.id.recyclerViewMain)
    RecyclerView recyclerView;

    public MainFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);
        final BakingAdapter bakingAdapter = new BakingAdapter((MainActivity) getActivity());
        recyclerView.setAdapter(bakingAdapter);

        if (getResources().getConfiguration().smallestScreenWidthDp >= 600
                || getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) { // if tablet or landscape
            GridLayoutManager mLayoutManager = new GridLayoutManager(getContext(), 4); // shows the recyclerview as a grid list
            recyclerView.setLayoutManager(mLayoutManager);
        } else { // normal phone or portrait
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext()); // shows the recyclerview as a linear list
            recyclerView.setLayoutManager(mLayoutManager);
        }

        BakingRecipe bakingRecipe = Retrofitz.Retrieve();
        final retrofit2.Call<ArrayList<Baking>> baking = bakingRecipe.getBaking();

        SimpleIdlingResource idlingResource = (SimpleIdlingResource)((MainActivity)getActivity()).getIdlingResource();

        if (idlingResource != null) {
            idlingResource.setIdleState(false);
        }

        baking.enqueue(new Callback<ArrayList<Baking>>() {
            @Override
            public void onResponse(Call<ArrayList<Baking>> call, Response<ArrayList<Baking>> response) {
                Integer statusCode = response.code();
                Log.v(STATUS_CODE, statusCode.toString());

                ArrayList<Baking> bakings = response.body();

                Bundle bakingBundle = new Bundle();
                bakingBundle.putParcelableArrayList(ALL_RECIPES, bakings);

                bakingAdapter.setBakingData(bakings, getContext());

                if (idlingResource != null) {
                    idlingResource.setIdleState(true);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Baking>> call, Throwable t) {
                Log.v(HTTP_FAIL, t.getMessage());
                Toast.makeText(getActivity(), FAILURE, Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
