package com.personal.yornel.androids.model;

/**
 * Created by Yornel on 23/9/2017.
 */

public class Camera {

    private String principalResolution;
    private String principalSensor;
    private String principalAperture;
    private String principalFlash;
    private String secondaryResolution;

    public String getPrincipalResolution() {
        return principalResolution;
    }

    public void setPrincipalResolution(String principalResolution) {
        this.principalResolution = principalResolution;
    }

    public String getPrincipalSensor() {
        return principalSensor;
    }

    public void setPrincipalSensor(String principalSensor) {
        this.principalSensor = principalSensor;
    }

    public String getPrincipalAperture() {
        return principalAperture;
    }

    public void setPrincipalAperture(String principalAperture) {
        this.principalAperture = principalAperture;
    }

    public String getPrincipalFlash() {
        return principalFlash;
    }

    public void setPrincipalFlash(String principalFlash) {
        this.principalFlash = principalFlash;
    }

    public String getSecondaryResolution() {
        return secondaryResolution;
    }

    public void setSecondaryResolution(String secondaryResolution) {
        this.secondaryResolution = secondaryResolution;
    }
}
