package gstores.merchandiser_beta.web;

import java.util.List;

import gstores.merchandiser2.components.models.homedelivery.Brand;
import gstores.merchandiser_beta.components.models.UserTargetDetailsView;
import gstores.merchandiser_beta.components.models.UserTargets;
import gstores.merchandiser_beta.components.models.homedelivery.DeliveryHeader;
import gstores.merchandiser_beta.localdata.models.UserDetail;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface IWebClient {

    //i.e. api/HomeDelivery/GetAuthFor/
    @GET("/api/HomeDelivery/GetAuthFor")
    Call<UserDetail> GetAuthFor(@Query("UserName") String UserName, @Query("Password") String Password);


    @POST("/api/DeliveryJob/AddDeliveryJob")
    Call<String> AddDeliveryJob(
            @Body  DeliveryHeader DeliveryJob
    );

    @Multipart
    //@POST("/api/DeliveryJob/AddDeliveryJobWithAttachment")
    @POST("/api/DeliveryJobAttachment/AddDeliveryJobWithAttachment")
    Call<String> AddDeliveryJobWithAttachment(
            @Part("DeliveryHeader") RequestBody DeliveryJob,
            @Part MultipartBody.Part file);

    @Multipart
    //@POST("/api/DeliveryJob/AddDeliveryJobWithAttachment")
    @POST("/api/DeliveryJobAttachment/SaveAttachment")
    Call<String> SaveAttachment(
            @Part MultipartBody.Part file);

    //@POST("/api/DeliveryJob/AddDeliveryJobWithAttachment")
    @POST("/api/DeliveryJobAttachment/SaveSales")
    Call<String> SaveSales(
            @Body  DeliveryHeader DeliveryHeader);

    @GET("/api/DeliveryJob/GetMobileOrders")
    Call<List<DeliveryHeader>> GetMobileOrders(
            @Query("EmpID")String EmpID,
            @Query("TransType")String TransType);

    @GET("/api/MobileTargetAPI/UserTarget")
    Call<List<UserTargetDetailsView>> UserTarget(@Query("UserId") String UserId);
}
