package com.ksucapstone.gasandgo.Wrappers;

import com.google.android.gms.maps.model.LatLng;
import com.ksucapstone.gasandgo.Interfaces.IGasStationGetter;
import com.ksucapstone.gasandgo.Models.GasStationModel;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class GasBuddyWrapper implements IGasStationGetter{

    private static String GasStationDivClass = "styles__station___2iQjH";
    private static String GasStationAddressDivClass = "styles__address___8IK98";
    private static String GasStationPriceDivClass = "styles__price___3DxO5";

    public ArrayList<GasStationModel> GetGasStationsNearLatitudeLongitude(LatLng latLng){
        String gasBuddyUrl = BuildGasBuddyUrlFromLatitudeLongitude(latLng);
        Document gasBuddyDocument = GetDocumentFromUrl(gasBuddyUrl);
        Elements gasStationElements = GetGasStationElementsFromDocument(gasBuddyDocument);
        return MapGasStationElementsToArrayList(gasStationElements);
    }

    public static ArrayList<GasStationModel> MapGasStationElementsToArrayList(Elements elements){
        ArrayList<GasStationModel> gasStations = new ArrayList<>();
        for (Element element: elements) {
            GasStationModel gasStation = new GasStationModel();
            gasStation.address = ExtractAddressFromParentElement(element);
            gasStation.price = ExtractPriceFromParentElement(element);
            gasStations.add(gasStation);
        }
        return gasStations;
    }

    public static Elements GetGasStationElementsFromDocument(Document document){
        return document.body().getElementsByClass(GasStationDivClass);
    }

    public static String ExtractAddressFromParentElement(Element element){
        return element.getElementsByClass(GasStationAddressDivClass).text();
    }

    public static double ExtractPriceFromParentElement(Element element){
        String price = element.getElementsByClass(GasStationPriceDivClass).text();
        price = price.replaceAll("[^\\d.]", "");
        return Double.valueOf(price);
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
