package com.example.lab5.models;
import com.google.gson.annotations.SerializedName;
public class Distributor {

    @SerializedName("_id")
    private String id;
    private String nameDistributor;
    private String createAt, updateAt;

    public Distributor() {
    }

    public Distributor(String id, String nameDistributor, String createAt, String updateAt) {
        this.id = id;
        this.nameDistributor = nameDistributor;
        this.createAt = createAt;
        this.updateAt = updateAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNameDistributor() {
        return nameDistributor;
    }

    public void setNameDistributor(String nameDistributor) {
        this.nameDistributor = nameDistributor;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public String getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(String updateAt) {
        this.updateAt = updateAt;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", username='" + nameDistributor + '\'' +
                '}';
    }

    }
