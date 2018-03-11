package com.ksucapstone.gasandgo.AsyncTasks;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.ksucapstone.gasandgo.Helpers.WebRequest;
import com.ksucapstone.gasandgo.Helpers.WebRequester;

public abstract class GetHttpAsync extends AsyncTask<String, Void, WebRequest>{

    @Override
    protected WebRequest doInBackground(String... params) {
        String url = params[0];
        WebRequester requester = new WebRequester(url);
        return requester.makeWebRequest();
    }

    @Override
    protected void onPostExecute(WebRequest response){
        parseResponse(response.ResultJson);
    }

    abstract void parseResponse(String jsonResponse);
}
