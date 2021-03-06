package com.ksucapstone.gasandgo.AsyncTasks;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.ksucapstone.gasandgo.Models.Directions.DirectionsModel;

public class GetDirectionsAsync extends GetHttpAsync{

    private Callback callback;

    public GetDirectionsAsync(Callback callback){
        this.callback = callback;
    }

    @Override
    public void parseResponse(String jsonResponse) {

        DirectionsModel directions;

        JsonParser jsonParser = new JsonParser();
        String directionJson = jsonParser.parse(jsonResponse)
                .getAsJsonObject().get("routes")
                .getAsJsonArray().get(0)
                .getAsJsonObject().toString();
        directions = new Gson().fromJson(directionJson, DirectionsModel.class);

        String polyline = jsonParser.parse(jsonResponse)
                .getAsJsonObject().get("routes")
                .getAsJsonArray().get(0)
                .getAsJsonObject().get("overview_polyline")
                .getAsJsonObject().get("points").getAsString();
        directions.polyline = polyline;

        callback.onResponseReceived(directions);
    }

    public interface Callback{
        void onResponseReceived(DirectionsModel directions);
    }
}
