package iut.android.tpfinal.objects;

import java.io.Serializable;

public class Defibrilator implements Serializable {
    private String commune, departemement, region;
    private double latitude, longitude;

    public Defibrilator(String commune, String departemement, double latitude, double longitude) {
        this.commune = commune;
        this.departemement = departemement;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "Defibrilator{" +
                "commune='" + commune + '\'' +
                ", departemement='" + departemement + '\'' +
                ", region='" + region + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
