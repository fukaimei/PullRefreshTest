package net.fkm.pullrefreshtest.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieBaseModel extends BaseModel {

    private String reason;
    @SerializedName("error_code")
    private String errorCode;
    private List<MovieDataModel> data;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public List<MovieDataModel> getData() {
        return data;
    }

    public void setData(List<MovieDataModel> data) {
        this.data = data;
    }

}
