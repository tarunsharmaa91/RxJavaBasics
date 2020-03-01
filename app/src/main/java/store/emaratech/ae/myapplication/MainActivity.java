package store.emaratech.ae.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import store.emaratech.ae.myapplication.rx.DataSource;

public class MainActivity extends AppCompatActivity {


    private String TAG = "Main";

    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Disposable is to clean all observer
        //Operator convert/transform data set to observable data set(create observable)

        //FlatMap operator emit object randomly
        //getValueRandomlyFlatMap();

        //Get Posts using Rx with Retrofit
        getPosts();



        //Normal Use of Get method
        //getPosts();

        //Get method with additional parameter
        //getComments();

        //Use of Single Query
        //getCommentsWithQuery();

        //Use of Multiple Query
        //getCommentsWithMultipleQuery();

        //Above thing we can achieve using QueryMap as well
        //getCommentsWithMultipleQueryUsingQueryMap();


        // POST METHOD

        //Normal post method
        //createPost();

        //Post Using Url Encoded
        //createPostUsingUrlEncoded();

        //Post Using Url Encoded
        //createPostUsingUrlEncodedMix();

        //Put Method
        //updateDataUsingPut();

        //Put Method
        //updateDataUsingPatch();

        //Delete data
        //deleteData();

    }

    private void getValueRandomlyFlatMap() {
        Observable<Comments> commentsObservable = Observable
                //fromIterable and filter is operator
                //This is in main thread
                .fromIterable(DataSource.getCommentsWithQuery())
                .subscribeOn(Schedulers.io())
                //Filter we can use to execute some task on background thread
                .filter(comments -> {
                    //This is on background thread
                    Thread.sleep(1000);

                    //if some data available then return true and pass to Observer means on Next
                    return true;
                })
                .observeOn(AndroidSchedulers.mainThread());

        commentsObservable.subscribe(new Observer<Comments>() {
            @Override
            public void onSubscribe(Disposable d) {

                compositeDisposable.add(d);

                Log.e(TAG, "onSubscribe: called");
            }

            @Override
            public void onNext(Comments comments) {
                Log.e(TAG, "onSubscribe: called " + Thread.currentThread().getName());
                Log.e(TAG, "onSubscribe: called " + comments.getPostId());
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    private void getPosts() {
        ServiceGenerator.getJsonPlaceholder()
                .getPost()
                .subscribeOn(Schedulers.io())
                 .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<List<Posts>>() {
            @Override
            public void onSubscribe(Disposable d) {

                compositeDisposable.add(d);
            }

            @Override
            public void onNext(List<Posts> posts) {
                assert posts != null;
                for (Posts post : posts) {
                    Log.e("Response : ", post.getTitle());
                }
                //Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(MainActivity.this, "Fail", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onComplete() {
                Toast.makeText(MainActivity.this, "Completed", Toast.LENGTH_LONG).show();
            }
        });

       /* call.enqueue(new Callback<List<Posts>>() {
            @Override
            public void onResponse(@NotNull Call<List<Posts>> call, @NotNull Response<List<Posts>> response) {

                if (response.isSuccessful()) {
                    List<Posts> posts = response.body();
                    assert posts != null;
                    for (Posts post : posts) {
                        Log.e("Response : ", post.getTitle());
                    }
                    Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MainActivity.this, response.code(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<List<Posts>> call, @NotNull Throwable t) {
                Toast.makeText(MainActivity.this, "Fail", Toast.LENGTH_LONG).show();
            }
        });*/
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}
