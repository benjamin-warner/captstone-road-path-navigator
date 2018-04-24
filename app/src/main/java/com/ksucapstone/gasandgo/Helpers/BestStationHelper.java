package com.ksucapstone.gasandgo.Helpers;

import com.ksucapstone.gasandgo.Models.GasStationModel;

import java.util.ArrayList;

public class BestStationHelper {

    public static GasStationModel findBestGasStation(ArrayList<GasStationModel> list, double MPG)
    {
        GasStationModel lowestExtraCostStation = new GasStationModel();
        int iter = 0;
        double cost = 0;

        for (GasStationModel station : list)
        {
            double result = (station.distance / MPG) * station.price;

            if (iter == 0)
            {
                cost = result;
                lowestExtraCostStation = station;
            }
            else
            {
                if (result < cost)
                {
                    lowestExtraCostStation = station;
                    cost = result;
                }
            }
            iter++;
        }
        return lowestExtraCostStation;
    }

    public static double findAveragePrice(ArrayList<GasStationModel> list) {
        double avg = 0;

        for (GasStationModel station : list)
        {
            avg += station.price;
        }
        avg /= list.size();
        return Math.round(avg * 100.0) / 100.0;
    }

    private static GasStationModel findCheapest(ArrayList<GasStationModel> list) {
        GasStationModel cheapestStation = new GasStationModel();

        for (GasStationModel station : list)
        {
            if (cheapestStation.price < station.price)
                cheapestStation = station;
        }
        return cheapestStation;
    }



}