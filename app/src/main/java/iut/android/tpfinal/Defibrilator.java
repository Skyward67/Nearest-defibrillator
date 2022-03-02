package iut.android.tpfinal;

public class Defibrilator {
    private String commune, departemement, region;
    private long latitude, longitude;

    public Defibrilator(String commune, String departemement, String region, long latitude, long longitude) {
        this.commune = commune;
        this.departemement = departemement;
        this.region = region;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
