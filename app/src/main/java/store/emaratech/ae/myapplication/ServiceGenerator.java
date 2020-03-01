package store.emaratech.ae.myapplication;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created By Tarun
 */
public class ServiceGenerator {

    private static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    private static JsonPlaceHolder jsonPlaceHolder = retrofit.create(JsonPlaceHolder.class);

    public static JsonPlaceHolder getJsonPlaceholder()
    {
        return jsonPlaceHolder;
    }
}
