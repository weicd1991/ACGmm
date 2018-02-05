package com.jsmy.acgmm.model;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by Administrator on 2017/10/11.
 */

public interface GitApi {
    //Get请求
    @GET
    Call<String> getNetWork(
            @Url String url,
            @QueryMap Map<String, String> options);

    //post请求
    @FormUrlEncoded
    @POST
    Call<String> postNetWork(
            @Url String url,
            @FieldMap Map<String, String> fields);

    // 上传单个文件
    @POST
    Call<String> uploadFile(
            @Url String url,
            @Body RequestBody Body);


    // 上传多个文件
    @Multipart
    @POST
    Call<String> uploadMultipleFiles(
            @Part("description") RequestBody description, @Part MultipartBody.Part file1, @Part MultipartBody.Part file2);

    /**
     * 文件下载
     *
     * @param fileUrl
     * @return
     */
    @Streaming
    @GET
    Call<ResponseBody> downloadFiles(
            @Url String fileUrl);

    @Multipart
    @POST
    Call<String> updataFiles(
            @Url String url,
            @QueryMap Map<String, String> options,
            @PartMap() Map<String, RequestBody> maps);

    @POST()
    Call<String> upLoad(
            @Url() String url,
            @Body RequestBody Body);

    @Multipart
    @POST()
    Observable<ResponseBody> uploadFiles(
            @Url String url,
            @QueryMap Map<String, String> options,
            @PartMap() Map<String, RequestBody> maps);


}
