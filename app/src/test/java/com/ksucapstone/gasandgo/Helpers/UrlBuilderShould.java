package com.ksucapstone.gasandgo.Helpers;

import org.junit.Test;
import static org.junit.Assert.*;

public class UrlBuilderShould {

    @Test
    public void returnAppropriateUrlForAddress() throws Exception {
        String expectedUrl = "https://maps.googleapis.com/maps/api/place/textsearch/" +
                "json?query=123+Fake+Street&key=";

        String address = "123 Fake Street";
        String apiKey = "";
        String actualUrl = UrlBuilder.BuildPlacesUrlForAddress(address, apiKey);
        assertEquals(expectedUrl, actualUrl);
    }
}
