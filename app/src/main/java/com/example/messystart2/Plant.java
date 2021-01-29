package com.example.messystart2;

import java.util.List;

public class Plant {
    private String dateCut;
    private String datePlanted;
    private List<String> daughterID;
    private String greenhouse;
    private String motherID;
    private String plantID;
    private String rating;
    private String sequence;
    private String strain;

    public Plant() {
    }

    public String getDateCut() {
        return dateCut;
    }

    public void setDateCut(String dateCut) {
        this.dateCut = dateCut;
    }

    public String getDatePlanted() {
        return datePlanted;
    }

    public void setDatePlanted(String datePlanted) {
        this.datePlanted = datePlanted;
    }

    public List<String> getDaughterID() {
        return daughterID;
    }

    public void setDaughterID(List<String> daughterID) {
        this.daughterID = daughterID;
    }

    public String getGreenhouse() {
        return greenhouse;
    }

    public void setGreenhouse(String greenhouse) {
        this.greenhouse = greenhouse;
    }

    public String getMotherID() {
        return motherID;
    }

    public void setMotherID(String motherID) {
        this.motherID = motherID;
    }

    public String getPlantID() {
        return plantID;
    }

    public void setPlantID(String plantID) {
        this.plantID = plantID;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public String getStrain() {
        return strain;
    }

    public void setStrain(String strain) {
        this.strain = strain;
    }
}
