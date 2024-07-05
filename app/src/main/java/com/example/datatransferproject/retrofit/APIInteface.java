package com.example.datatransferproject.retrofit;

import com.example.datatransferproject.model.Root;

import retrofit2.Call;
import retrofit2.http.GET;

public interface APIInteface {

    @GET("employees")
    Call<Root> getAllUsers();
}
