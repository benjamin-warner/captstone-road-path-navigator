package com.ksucapstone.gasandgo.Helpers;


public class UrlBuilder {

    public static String BuildPlacesUrlForAddress(String address, String apiKey){
        String base = "https://maps.googleapis.com/maps/api/place/textsearch/json?";
        address = address.replaceAll("[^a-zA-Z0-9\\s]", "");
        address = address.replace(' ', '+');
        String addressParameter = "query=" + address;

        String apiKeyParameter = "&key=" + apiKey;
        return base + addressParameter + apiKeyParameter;
    }
}
