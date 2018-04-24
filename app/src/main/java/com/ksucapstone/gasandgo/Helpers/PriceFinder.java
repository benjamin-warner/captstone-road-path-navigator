package com.ksucapstone.gasandgo.Helpers;

import com.ksucapstone.gasandgo.Interfaces.IGetAveragePrice;

public class PriceFinder implements IGetAveragePrice{
    //Temp method, TODO: crawl GasBuddy for locational average
    @Override
    public double getPrice() {
        return 2.5;
    }
}
