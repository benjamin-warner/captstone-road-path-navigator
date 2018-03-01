package com.ksucapstone.gasandgo.AsyncTasks;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.ksucapstone.gasandgo.Helpers.WebRequest;
import com.ksucapstone.gasandgo.Helpers.WebRequester;

public class GetHttpAsync extends AsyncTask<String, Void, WebRequest>{

    private ResponseCallback callback;
    private Class responseClass;

    public GetHttpAsync(Class expectedResponseClass, ResponseCallback responseCallback ){
        responseClass = expectedResponseClass;
        callback = responseCallback;
    }

    @Override
    protected WebRequest doInBackground(String... params) {
        String url = params[0];
        WebRequester requester = new WebRequester(url);
        return requester.makeWebRequest();
    }

    @Override
    protected void onPostExecute(WebRequest response){
        callback.onResponseReceived(new Gson().fromJson(response.ResultJson, responseClass));
    }


    public interface ResponseCallback<T> {
        void onResponseReceived(T obj);
    }
}
