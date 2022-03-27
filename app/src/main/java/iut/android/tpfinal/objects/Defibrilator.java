package iut.android.tpfinal.objects;

import java.io.Serializable;

public class Defibrilator implements Serializable {
    private String commune, departemement, acc, acc_complt;
    private double latitude, longitude, distance;

    public Defibrilator(String commune, String departemement, double latitude, double longitude, double distance) {
        this.commune = commune;
        this.departemement = departemement;
        this.latitude = latitude;
        this.longitude = longitude;
        this.distance = distance;
        this.acc = "";
        this.acc_complt = "";
    }

    public Defibrilator(String commune, String departemement, double latitude, double longitude, double distance, String acc) {
        this.commune = commune;
        this.departemement = departemement;
        this.acc = acc;
        this.latitude = latitude;
        this.longitude = longitude;
        this.distance = distance;
        this.acc_complt="";
    }

    public Defibrilator(String commune, String departemement, double latitude, double longitude, double distance, String acc, String acc_complt) {
        this.commune = commune;
        this.departemement = departemement;
        this.acc = acc;
        this.acc_complt = acc_complt;
        this.latitude = latitude;
        this.longitude = longitude;
        this.distance = distance;
    }

    public String getCommune() {
        return commune;
    }

    public String getDepartemement() {
        return departemement;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getDistance() {
        return distance;
    }

    public String getAcc() {
        return acc;
    }

    public String getAcc_complt() {
        return acc_complt;
    }

    @Override
    public String toString() {
        return "Defibrilator{" +
                "commune='" + commune + '\'' +
                ", departemement='" + departemement + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", distance=" + distance +
                ", acc=" + acc +
                ", acc_complt" + acc_complt +
                '}';
    }
}
