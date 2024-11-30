package com.example.githubreposearchapp2.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.githubreposearchapp2.R;
import com.example.githubreposearchapp2.model.Contributor;

import java.util.ArrayList;
import java.util.List;

public class ContributorsAdapter extends RecyclerView.Adapter<ContributorsAdapter.ContributorViewHolder> {

    private List<Contributor> contributors = new ArrayList<>();

    @NonNull
    @Override
    public ContributorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contributor_item, parent, false);
        return new ContributorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContributorViewHolder holder, int position) {
        Contributor contributor = contributors.get(position);
        holder.bind(contributor);
    }

    @Override
    public int getItemCount() {
        return contributors.size();
    }

    public void submitList(List<Contributor> contributors) {
        this.contributors = contributors;
        notifyDataSetChanged();
    }

    public static class ContributorViewHolder extends RecyclerView.ViewHolder {
        private final TextView contributorNameTextView;

        public ContributorViewHolder(@NonNull View itemView) {
            super(itemView);
            contributorNameTextView = itemView.findViewById(R.id.contributor_name);
        }

        public void bind(Contributor contributor) {
            contributorNameTextView.setText(contributor.getLogin());
        }
    }
}
