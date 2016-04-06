package com.trendoidtechnologies.vault.service;

import android.content.Context;

import com.trendoidtechnologies.vault.datacontract.Computer;
import com.trendoidtechnologies.vault.datacontract.Credential;
import com.trendoidtechnologies.vault.datacontract.Permission;
import com.trendoidtechnologies.vault.datacontract.Token;
import com.trendoidtechnologies.vault.datacontract.User;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;


/**
 * Created by qazimusab on 3/30/16.
 */
public interface VaultService {
    //Retrofit turns our institute WEB API into a Java interface.
    //So these are the list available in our WEB API and the methods look straight forward

    class Factory {
        private static final String BASE_URL = "https://passwordvaultapi.azurewebsites.net/";
        private static VaultService vaultService;

        public static VaultService getInstance(Context context) {
            if (vaultService == null) {

                OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
                builder.readTimeout(15, TimeUnit.SECONDS);
                builder.connectTimeout(10, TimeUnit.SECONDS);
                builder.writeTimeout(10, TimeUnit.SECONDS);

                int cacheSize = 10 * 1024 * 1024; // 10 MiB
                Cache cache = new Cache(context.getCacheDir(), cacheSize);
                builder.cache(cache);

                Retrofit retrofit = new Retrofit.Builder().client(builder.build()).addConverterFactory(GsonConverterFactory.create()).baseUrl(BASE_URL).build();
                vaultService = retrofit.create(VaultService.class);
                return vaultService;
            }
            else {
                return vaultService;
            }
        }
    }

    @FormUrlEncoded
    @POST("/api/Token")
    Call<Token> getAuthToken(@Field("username") String username, @Field("password") String password, @Field("grant_type") String grantType);

    @GET("/api/AspNetUsers")
    Call<List<User>> getAllUsers(@Header("Authorization") String token);

    @GET("/api/AspNetUsers")
    Call<User> getUser(@Header("Authorization") String token, @Query("email") String email);

    @PUT("/api/AspNetUsers")
    Call<User> updateUser(@Header("Authorization") String token, @Body User user, @Query("id") String id);

    @POST("api/Computers")
    Call<Computer> addComputer(@Header("Authorization") String token, @Body Computer computer);

    @POST("api/Credentials")
    Call<Credential> addCredential(@Header("Authorization") String token, @Body Credential credential);

    @POST("api/Departments")
    Call<Permission> addDepartment(@Header("Authorization") String token, @Body Permission department);

    @POST("api/Account/Register")
    Call<Void> addUser(@Body User user);

}
