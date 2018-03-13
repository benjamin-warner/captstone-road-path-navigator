package com.ksucapstone.gasandgo.Helpers;

import org.jsoup.Jsoup;

public class StringHelpers {
    public static String RemoveHtmlTags(String original){
        return Jsoup.parse(original).text();
    }
}
