package com.ksucapstone.gasandgo;

import com.google.android.gms.maps.model.LatLng;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class GasBuddyWrapper {

    public static Document GetDocumentFromUrl(String url){
        Document gasBuddyDocument = null;
        try {
            gasBuddyDocument = Jsoup.connect(url).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return gasBuddyDocument;
    }

    public static String BuildGasBuddyUrlFromLatitudeLongitude(LatLng latLng){
        String urlBase = "https://www.gasbuddy.com/home";
        String fuelTypeParameter = "&fuel=1";
        String latLongParameter = "?search=" + String.valueOf(latLng.latitude) + "%2C" + String.valueOf(latLng.longitude);
        return urlBase + latLongParameter + fuelTypeParameter;
    }
}
