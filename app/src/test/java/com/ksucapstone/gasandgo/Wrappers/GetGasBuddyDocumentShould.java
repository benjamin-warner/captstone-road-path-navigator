package com.ksucapstone.gasandgo.Wrappers;

import org.jsoup.nodes.Document;
import org.junit.Test;
import static org.junit.Assert.*;

public class GetGasBuddyDocumentShould {

    @Test
    public void notReturnANullDocument() throws Exception{
        Document document = null;
        String url = "https://www.wikipedia.org";
        document = GasBuddyWrapper.GetDocumentFromUrl(url);
        assertNotNull(document);
    }
}
