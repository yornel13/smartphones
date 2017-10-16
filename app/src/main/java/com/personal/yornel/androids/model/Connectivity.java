package com.personal.yornel.androids.model;

/**
 * Created by Yornel on 24/9/2017.
 */

public class Connectivity {

    private String lte;
    private String hspa;
    private String edge;
    private String wifi;
    private String bluetooth;
    private String navigation;

    public String getLte() {
        return lte;
    }

    public void setLte(String lte) {
        this.lte = lte;
    }

    public String getHspa() {
        return hspa;
    }

    public void setHspa(String hspa) {
        this.hspa = hspa;
    }

    public String getEdge() {
        return edge;
    }

    public void setEdge(String edge) {
        this.edge = edge;
    }

    public String getWifi() {
        return wifi;
    }

    public void setWifi(String wifi) {
        this.wifi = wifi;
    }

    public String getBluetooth() {
        return bluetooth;
    }

    public void setBluetooth(String bluetooth) {
        this.bluetooth = bluetooth;
    }

    public String getNavigation() {
        return navigation;
    }

    public void setNavigation(String navigation) {
        this.navigation = navigation;
    }
}
