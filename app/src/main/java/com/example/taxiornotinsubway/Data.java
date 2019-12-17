package com.example.taxiornotinsubway;


import java.io.Serializable;
import java.util.ArrayList;

class Data implements Serializable {
    private String taxiTime;
    private String subwayTime;
    private double startX;
    private double endX;
    private double startY;
    private double endY;
    private String startName;
    private String endName;
    private ArrayList<String> exchangeStations;


    public Data(String taxiTime, String subwayTime, double startX, double endX, double startY, double endY, String startName, String endName) {
        this.taxiTime = taxiTime;
        this.subwayTime = subwayTime;
        this.startX = startX;
        this.endX = endX;
        this.startY = startY;
        this.endY = endY;
        this.startName = startName;
        this.endName = endName;
    }

    public Data(String taxiTime, String subwayTime,String startName, String endName, ArrayList<String>  exchangeStations) {
        this.taxiTime = taxiTime;
        this.subwayTime = subwayTime;
        this.startName = startName;
        this.endName = endName;
        this.exchangeStations = exchangeStations;
    }


    public Data(String taxiTime, String subwayTime, double startX, double endX, double startY, double endY) {
        this.taxiTime = taxiTime;
        this.subwayTime = subwayTime;
        this.startX = startX;
        this.endX = endX;
        this.startY = startY;
        this.endY = endY;
    }

    public String getTaxiTime() {
        return taxiTime;
    }

    public String getSubwayTime() {
        return subwayTime;
    }

    public double getStartX() {
        return startX;
    }

    public double getEndX() {
        return endX;
    }

    public double getStartY() {
        return startY;
    }

    public double getEndY() {
        return endY;
    }

    public String getStartName() {
        return startName;
    }

    public String getEndName() {
        return endName;
    }

    public ArrayList<String>  getExchangeStations() {
        return exchangeStations;
    }
}