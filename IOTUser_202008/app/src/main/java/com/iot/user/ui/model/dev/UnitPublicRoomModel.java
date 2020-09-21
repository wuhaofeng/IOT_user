package com.iot.user.ui.model.dev;

import java.io.Serializable;

public class UnitPublicRoomModel implements Serializable {
    public  UnitPublicRoomModel(){

    }
    public UnitPublicRoomModel(String room_id,String room_name,String room_url){
        this.place_dictionary_id=room_id;
        this.place_name=room_name;
        this.place_url=room_url;
    }

    private String place_dictionary_id;
    private String place_name;
    private String place_url;

    public String getPlace_dictionary_id() {
        return place_dictionary_id;
    }

    public void setPlace_dictionary_id(String place_dictionary_id) {
        this.place_dictionary_id = place_dictionary_id;
    }

    public String getPlace_name() {
        return place_name;
    }

    public void setPlace_name(String place_name) {
        this.place_name = place_name;
    }

    public String getPlace_url() {
        return place_url;
    }

    public void setPlace_url(String place_url) {
        this.place_url = place_url;
    }
}

