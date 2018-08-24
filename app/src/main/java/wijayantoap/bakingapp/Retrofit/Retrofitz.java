package wijayantoap.bakingapp.Retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Wijayanto A.P on 9/8/2017.
 */

public class Retrofitz {

    public static final String LINK = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/";
    static BakingRecipe BR;

    public static BakingRecipe Retrieve() {
        Gson gson = new GsonBuilder().create();

        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();

        BR = new Retrofit.Builder()
                .baseUrl(LINK)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .callFactory(httpClientBuilder.build())
                .build().create(BakingRecipe.class);

        return BR;
    }
}
