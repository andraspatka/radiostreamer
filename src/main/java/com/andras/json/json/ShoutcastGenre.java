package com.andras.json.json;

/**
 * Created by Patka Zsolt-Andras on 09.12.2017.
 * ShoutcastGenre class
 * Has all the properties which get returned with a query from the Shoutcast database
 * This class is needed for parsing the returned Json from the query to Java objects
 */
public class ShoutcastGenre {

    private String name;
    private Integer count;
    private Integer id;
    private Boolean haschildren;
    private Integer parentid;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getHaschildren() {
        return haschildren;
    }

    public void setHaschildren(Boolean haschildren) {
        this.haschildren = haschildren;
    }

    public Integer getParentid() {
        return parentid;
    }

    public void setParentid(Integer parentid) {
        this.parentid = parentid;
    }
    @Override
    public String toString(){
        return this.name;
    }

}
