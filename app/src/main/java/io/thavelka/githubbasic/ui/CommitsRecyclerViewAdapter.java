package io.thavelka.githubbasic.ui;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import io.thavelka.githubbasic.R;
import io.thavelka.githubbasic.models.Commit;
import io.thavelka.githubbasic.models.CommitDetails;
import io.thavelka.githubbasic.models.User;

public class CommitsRecyclerViewAdapter extends RecyclerView.Adapter<CommitsRecyclerViewAdapter.CommitViewHolder> {
    private LayoutInflater inflater;
    private List<Commit> commits = new ArrayList<>();

    public CommitsRecyclerViewAdapter(@NonNull Context context, @Nullable Collection<Commit> commits) {
        inflater = LayoutInflater.from(context);
        if (commits != null) {
            this.commits.addAll(commits);
        }
    }

    @NonNull
    @Override
    public CommitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item_commit, parent, false);
        return new CommitViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommitViewHolder holder, int position) {
        if (position >= 0 && position < getItemCount()) {
            holder.bind(commits.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return commits != null ? commits.size() : 0;
    }

    public void setCommits(Collection<Commit> commits) {
        this.commits.clear();
        this.commits.addAll(commits);
        notifyDataSetChanged();
    }

    static class CommitViewHolder extends RecyclerView.ViewHolder {

        // Viewholder views
        private final TextView nameText;
        private final TextView emailText;
        private final TextView messageText;
        private final TextView hashText;

        // Placeholder strings
        private final String emptyName;
        private final String emptyEmail;
        private final String emptyMessage;
        private final String emptyHash;

        public CommitViewHolder(@NonNull View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.list_item_commit_text_author_name);
            emailText = itemView.findViewById(R.id.list_item_commit_text_author_email);
            messageText = itemView.findViewById(R.id.list_item_commit_text_message);
            hashText = itemView.findViewById(R.id.list_item_commit_text_hash);

            // Cache empty strings in fields to avoid lookups every bind
            Context context = itemView.getContext();
            emptyName = context.getString(R.string.commit_item_name_empty);
            emptyEmail = context.getString(R.string.commit_item_email_empty);
            emptyMessage = context.getString(R.string.commit_item_message_empty);
            emptyHash = context.getString(R.string.commit_item_hash_empty);
        }

        public void bind(@NonNull Commit commit) {
            // Default to placeholder values
            String name = emptyName;
            String email = emptyEmail;
            String message = emptyMessage;
            String hash = emptyHash;

            // Get available information
            CommitDetails details = commit.commit;
            if (details != null) {
                User author = details.author;
                if (author != null) {
                    if (!TextUtils.isEmpty(author.name)) {
                        name = author.name;
                    }
                    if (!TextUtils.isEmpty(author.email)) {
                        email = String.format("<%s>", author.email);
                    }
                }
                if (!TextUtils.isEmpty(details.message)) {
                    message = details.message;
                }
            }

            if (!TextUtils.isEmpty(commit.sha)) {
                hash = commit.sha;
            }

            // Set text in TextViews
            nameText.setText(name);
            emailText.setText(email);
            messageText.setText(message);
            hashText.setText(hash);
        }
    }
}
