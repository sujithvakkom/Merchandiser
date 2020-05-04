package gstores.merchandiser_beta.components.web;

import java.util.List;

import gstores.merchandiser2.components.models.homedelivery.Brand;
import gstores.merchandiser_beta.components.models.ItemModel;
import gstores.merchandiser_beta.components.models.UserLocation;
import gstores.merchandiser_beta.components.models.UserTargets;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface BusinessExcelService {

    //i.e. api/MobileAPI/AllProducts/
    @GET("/api/MobileAPI/AllProducts")
    Call<List<ItemModel>> AllProducts(@Query("Search") String Search, @Query("Page") int Page,@Query("ExtededFilter")boolean ExtededFilter);

    @GET("/api/MobileAPI/AllBrands")
    Call<List<Brand>> AllBrands();

    @GET("/api/MobileTargetAPI/AllUserTarget")
    Call< List<UserTargets>> GetAllUserTarget(@Query("UserId") Integer UserId);

    @POST("/api/MobileAPI/UserLocation")
    Call<UserLocation> postUserLocation(@Body UserLocation UserLocation);


    @Multipart
    //string itemcode, string customerName, string customerPhone, string customerEmirate, string pONumber,int quantity
    @POST("/api/DeliveryAPI/AddDelivery")
    Call<String> AddDelivery(@Query("itemcode") String itemcode,
                             @Query("customerName") String customerName,
                             @Query("customerPhone") String customerPhone,
                             @Query("customerEmirate") String customerEmirate,
                             @Query("pONumber") String pONumber,
                             @Query("quantity") int quantity,
                             @Query("userName") String userName,
                             @Query("address") String address,
                             @Query("remarks") String remarks,
                             @Query("saleDate") String saleDate,
                             @Query("preferedDate") String preferedDate,
                             @Part("description") RequestBody description,
                             @Part MultipartBody.Part file);
}
