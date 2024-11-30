package com.example.githubreposearchapp2.network;

import com.example.githubreposearchapp2.model.Contributor;
import com.example.githubreposearchapp2.model.RepoSearchResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GitHubApiService {
    // Search repositories
    @GET("search/repositories")
    Call<RepoSearchResponse> searchRepositories(
            @Query("q") String query,
            @Query("page") int page,
            @Query("per_page") int perPage
    );

    // Get contributors for a repository
    @GET("repos/{owner}/{repo}/contributors")
    Call<List<Contributor>> getContributors(
            @Path("owner") String owner,
            @Path("repo") String repo
    );
}