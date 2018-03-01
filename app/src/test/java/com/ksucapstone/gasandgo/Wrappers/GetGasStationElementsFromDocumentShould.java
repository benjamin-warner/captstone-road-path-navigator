package com.ksucapstone.gasandgo.Wrappers;

import com.google.android.gms.maps.model.LatLng;
import com.ksucapstone.gasandgo.GasBuddyWrapper;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Test;
import static org.junit.Assert.*;

public class GetGasStationElementsFromDocumentShould {

    @Test
    public void notReturnNoElements() throws Exception{
        Elements elements;
        LatLng kentLatLng = new LatLng(41.1500897, -81.334272);
        String url = GasBuddyWrapper.BuildGasBuddyUrlFromLatitudeLongitude(kentLatLng);
        Document document = GasBuddyWrapper.GetDocumentFromUrl(url);
        elements = GasBuddyWrapper.GetGasStationElementsFromDocument(document);
        assertFalse( elements.isEmpty() );
    }
}
