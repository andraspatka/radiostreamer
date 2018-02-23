/**
 * @author: Jason K. Jackson
 * @since: 2013-03-08
 * @version: 1.0
 * Class: NJIT CS-602
 * Professor: George Blank
 * Final Project - MyNPR Applet
 * **/

package com.andras.json.audio;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import com.andras.json.GUI.MainApplication;
import com.sun.media.jfxmedia.MediaPlayer;
import javafx.scene.media.Media;
import javazoom.jl.player.Player;

/**
 * MP3AudioPlayer is a simple MP3 audio player that will handle MP3 files or
 * M3U files. The actual player is JLayer created by javazoom and included as a
 * jar file. http://sourceforge.net/projects/javalayer/
 *
 * Modified by: Patka Zsolt-Andras
 * on 26.11.2017
 * Modified method name: close() -> stopPlayback()
 * Added method: changeStream
 * Removed M3U compatibility
 * Added method: M3UtoURL
 */
public class MP3AudioPlayer extends Thread{
    private String streamURL;
    private Player player;
    private boolean isPlaying;

    /**
     * constructor to take a url and create a new instance of the JLayer MP3 player.
     * @param streamURL url to the MP3 stream.
     */
    public MP3AudioPlayer(String streamURL){
        this.streamURL = M3UtoURL(streamURL);
    }

    public MP3AudioPlayer(){}

    /**
     * Extracts the mp3 stream from the M3U URL
     * @param streamURL gets the M3U URL
     * @return mp3 stream url
     */
    private String M3UtoURL(String streamURL){
        String url;
        try{
            Scanner scanner = new Scanner(new URL(streamURL).openStream());
            while(scanner.hasNext()){
                url = scanner.nextLine();
                if(url.contains("http://") || (url.contains("https://"))){
                    return url;
                }
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return null;
    }
    /**
     * Changes the audio stream
     * @param streamURL
     */
    public void changeStream(String streamURL){
        this.streamURL = M3UtoURL(streamURL);
    }

    /**
     * Stop the audio player
     */
    public void stopPlayback(){
        if(player != null){
            player.close();
        }
        isPlaying = false;
    }
    /**
     * Play the audio of the defined MP3 stream
     */
    public void startPlayback() throws Exception{
        URL url;

        //Create a buffered input stream from the url
        url = new URL(streamURL);
        InputStream stream = url.openStream();
        InputStream bufferedStream = new BufferedInputStream(stream);
        player = new Player(bufferedStream);

        MainApplication.executor.submit(this);
    }
    public boolean getPlayerState(){
        return this.isPlaying;
    }

    public void run() {
        try{
            isPlaying = true;
            player.play();
        }catch(Exception e){
            System.out.println("Error starting player: "+e.getMessage());
        }
    }
}
