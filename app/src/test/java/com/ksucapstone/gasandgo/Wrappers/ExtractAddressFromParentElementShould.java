package com.ksucapstone.gasandgo.Wrappers;

import com.google.android.gms.maps.model.LatLng;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExtractAddressFromParentElementShould {

    @Test
    public void ReturnNonEmptyAddressAsString() throws Exception {
        LatLng kentLatLng = new LatLng(41.1500897, -81.334272);
        String url = GasBuddyWrapper.BuildGasBuddyUrlFromLatitudeLongitude(kentLatLng);
        Document document = GasBuddyWrapper.GetDocumentFromUrl(url);
        Elements elements = GasBuddyWrapper.GetGasStationElementsFromDocument(document);

        String elementText = GasBuddyWrapper.ExtractAddressFromParentElement(elements.first());
        assertNotNull(elementText);
    }
}
