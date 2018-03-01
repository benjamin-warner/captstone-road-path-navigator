package com.ksucapstone.gasandgo;

import com.google.android.gms.maps.model.LatLng;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GasBuddyWrapper {

    private static String GasStationDivClass = "styles__station___2iQjH";

    public static List<String> GetListOfGasPricesNearLatitudeLongitude(LatLng latLng){
        String gasBuddyUrl = BuildGasBuddyUrlFromLatitudeLongitude(latLng);
        Document gasBuddyDocument = GetDocumentFromUrl(gasBuddyUrl);
        return new ArrayList<String>();
    }

    public static Elements GetGasStationElementsFromDocument(Document document){
        return document.body().getElementsByClass(GasStationDivClass);
    }

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
