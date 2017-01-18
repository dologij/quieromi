package com.brunix.quieromi.data.entity;

import com.brunix.quieromi.Utils;
import com.brunix.quieromi.base.BaseModel;
import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dolo on 9/14/16.
 */
public class Tapa extends BaseModel {

    @Exclude
    String id;
    String name;
    Double price;
    String imageUrl;
    List<String> reports;
    Double longitude;
    Double latitude;

    public Tapa() {}

    public Tapa(String name, Double price, String imageUrl, List reports, Double longitude, Double latitude){
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.reports = reports;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public List getReports() {
        return reports;
    }

    public void setReports(List reports) {
        this.reports = reports;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhotoFilename() {
        return "IMG_" + getId().toString() + ".jpg";
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("price", price);
        result.put("imageUrl", imageUrl);
        result.put("reports", reports);
        result.put("longitude", longitude);
        result.put("latitude", latitude);

        return result;
    }

    @Override
    public String toString() {
        return "Tapa{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", imageUrl='" + imageUrl + '\'' +
                ", reports=" + reports +
                '}';
    }

    @Override
    protected boolean validate() {
        return !Utils.stringsAreNullOrEmpty(id, name, String.valueOf(price));
    }
}
