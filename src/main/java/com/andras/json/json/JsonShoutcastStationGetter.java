package com.andras.json.json;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.concurrent.Callable;

/**
 * Created by Patka Zsolt-Andras on 06.01.2018.
 * This class takes a url (a http get request from the Shoutcast database), downloads the JSON array
 * and parses it to a Java ShoutcastStation array.
 * It implements the Callable interface because the background thread needs to return an object
 */
public class JsonShoutcastStationGetter extends JsonGetterFromWeb implements Callable<ShoutcastStation[]>{
    public JsonShoutcastStationGetter(String url){
        super(url);
    }

    /**
     * call method
     * Calls super's getJSON method, which returns the JSON file from the url
     * Formats the JSON file, extracts the Shoutcast object array and parses it using Gson
     * When it finishes it returns the ShoutcastStation array
     */
    public ShoutcastStation[] call() throws Exception {
        StringBuilder json = new StringBuilder();

        //Calls super's getJson method.
        json.append(super.getJSON());

        //Formating JSON
        while(json.charAt(0)!='['){
            json.deleteCharAt(0);
        }
        while (json.charAt(json.length()-1) != ']') {
            json.deleteCharAt(json.length()-1);
        }

        Gson gson = new Gson();
        return  gson.fromJson(json.toString(),ShoutcastStation[].class);
    }
}
