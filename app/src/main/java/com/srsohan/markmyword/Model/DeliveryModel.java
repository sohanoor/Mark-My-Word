package com.srsohan.markmyword.Model;


public class DeliveryModel {

    private int _id;
    private String _description;
    private String _imageUrl;
    private double _lat;
    private double _lng;
    private String _address;


    public DeliveryModel(int id, String description, String imageUrl, double lat, double lng, String address) {

        _id = id;
        _description = description;
        _imageUrl = imageUrl;
        _lat = lat;
        _lng = lng;
        _address = address;
    }

    public int get_id() {return _id;}

    public String get_description() {
        return _description;
    }

    public String get_imageUrl() {
        return _imageUrl;
    }

    public double get_lat() {
        return _lat;
    }

    public double get_lng() {
        return _lng;
    }

    public String get_address() {
        return _address;
    }

}

