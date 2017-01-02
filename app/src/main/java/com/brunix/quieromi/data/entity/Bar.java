package com.brunix.quieromi.data.entity;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dolo on 9/16/16.
 */
public class Bar {

    @Exclude
    String id;
    String name;
    String imageUrl;
    List<String> reports;
    Double longitude;
    Double latitude;
    List<String> tapas;

    public Bar() {}

    public Bar(String name, String imageUrl, List<String> reports, Double longitude, Double latitude) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.reports = reports;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<String> getReports() {
        return reports;
    }

    public void setReports(List<String> reports) {
        this.reports = reports;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public List<String> getTapas() {
        return tapas;
    }

    public void setTapas(List<String> tapas) {
        this.tapas = tapas;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("imageUrl", imageUrl);
        result.put("reports", reports);
        result.put("longitude", longitude);
        result.put("latitude", latitude);
        result.put("tapas", tapas);

        return result;
    }

    @Override
    public String toString() {
        return "Bar{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", imageUrl='" + imageUrl + '\'' +
                ", reports=" + reports +
                ", tapas=" + tapas +
                '}';
    }
}
