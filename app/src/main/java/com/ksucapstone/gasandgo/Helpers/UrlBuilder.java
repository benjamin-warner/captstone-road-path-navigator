package com.ksucapstone.gasandgo.Helpers;


public class UrlBuilder {

    public static String BuildPlacesUrlForAddress(String address, String apiKey){
        String base = "https://maps.googleapis.com/maps/api/place/textsearch/json?";
        String addressParameter = "query=" + FormatAddress(address);

        String apiKeyParameter = "&key=" + apiKey;
        return base + addressParameter + apiKeyParameter;
    }

    public static String BuildDirectionUrl(String origin, String destination, String apiKey){
        String base = "https://maps.googleapis.com/maps/api/directions/json?";
        String originParameter = "origin=" + FormatAddress(origin);
        String destinationParameter = "&destination=" + FormatAddress(destination);

        String apiKeyParameter = "&key=" + apiKey;
        return base + originParameter + destinationParameter + apiKeyParameter;
    }

    private static String FormatAddress(String address){
        String formattedAddress = address.replaceAll("[^a-zA-Z0-9\\s]", "");
        formattedAddress = formattedAddress.replace(' ', '+');
        return formattedAddress;
    }
}
