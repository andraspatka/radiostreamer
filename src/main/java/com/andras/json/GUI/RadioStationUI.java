package com.andras.json.GUI;

import com.andras.json.audio.MP3AudioPlayer;
import com.andras.json.json.*;

import com.sun.javafx.scene.control.skin.TableColumnHeader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.image.ImageView;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.function.Predicate;

/**
 * Created by Patka Zsolt-Andras on 08.12.2017.
 * Controller class
 * Uses RadioStationUI.fxml as the layout
 */
public class RadioStationUI implements Initializable{
    private final String SHOUTCASTDEVID = "<insertYourShoutcastDevIdHere>";
    private final String TUNEIN = "http://yp.shoutcast.com/sbin/tunein-station.m3u?id=";
    private final String RANDOMSTATIONSURL = "http://api.shoutcast.com/station/randomstations?k="+ SHOUTCASTDEVID +"&f=json&limit=200&mt=audio/mpeg";
    private final String ALLGENRESURL = "http://api.shoutcast.com/genre/primary?k="+SHOUTCASTDEVID+"&f=json";
    private final String STATIONSBYGENREURL = "http://api.shoutcast.com/legacy/genresearch?k="+SHOUTCASTDEVID+"&genre=";

    private MP3AudioPlayer player = new MP3AudioPlayer();

    private Image playImage = new Image(new File("pic/playS.png").toURI().toString());
    private Image pauseImage = new Image(new File("pic/pauseS.png").toURI().toString());
    @FXML
    public ImageView playbackImage;
    @FXML
    public ListView<ShoutcastGenre>genresList;
    @FXML
    public TableView<ShoutcastStation> stationTableView;
    @FXML
    public TableColumn<ShoutcastStation, String> stationColumn;
    @FXML
    public TableColumn<ShoutcastStation, String> genreColumn;
    @FXML
    public TextField stationNameTextField;
    @FXML
    public TextField genreTextField;

    public void initialize(URL location, ResourceBundle resources) {
        //Set an image to the ImageView
        playbackImage.setImage(playImage);
        //Initialize Station List
        //Initially the list is filled with random stations
        //Get the stations from the URL
        JsonShoutcastStationGetter jsonShoutcastStationGetter = new JsonShoutcastStationGetter(RANDOMSTATIONSURL);

        ShoutcastStation[] shoutcastStations = null;
        try {//Run in the ExecutorService
            shoutcastStations = MainApplication.executor.submit(jsonShoutcastStationGetter).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Building up an ArrayList for the TableView.
        //TableView requires SimpleStringProperties, so Arrays.asList(shoutcastStations) is not an option
        ArrayList <ShoutcastStation> stationArrayList = new ArrayList<>();
        for(ShoutcastStation sh: shoutcastStations){
            stationArrayList.add(new ShoutcastStation(sh.getName(),sh.getGenre(),sh.getId()));
        }
        //Set the List items
        stationColumn.setCellValueFactory(new PropertyValueFactory<ShoutcastStation,String>("nameProperty"));
        genreColumn.setCellValueFactory(new PropertyValueFactory<ShoutcastStation,String>("genreProperty"));
        //Set items to the TableView
        stationTableView.setItems(FXCollections.observableArrayList(stationArrayList));
        stationTableView.setOnMouseClicked(event -> {
            //This is needed so the station doesn't start playing when the user clicks on the TableColumnHeader
            if( ! (event.getTarget() instanceof TableColumnHeader) ){
                ShoutcastStation currentStation = stationTableView.getSelectionModel().getSelectedItem();
                startStream(currentStation);
            }
        });

        //Initialize Genres list
        JsonShoutcastGenresGetter jsonShoutcastGenresGetter = new JsonShoutcastGenresGetter(ALLGENRESURL);
        ShoutcastGenre shoutcastGenres[] = null;
        try {//Run in the ExecutorService
            shoutcastGenres = MainApplication.executor.submit(jsonShoutcastGenresGetter).get();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
        //ArrayList, so removal of items is easier
        ArrayList<ShoutcastGenre> shoutcastGenresArrayList = new ArrayList<>();
        //Add all of the elements from the Array to the ArrayList
        shoutcastGenresArrayList.addAll(Arrays.asList(shoutcastGenres));
        //Remove all genres that don't have at least 10 stations, and remove the Genre with the ID 232
        //Uses the Java 8 List method: removeIf(Predicate<? super E> filter)
        Predicate<ShoutcastGenre> lessThan10 = p-> p.getCount() < 10 || p.getId() == 232;
        shoutcastGenresArrayList.removeIf(lessThan10);
        //Set items to the ListView
        genresList.setItems( FXCollections.observableArrayList(shoutcastGenresArrayList) );
        genresList.setOnMouseClicked(event -> {
            //Get the selected item, get the Genre name
            String genre = genresList.getSelectionModel().getSelectedItem().getName();
            //Update the Station list
            XMLShoutcastStationGetter xmlShoutcastStationGetter = new XMLShoutcastStationGetter(STATIONSBYGENREURL + genre);
            ShoutcastStation stationArray[] = null;
            try {//Run in the ExecutorService
                stationArray = MainApplication.executor.submit(xmlShoutcastStationGetter).get();
            } catch (Exception e) {
                e.printStackTrace();
            }
            //Build up an ArrayList for the TableView
            ArrayList <ShoutcastStation> stationArrayListHelper = new ArrayList<>();
            for(ShoutcastStation sh: stationArray){
                stationArrayListHelper.add(new ShoutcastStation(sh.getName(),sh.getGenre(),sh.getId()));
            }
            stationTableView.setItems(FXCollections.observableArrayList(stationArrayListHelper));
        });
    }

    /**
     * Playback control
     * Starts the player and changes the playbackImage ImageView's current image depending on the player's state
     */
    public void playbackControl(){
        if(!player.getPlayerState()){
            try {
                player.startPlayback();
            } catch (Exception e) {
                stationNameTextField.setText("Station not available at the moment");
                genreTextField.setText("Genre not available");
            }
            playbackImage.setImage(pauseImage);
        }else{
            player.stopPlayback();
            playbackImage.setImage(playImage);
        }
    }

    /**
     * Gets the ID from the @param sh ShoutcastStation object and tunes the player to that station
     * Also sets the playbackImage's image to pauseImage
     * If the station gets played successfully then the station's name and genre get displayed, if not
     * then an error message gets displayed.
     * @param sh ShoutcastStation object which is going to get played
     */
    private void startStream(ShoutcastStation sh){
        player.stopPlayback();
        player.changeStream(TUNEIN + sh.getId());
        try {
            player.startPlayback();
            stationNameTextField.setText(sh.getName());
            genreTextField.setText(sh.getGenre());
        } catch (Exception e) {
            stationNameTextField.setText("Station not available at the moment");
            genreTextField.setText("Genre not available");
        }
        playbackImage.setImage(pauseImage);
    }


}
