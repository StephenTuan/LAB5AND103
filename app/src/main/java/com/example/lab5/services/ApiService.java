package com.example.lab5.services;

import com.example.lab5.models.Distributor;
import com.example.lab5.models.Response;

import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {
    public static String BASE_URl = "http://192.168.31.78:3000/";
    @GET("dis/list")
    Call<Response<ArrayList<Distributor>>> getListDistributor();

    @POST("dis/add")
    Call<Response<Distributor>> addDistributor(@Body Distributor distributor); // body dùng để gửi dữ liệu dưới dạng body của http request

    @PUT("dis/update/{id}")
    Call<Response<Distributor>> updateDistributor(@Path("id") String id, @Body Distributor distributor);

    @DELETE("dis/delete/{id}")
    Call<Response<Distributor>> deleteDistributor(@Path("id") String id);

}
