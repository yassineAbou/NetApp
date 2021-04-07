package com.example.netapp.Utils;

import android.os.AsyncTask;
import android.util.Log;

import java.lang.ref.WeakReference;

/**
 * Created by Yassine Abou on 3/30/2021.
 */
public class NetworkAsyncTask extends AsyncTask<String, Void, String> {



    public interface Listeners {
        void onPreExecute();
        void doInBackground();
        void onPostExecute(String success);

    }

   private final WeakReference<Listeners> callBack;

    public NetworkAsyncTask(Listeners callBack) {
        this.callBack = new WeakReference<>(callBack);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        this.callBack.get().onPreExecute();
        Log.e("TAG", "AsyncTask is started.");
    }

    @Override
    protected void onPostExecute(String success) {
        super.onPostExecute(success);
        this.callBack.get().onPostExecute(success);
        Log.e("TAG", "AsyncTask is finished.");
    }

    @Override
    protected String doInBackground(String... url) {
        this.callBack.get().doInBackground();
        Log.e("TAG", "AsyncTask doing some big work...");
        return MyHttpURLConnection.startHttpRequest(url[0]);
    }
}
