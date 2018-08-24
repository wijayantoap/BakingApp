package wijayantoap.bakingapp.Retrofit;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import wijayantoap.bakingapp.Model.Baking;

/**
 * Created by Wijayanto A.P on 9/8/2017.
 */

public interface BakingRecipe {
    @GET("baking.json")
    Call<ArrayList<Baking>> getBaking();
}
