package gstores.merchandiser_beta.components.models.homedelivery;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

public class DeliveryLine extends DeliverySchedule implements Serializable {
    @SerializedName("LineId")
    public Integer LineId;
    @SerializedName("SaleDate")
    public Date SaleDate;
    @SerializedName("LineNumber")
    public double LineNumber;
    @SerializedName("ItemCode")
    public String ItemCode;
    @SerializedName("Description")
    public String Description;
    public double OrderQuantity;
    @SerializedName("SelleingPrice")
    public double SelleingPrice;

    @Override
    public String toString() {
        return this.Description;
    }
}