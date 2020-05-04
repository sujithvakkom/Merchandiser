package gstores.merchandiser_beta.components.models.homedelivery;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

public class DeliverySchedule implements Serializable {
    @SerializedName("Status")
    public Integer Status;
    @SerializedName("ScheduledDate")
    public Date ScheduledDate;
    @SerializedName("TimeSlot")
    public String TimeSlot;
    @SerializedName("DriverName")
    public String DriverName;
    @SerializedName("VehicleCode")
    public String VehicleCode;
    @SerializedName("GPS")
    public String GPS;
    @SerializedName("MapURL")
    public String MapURL;
    @SerializedName("Remarks")
    public String Remarks;
    @SerializedName("Emirate")
    public String Emirate;
    @SerializedName("UserName")
    public String UserName;
    @SerializedName("dateMinPreferredDate")
    public Date dateMinPreferredDate;
    @SerializedName("datePreferredDate")
    public Date datePreferredDate;
}