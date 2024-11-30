package com.example.githubreposearchapp2.ui;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.githubreposearchapp2.R;
import com.example.githubreposearchapp2.adapter.RepoAdapter;
import com.example.githubreposearchapp2.database.AppDatabase;
import com.example.githubreposearchapp2.database.RepositoryEntity;
import com.example.githubreposearchapp2.network.GitHubApiService;
import com.example.githubreposearchapp2.network.RetrofitClient;
import com.example.githubreposearchapp2.model.RepoSearchResponse;
import com.example.githubreposearchapp2.model.Repository;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private EditText searchBar;
    private RepoAdapter repoAdapter;
    private int currentPage = 1;
    private boolean isLastPage = false;
    private AppDatabase appDatabase;
    private final List<Repository> allRepositories = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appDatabase = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "github_repos.db").build();
        setContentView(R.layout.activity_main);

        searchBar = findViewById(R.id.search_bar);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);

        repoAdapter = new RepoAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(repoAdapter);

        repoAdapter.setOnItemClickListener(repository -> {
            Intent intent = new Intent(MainActivity.this, RepoDetailsActivity.class);
            intent.putExtra("repo_name", repository.getName());
            intent.putExtra("repo_description", repository.getDescription());
            intent.putExtra("repo_url", repository.getUrl());
            intent.putExtra("repo_owner", repository.getOwner().getLogin());
            intent.putExtra("repo_image_url", repository.getOwner().getAvatarUrl()); // Assuming your Repository model includes an Owner with avatar URL
            startActivity(intent);
        });

        repoAdapter.setLoadMoreListener(() -> {
            if (!isLastPage) {
                loadMoreRepositories();
            }
        });

        // Set the Runnable for loading more repositories when needed
        if (isOnline()) {
            // Online: Allow search and pagination
            searchBar.setOnEditorActionListener((v, actionId, event) -> {
                String query = searchBar.getText().toString().trim();
                if (!TextUtils.isEmpty(query)) {
                    searchRepositories(query);
                }
                return true;
            });
        } else {
            // Offline: Load data from the database
            new Thread(() -> {
                List<RepositoryEntity> savedRepos = appDatabase.repositoryDao().getAllRepositories();
                runOnUiThread(() -> {
                    repoAdapter.submitList(convertFromEntities(savedRepos));
                });
            }).start();
        }
    }

    private List<Repository> convertFromEntities(List<RepositoryEntity> entities) {
        List<Repository> repositories = new ArrayList<>();
        for (RepositoryEntity entity : entities) {
            repositories.add(new Repository(entity.getId(), entity.getName(), entity.getDescription(), entity.getUrl()));
        }
        return repositories;
    }

    private void searchRepositories(String query) {
        currentPage = 1;  // Reset to the first page
        isLastPage = false;  // Reset last page flag
        allRepositories.clear();  // Clear the existing list

        GitHubApiService apiService = RetrofitClient.getInstance().create(GitHubApiService.class);
        apiService.searchRepositories(query, currentPage, 10).enqueue(new Callback<RepoSearchResponse>() {
            @Override
            public void onResponse(@NonNull Call<RepoSearchResponse> call, @NonNull Response<RepoSearchResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Repository> repositories = response.body().getItems();
                    allRepositories.addAll(repositories);  // Add to the complete list

                    // Save first 15 repositories to the database
                    new Thread(() -> {
                        if (!repositories.isEmpty()) {
                            List<RepositoryEntity> repositoryEntities = convertToEntities(repositories.subList(0, Math.min(15, repositories.size())));
                            appDatabase.repositoryDao().insertRepositories(repositoryEntities);
                        }
                    }).start();

                    repoAdapter.submitList(new ArrayList<>(allRepositories));  // Update adapter
                    if (repositories.size() < 10) {
                        isLastPage = true;  // Mark as last page if fewer than 10 items
                    }
                    repoAdapter.setIsLastPage(isLastPage);
                }
            }

            @Override
            public void onFailure(@NonNull Call<RepoSearchResponse> call, @NonNull Throwable t) {
                // Handle failure
            }
        });
    }

    private List<RepositoryEntity> convertToEntities(List<Repository> repositories) {
        List<RepositoryEntity> entities = new ArrayList<>();
        for (Repository repo : repositories) {
            entities.add(new RepositoryEntity(
                    repo.getId(),
                    repo.getName(),
                    repo.getDescription(),
                    repo.getUrl()
            ));
        }
        return entities;
    }

    private boolean isOnline() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        Network network = connectivityManager.getActiveNetwork();
        NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(network);
        return capabilities != null && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
    }

    private void loadMoreRepositories() {
        currentPage++;  // Increment page number

        String query = searchBar.getText().toString().trim();
        if (!query.isEmpty()) {
            GitHubApiService apiService = RetrofitClient.getInstance().create(GitHubApiService.class);
            apiService.searchRepositories(query, currentPage, 10).enqueue(new Callback<RepoSearchResponse>() {
                @Override
                public void onResponse(@NonNull Call<RepoSearchResponse> call, @NonNull Response<RepoSearchResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        List<Repository> repositories = response.body().getItems();
                        if (repositories.isEmpty()) {
                            isLastPage = true;  // Mark as last page if no more data
                        } else {
                            allRepositories.addAll(repositories);  // Append new results
                            repoAdapter.submitList(new ArrayList<>(allRepositories));  // Update adapter
                        }
                        repoAdapter.setIsLastPage(isLastPage);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<RepoSearchResponse> call, @NonNull Throwable t) {
                    // Handle failure
                }
            });
        }
    }
}