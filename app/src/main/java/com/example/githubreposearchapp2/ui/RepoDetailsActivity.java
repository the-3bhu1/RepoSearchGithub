package com.example.githubreposearchapp2.ui;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.githubreposearchapp2.R;
import com.example.githubreposearchapp2.adapter.ContributorsAdapter;
import com.example.githubreposearchapp2.model.Contributor;
import com.example.githubreposearchapp2.network.GitHubApiService;
import com.example.githubreposearchapp2.network.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RepoDetailsActivity extends AppCompatActivity {

    private TextView repoNameTextView, repoDescriptionTextView, repoUrlTextView;
    private ImageView repoImageView;
    private RecyclerView contributorsRecyclerView;
    private ContributorsAdapter contributorsAdapter;
    private GitHubApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repo_details);

        // Initialize views
        repoImageView = findViewById(R.id.repo_image);
        repoNameTextView = findViewById(R.id.repo_name);
        repoDescriptionTextView = findViewById(R.id.repo_description);
        repoUrlTextView = findViewById(R.id.repo_link);
        contributorsRecyclerView = findViewById(R.id.contributors_recycler_view);
        contributorsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        contributorsAdapter = new ContributorsAdapter();
        contributorsRecyclerView.setAdapter(contributorsAdapter);

        // Get the data passed from MainActivity
        String repoName = getIntent().getStringExtra("repo_name");
        String repoDescription = getIntent().getStringExtra("repo_description");
        String repoUrl = getIntent().getStringExtra("repo_url");
        String owner = getIntent().getStringExtra("repo_owner");
        String repoImageUrl = getIntent().getStringExtra("repo_image_url");

        // Set the data to the TextViews
        if (repoName != null) {
            repoNameTextView.setText(repoName);
        }
        if (repoDescription != null) {
            repoDescriptionTextView.setText(repoDescription);
        }
        if (repoUrl != null) {
            repoUrlTextView.setText(repoUrl);
        }

        // Fetch contributors
        if (owner != null && repoName != null) {
            fetchContributors(owner, repoName);
        }
        Glide.with(this)
                .load(repoImageUrl)
                .placeholder(R.drawable.placeholder_image) // Add a placeholder image in your drawable folder
                .error(R.drawable.error_image) // Add an error image in your drawable folder
                .into(repoImageView);
    }

    private void fetchContributors(String owner, String repoName) {
        apiService = RetrofitClient.getInstance().create(GitHubApiService.class);
        apiService.getContributors(owner, repoName).enqueue(new Callback<List<Contributor>>() {
            @Override
            public void onResponse(@NonNull Call<List<Contributor>> call, @NonNull Response<List<Contributor>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Contributor> contributors = response.body();
                    contributorsAdapter.submitList(contributors);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Contributor>> call, @NonNull Throwable t) {
                Toast.makeText(RepoDetailsActivity.this, "Failed to load contributors", Toast.LENGTH_SHORT).show();
            }
        });
    }
}