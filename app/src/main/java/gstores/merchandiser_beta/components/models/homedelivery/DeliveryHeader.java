package gstores.merchandiser_beta.components.models.homedelivery;

import android.content.Context;
import android.os.Environment;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.File;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import gstores.merchandiser_beta.components.AppLiterals;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class DeliveryHeader extends  DeliverySchedule
implements Serializable {
    @SerializedName("HeaderId")
    public Integer HeaderId;
    @SerializedName("Receipt")
    public String Receipt;
    @SerializedName("OrderNumber")
    public String OrderNumber;
    @SerializedName("CustomerName")
    public String CustomerName;
    @SerializedName("SaleDate")
    public Date SaleDate;
    @SerializedName("Phone")
    public String Phone;
    @SerializedName("DeliveryAddress")
    public String DeliveryAddress;
    @SerializedName("DeliveryLines")
    public ArrayList<DeliveryLine> DeliveryLines;
    @SerializedName("Retailer")
    public String Retailer;
    @SerializedName("Attachment")
    public String  Attachment;
    @SerializedName("saleType")
    public String saleType;
    @SerializedName("deliveryType")
    public String deliveryType;
    @SerializedName("attachmentName")
    public String attachmentName;
    @SerializedName("Price")
    public Double Price;
    @SerializedName("StatusDescription")
    public String StatusDescription;

    public String getSaleType() {
        return saleType;
    }

    public void setSaleType(String saleType) {
        this.saleType = saleType;
    }

    public String getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(String deliveryType) {
        this.deliveryType = deliveryType;
    }

    public String getCustomerLabel() {
        String customerLabel = "";
        if (CustomerName != null) {
            customerLabel += CustomerName;
            if (Phone != null)
                customerLabel += (System.getProperty("line.separator") + Phone);

        } else if (Phone != null)
            customerLabel += Phone;
        if (customerLabel.isEmpty()) customerLabel = "Add/Edit Customer";
        return customerLabel;
    }

    public String getSaleTypeLabel() {
        return getSaleType()+
                System.lineSeparator()+
                AppLiterals.APPLICATION_DATE_SMALL_VIEW_FORMAT.format(this.SaleDate);
        /*return Html.fromHtml(getSaleType()+System.lineSeparator()+"</br><sup>"+
                AppLiterals.APPLICATION_DATE_SMALL_VIEW_FORMAT.format(this.SaleDate)+"</sup>");*/
    }

    public String getDeliveryTypeLabel(String homeDeliverFlag) {
        String label =
                 getDeliveryType().equals(homeDeliverFlag) ?
                        getDeliveryType() +
                                System.lineSeparator() +
                                AppLiterals.APPLICATION_DATE_SMALL_VIEW_FORMAT.format(this.ScheduledDate) :
                        getDeliveryType();
        return label;
        /*
        return Html.fromHtml(getDeliveryType()+System.lineSeparator()+"</br><sup>"+
                AppLiterals.APPLICATION_DATE_SMALL_VIEW_FORMAT.format(this.ScheduledDate)+"</sup>");*/
    }

    public void resetAttachment() {
        //ByteArrayOutputStream stream = new ByteArrayOutputStream();
        //attachment.compress(Bitmap.CompressFormat.PNG, 0, stream);
        //byte[] byteArray = stream.toByteArray();
        this.Attachment = null;
        this.attachmentName=null;
    }

    public void resetAttachment(String galleryFile) {
        //ByteArrayOutputStream stream = new ByteArrayOutputStream();
        //attachment.compress(Bitmap.CompressFormat.PNG, 0, stream);
        //byte[] byteArray = stream.toByteArray();
        this.Attachment = null;
        this.attachmentName=galleryFile;
    }

    public File getAttachmentFile(Context context){

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp;

        File directory = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        //File directory = context.getFilesDir();
        if (!directory.exists()) {
            directory.mkdir();
        }
        try {
            File mypath = new File(directory, imageFileName + ".jpg");
            if(!mypath.createNewFile())
                return null;
            this.attachmentName =  mypath.getAbsolutePath();
            return mypath;
        }catch (Exception ex){
            ex.printStackTrace();
            return null;
        }

    }

    public String getAttachmentFilePath(){
        return this.attachmentName;
    }

    public MultipartBody.Part getRequestBodyPart() {
        try {
            String gsonSting = this.getGSON();

            MediaType MEDIATYPE_GSON = MediaType.parse("application/json; charset=utf-8");
            RequestBody body = RequestBody.create(MEDIATYPE_GSON,
                    gsonSting
            );
            return MultipartBody.Part.create(body);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public RequestBody getRequestBody() {
        try {
            String gsonSting = this.getGSON();

            MediaType MEDIATYPE_GSON = MediaType.parse("application/json; charset=utf-8");
            RequestBody body = RequestBody.create(MEDIATYPE_GSON,
                    gsonSting
            );
            return  body;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private String getGSON() {
        Gson gson = new Gson();
        return gson.toJson(this,this.getClass());
    }

    public boolean Validate() {
        if(deliveryType=="Home Delivery"){
            if(CustomerName.isEmpty()) {
                return false;}
            if(Phone.isEmpty()) return false;
            if(attachmentName.isEmpty()) return false;
            if(DeliveryLines==null || DeliveryLines.isEmpty())return false;
            return  true;
        }
        else {
            if(DeliveryLines==null || DeliveryLines.isEmpty())return false;
            return  true;
        }
    }

    public String getPrintableReceipt(String OrderNumber) {
        String receipt =  String.format("Receipt : %s",OrderNumber);
        receipt+= System.lineSeparator();
        receipt+= String.format("Date: %s",SaleDate);
        receipt+= System.lineSeparator();
        receipt+= String.format("============================================");
        double SelleingPriceTotal=0;
        for  ( DeliveryLine x:this.DeliveryLines
             ) {
            receipt+= System.lineSeparator();
            receipt+= String.format("%s  %s",x.ItemCode,x.Description);
            receipt+= System.lineSeparator();
            receipt+= String.format("Qty : %s",x.OrderQuantity);
            receipt+= String.format("Price : %s",x.SelleingPrice);
            SelleingPriceTotal+=x.SelleingPrice;
        }
        receipt+= System.lineSeparator();
        receipt+= String.format("============================================");
        receipt+= System.lineSeparator();
        receipt+= String.format("Total: %s",SelleingPriceTotal);
        receipt+= System.lineSeparator();
        receipt+= String.format("============================================");
        return  receipt;
    }

    protected class BitmapDataObject implements Serializable {
        private static final long serialVersionUID = 111696345129311948L;
        public byte[] imageByteArray;
    }

}