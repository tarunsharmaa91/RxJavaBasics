package store.emaratech.ae.myapplication.rx;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import store.emaratech.ae.myapplication.Comments;
import store.emaratech.ae.myapplication.JsonPlaceHolder;
import store.emaratech.ae.myapplication.MainActivity;

/**
 * Created By Tarun
 */
public class DataSource {

    public static List<Comments> getCommentsWithQuery() {


        List<Comments> comments = new ArrayList<>();
        comments.add(new Comments(1, 1,1, "first",""));
        comments.add(new Comments(2, 2,1, "second",""));
        comments.add(new Comments(3, 3,1, "third",""));

        return comments;

        /*Call<List<Comments>> call = jsonPlaceHolder.getCommentsQuery(3);
        call.enqueue(new Callback<List<Comments>>() {
            @Override
            public void onResponse(@NotNull Call<List<Comments>> call, @NotNull Response<List<Comments>> response) {

                if(response.isSuccessful())
                {
                    List<Comments> comments = response.body();
                    assert comments != null;
                    for(Comments comment : comments)
                    {
                        Log.e("Response : ", String.valueOf(comment.getPostId()));
                    }


                }else
                {

                }

            }

            @Override
            public void onFailure(@NotNull Call<List<Comments>> call, @NotNull Throwable t) {

            }
        });*/

    }
}
