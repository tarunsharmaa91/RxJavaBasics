package store.emaratech.ae.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import store.emaratech.ae.myapplication.rx.DataSource;

public class MainActivity extends AppCompatActivity {


    private String TAG = "Main";

    CompositeDisposable compositeDisposable = new CompositeDisposable();

    SearchView searchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchView = findViewById(R.id.searchView);
        //Disposable is to clean all observer
        //Operator convert/transform data set to observable data set(create observable)

        //FlatMap operator emit object randomly
        //getValueRandomlyFlatMap();

        //Get Posts using Rx with Retrofit
        //getPosts();

        //Create operator is used to create a single observable like "T converted to Observable<T>"

        //Just operator only accept upto 10 entries

        //Range operator is used if you want to iterate through the particular range of list
        //and perform some action on it on the background

        //Repeat operator when you want to repeat the range again

        //Buffer operator gather items from observable and emit them in bundle(Order is maintained)

        //Debounce operator filter out item (for search after few milli seconds) to minimize api call
        //SwitchMap to stop previous query
        debounceOperator();

        //ThrottleFirst operator for restricting button spamming(multiple clicks)

        //ConcatMap operator is same like flatMap only difference is it maintains order but with less speed

    }

    private void debounceOperator() {

        Observable<String> observableQueryText = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        if(!emitter.isDisposed())
                        {
                            emitter.onComplete();
                        }
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        if(!emitter.isDisposed())
                        {
                            emitter.onNext(newText);
                        }
                        return false;
                    }
                });
            }
        }).debounce(1000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()
                );

        observableQueryText.subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                compositeDisposable.add(d);
            }

            @Override
            public void onNext(String s) {
                Toast.makeText(MainActivity.this,s, Toast.LENGTH_SHORT ).show();
            }

            @Override
            public void onError(Throwable e) {
                //Do Nothing
            }

            @Override
            public void onComplete() {
                Toast.makeText(MainActivity.this,searchView.getQuery().toString(), Toast.LENGTH_SHORT ).show();
            }
        });
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
