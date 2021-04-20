package gstores.merchandiser_beta.components.models;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class UserLocation  implements Serializable{

    @SerializedName("UserName")
    public String UserName;
    public String Address;

    public String getUserID() {
        return UserName;
    }

    @SerializedName("Latitude")
    public Double Latitude ;

    public Double getLatitude() {
        return Latitude;
    }

    @SerializedName("Longitude")
    public Double Longitude ;

    public Double getLongitude() {
        return Longitude;
    }

    @SerializedName("Type")
    public Integer Type;

    public Integer getType() {
        return Type;
    }

    public UserLocation(String UserName, Double latitude, Double longitude, Integer type) {
        this.UserName = UserName;
        Latitude = latitude;
        Longitude = longitude;
        Type = type;
    }
}
