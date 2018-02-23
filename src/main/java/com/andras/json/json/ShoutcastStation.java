package com.andras.json.json;

import javafx.beans.property.SimpleStringProperty;

/**
* created by Patka Zsolt-Andras
* ShoutcastStation class
* Has all the properties which get returned with a query from the Shoutcast database
* This class is needed for parsing the returned Json from the query to Java objects
*/
public class ShoutcastStation {
    private SimpleStringProperty nameProperty = null;//Needed for TableView
    private SimpleStringProperty genreProperty = null;//Needed for TableView
    private Integer br;
    private String mt;
    private Integer lc;
    private String name;
    private String genre;
    private Integer id;
    private Integer ml;

    public ShoutcastStation(String name, String genre, Integer id){
        this.name = name;
        this.genre = genre;
        this.nameProperty = new SimpleStringProperty(name);
        this.genreProperty = new SimpleStringProperty(genre);
        this.id = id;
    }
    public ShoutcastStation(){}

    public Integer getBr() {
        return br;
    }

    public void setBr(Integer br) {
        this.br = br;
    }

    public String getMt() {
        return mt;
    }

    public void setMt(String mt) {
        this.mt = mt;
    }

    public Integer getLc() {
        return lc;
    }

    public void setLc(Integer lc) {
        this.lc = lc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        nameProperty.set(name);
        this.name = name;
    }

    public String getGenre() {
        return genre;
    }

    public String getGenreProperty(){
        return this.genreProperty.get();
    }
    public String getNameProperty(){
        return this.nameProperty.get();
    }

    public void setGenre(String genre) {
        genreProperty.set(genre);
        this.genre = genre;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMl() {
        return ml;
    }

    public void setMl(Integer ml) {
        this.ml = ml;
    }

}
