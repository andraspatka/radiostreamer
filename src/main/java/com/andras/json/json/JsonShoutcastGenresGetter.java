package com.andras.json.json;

import com.google.gson.Gson;
import java.util.concurrent.Callable;

/**
 * Created by Patka Zsolt-Andras on 06.01.2018.
 * Gets Shoutcast stations from the specified URL. These Stations are in JSON format so they need to get parsed to
 * Java objects
 * The class' whole logic is placed inside a call method so it can be performed in a background thread.
 * Callable is used instead of Runnable because the method needs to return a value.
 */
public class JsonShoutcastGenresGetter extends JsonGetterFromWeb implements Callable<ShoutcastGenre[]> {

    public JsonShoutcastGenresGetter(String url){
        super(url);
    }

    public ShoutcastGenre[] call() throws Exception {
        StringBuilder json = new StringBuilder();

        json.append(super.getJSON());

        //Formating JSON
        while(json.charAt(0)!='['){
            json.deleteCharAt(0);
        }
        while (json.charAt(json.length()-1) != ']') {
            json.deleteCharAt(json.length()-1);
        }

        Gson gson = new Gson();

        return gson.fromJson(json.toString(), ShoutcastGenre[].class);
    }
}
