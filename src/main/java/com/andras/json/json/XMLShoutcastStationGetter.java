package com.andras.json.json;

import com.google.gson.Gson;
import java.util.concurrent.Callable;

/**
 * Created by Patka Zsolt-Andras on 06.01.2018.
 * Gets Shoutcast stations from the specified URL. These Stations are in XML format so they need to get parsed to
 * Json and then to Java objects
 * The class' whole logic is placed inside a call method so it can be performed in a background thread.
 * Callable is used instead of Runnable because the method needs to return a value.
 */
public class XMLShoutcastStationGetter extends JsonGetterFromWeb implements Callable<ShoutcastStation[]>{

    public XMLShoutcastStationGetter(String url){
        super(url);
    }

    public ShoutcastStation[] call() throws Exception {
        String XML = null;

        XML = getJSON();

        StringBuilder json = new StringBuilder();
        json.append(org.json.XML.toJSONObject(XML).toString());
        //Formating JSON
        while(json.charAt(0)!='['){
            json.deleteCharAt(0);
        }
        while (json.charAt(json.length()-1) != ']') {
            json.deleteCharAt(json.length()-1);
        }
        Gson gson = new Gson();
        return gson.fromJson(json.toString(),ShoutcastStation[].class);
    }
}
