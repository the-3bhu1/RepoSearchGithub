package com.example.githubreposearchapp2.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.githubreposearchapp2.R;
import com.example.githubreposearchapp2.model.Repository;

import java.util.ArrayList;
import java.util.List;

public class RepoAdapter extends RecyclerView.Adapter<RepoAdapter.RepoViewHolder> {

    private List<Repository> repositories = new ArrayList<>();
    private OnItemClickListener onItemClickListener;
    private boolean isLastPage = false;
    private Runnable loadMoreRepositoriesRunnable;

    // Interface for item click listener
    public interface OnItemClickListener {
        void onItemClick(Repository repository);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public void setLoadMoreListener(Runnable loadMoreRepositoriesRunnable) {
        this.loadMoreRepositoriesRunnable = loadMoreRepositoriesRunnable;
    }

    public void setIsLastPage(boolean isLastPage) {
        this.isLastPage = isLastPage;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void submitList(List<Repository> newRepositories) {
        this.repositories = newRepositories;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RepoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.repo_item, parent, false);
        return new RepoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RepoViewHolder holder, int position) {
        Repository repository = repositories.get(position);
        holder.bind(repository);

        // Handle item click
        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(repository);
            }
        });

        // Trigger load more when reaching the end of the list
        if (position == getItemCount() - 1 && loadMoreRepositoriesRunnable != null && !isLastPage) {
            loadMoreRepositoriesRunnable.run();
        }
    }

    @Override
    public int getItemCount() {
        return repositories.size();
    }

    // ViewHolder class
    public static class RepoViewHolder extends RecyclerView.ViewHolder {

        private final TextView repoNameTextView;
        private final TextView repoDescriptionTextView;

        public RepoViewHolder(@NonNull View itemView) {
            super(itemView);
            repoNameTextView = itemView.findViewById(R.id.repo_name);
            repoDescriptionTextView = itemView.findViewById(R.id.repo_description);
        }

        public void bind(Repository repository) {
            repoNameTextView.setText(repository.getName());
            repoDescriptionTextView.setText(repository.getDescription());
        }
    }
}