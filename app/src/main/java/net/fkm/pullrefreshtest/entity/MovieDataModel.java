package net.fkm.pullrefreshtest.entity;

import com.google.gson.annotations.SerializedName;

public class MovieDataModel extends BaseModel {

    @SerializedName("name")
    private String movieName;
    private String tag;
    private String updateStatus;
    private String poster;

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getUpdateStatus() {
        return updateStatus;
    }

    public void setUpdateStatus(String updateStatus) {
        this.updateStatus = updateStatus;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

}
