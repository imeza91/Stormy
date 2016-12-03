package com.example.israel.stormy;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String apiKey = "da7a2ab35d068950e8bb9a1c4639bc6a";
        double latitude=40.64730356;
        double longitude=74.00270462;
        String forecastUrl ="https://api.darksky.net/forecast/" + apiKey + "/" + latitude + "," + longitude;

        if (networkIsAvailable()){
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(forecastUrl)
                    .build();

            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        if (response.isSuccessful()) {
                            Log.v(TAG, response.body().string());
                        } else {
                            alertUsersAboutError();
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "Exception caught: ", e);
                    }
                }
            });
        } else{
            Toast.makeText(this, R.string.network_unavailable_message, Toast.LENGTH_LONG).show();
        }
        Log.d(TAG,"main ui code is running");
    }

    private boolean networkIsAvailable() {
        ConnectivityManager manger = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netWorkInfo = manger.getActiveNetworkInfo();
        boolean isAvailable = false;

        if (netWorkInfo != null && netWorkInfo.isConnected()){
            isAvailable = true;
        }
        return isAvailable;
    }

    private void alertUsersAboutError(){
        AlertDialogFragment dialog = new AlertDialogFragment();
        dialog.show(getFragmentManager(), "error_dialog");
    }
}
