package com.ksucapstone.gasandgo.Helpers;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WebRequestHelper {


    HttpURLConnection openConnectionFromUrl(String url) throws IOException
    {
        URL connectionUrl = new URL(url);
        return (HttpURLConnection) connectionUrl.openConnection();
    }

    String getResultJsonFromUrlConnection(HttpURLConnection urlConnection) throws IOException
    {
        String json = "";
        InputStream in = new BufferedInputStream(urlConnection.getInputStream());
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String line;
        while ((line = reader.readLine()) != null) {
            json += line;
        }

        return json;
    }

    void Disconnect(HttpURLConnection urlConnection)
    {
        if (urlConnection != null)
        {
            urlConnection.disconnect();
        }
    }

}
