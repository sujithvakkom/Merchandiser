package gstores.merchandiser_beta.components.models;

public class UserLocation {

    public Integer UserID;

    public Integer getUserID() {
        return UserID;
    }

    public Double Latitude ;

    public Double getLatitude() {
        return Latitude;
    }

    public Double Longitude ;

    public Double getLongitude() {
        return Longitude;
    }

    public Integer Type;

    public Integer getType() {
        return Type;
    }

    public UserLocation(Integer userID, Double latitude, Double longitude, Integer type) {
        UserID = userID;
        Latitude = latitude;
        Longitude = longitude;
        Type = type;
    }
}
