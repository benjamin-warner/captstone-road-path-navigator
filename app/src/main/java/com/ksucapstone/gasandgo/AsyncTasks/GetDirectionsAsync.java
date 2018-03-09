package com.ksucapstone.gasandgo.AsyncTasks;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ksucapstone.gasandgo.Models.Directions.DirectionsModel;

public class GetDirectionsAsync extends GetHttpAsync{

    private Callback callback;

    GetDirectionsAsync(Callback callback){
        this.callback = callback;
    }

    @Override
    public void parseResponse(String jsonResponse) {

        DirectionsModel directions;

        JsonParser jsonParser = new JsonParser();
        JsonObject nestedDirectionObject = jsonParser.parse(jsonResponse)
                .getAsJsonObject().get("routes")
                .getAsJsonObject().getAsJsonArray("legs").get(0)
                .getAsJsonObject();
        String nestedDirectionAsJsonString = nestedDirectionObject.getAsString();
        directions = new Gson().fromJson(nestedDirectionAsJsonString, DirectionsModel.class);

        callback.onResponceReceived(directions);
    }

    public interface Callback{
        void onResponceReceived(DirectionsModel directions);
    }
}
