package com.andras.json.json;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

/**
 * Created by Patka Zsolt-Andras on 06.01.2018.
 */
public class JsonGetterFromWeb {

    protected String url;
    protected JsonGetterFromWeb(String url){
        this.url = url;
    }
    public void setUrl(String url){
        this.url = url;
    }
    /**
     * Makes a HTTP get request and gets the text from the url
     * @return the text from the url
     * @throws IOException
     */
    protected String getJSON() throws IOException {//HTTP get request
        Request request = new Request.Builder()
                .url(url)
                .build();

        OkHttpClient client = new OkHttpClient();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }
}
