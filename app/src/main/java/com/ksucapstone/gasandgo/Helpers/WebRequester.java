package com.ksucapstone.gasandgo.Helpers;

import java.net.HttpURLConnection;


public class WebRequester {

    private WebRequestHelper _webRequestHelper;
    private String _url;

    public WebRequester(String url)
    {
        _url = url;
        _webRequestHelper = new WebRequestHelper();
    }

    public WebRequest makeWebRequest(){
        WebRequest webRequest = new WebRequest();
        String resultJson;
        HttpURLConnection urlConnection = null;

        try
        {
            urlConnection = _webRequestHelper.openConnectionFromUrl(_url);

            resultJson = _webRequestHelper.getResultJsonFromUrlConnection(urlConnection);

            webRequest.ResultJson = resultJson;
            webRequest.Success = true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            webRequest.Success = false;
            return webRequest;
        }
        finally
        {
            _webRequestHelper.Disconnect(urlConnection);
        }

        return webRequest;
    }
}
