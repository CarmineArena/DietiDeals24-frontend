package com.example.dietideals24frontend.retrofit;

import retrofit2.Call;
import retrofit2.http.GET;

public interface GitHubApiService {
    @GET("/startGitHubLogin")
    Call<String> startGitHubProcess();
}