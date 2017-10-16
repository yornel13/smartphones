package com.personal.yornel.androids.model;

import java.util.List;

/**
 * Created by Yornel on 24/9/2017.
 */

public class Smartphone {

    private String title;
    private String url;
    private String content;
    private String image;
    private List<String> gallery;
    private Screen screen;
    private Memory memory;
    private Processor processor;
    private Battery battery;
    private Camera camera;
    private Phone phone;
    private Connectivity connectivity;
    private List<Version> versions;
    private String antutu;
    private String review;

    private int top1;
    private int top2;
    private int top3;
    private int top4;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Screen getScreen() {
        return screen;
    }

    public void setScreen(Screen screen) {
        this.screen = screen;
    }

    public Memory getMemory() {
        return memory;
    }

    public void setMemory(Memory memory) {
        this.memory = memory;
    }

    public Processor getProcessor() {
        return processor;
    }

    public void setProcessor(Processor processor) {
        this.processor = processor;
    }

    public Battery getBattery() {
        return battery;
    }

    public void setBattery(Battery battery) {
        this.battery = battery;
    }

    public Camera getCamera() {
        return camera;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public Phone getPhone() {
        return phone;
    }

    public void setPhone(Phone phone) {
        this.phone = phone;
    }

    public Connectivity getConnectivity() {
        return connectivity;
    }

    public void setConnectivity(Connectivity connectivity) {
        this.connectivity = connectivity;
    }

    public List<String> getGallery() {
        return gallery;
    }

    public void setGallery(List<String> gallery) {
        this.gallery = gallery;
    }

    public List<Version> getVersions() {
        return versions;
    }

    public void setVersions(List<Version> versions) {
        this.versions = versions;
    }

    public String getAntutu() {
        return antutu;
    }

    public void setAntutu(String antutu) {
        this.antutu = antutu;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getTop1() {
        return top1;
    }

    public void setTop1(int top1) {
        this.top1 = top1;
    }

    public int getTop2() {
        return top2;
    }

    public void setTop2(int top2) {
        this.top2 = top2;
    }

    public int getTop3() {
        return top3;
    }

    public void setTop3(int top3) {
        this.top3 = top3;
    }

    public int getTop4() {
        return top4;
    }

    public void setTop4(int top4) {
        this.top4 = top4;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }
}
