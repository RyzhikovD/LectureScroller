package ru.sberbankmobile.learningprogram.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DetailsAdapter extends RecyclerView.Adapter<DetailsAdapter.SubtopicHolder> {

    private List<String> mSubtopics;

    @NonNull
    @Override
    public DetailsAdapter.SubtopicHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new SubtopicHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailsAdapter.SubtopicHolder holder, int position) {
        holder.mSubtopic.setText(mSubtopics.get(position));
    }

    @Override
    public int getItemCount() {
        return mSubtopics == null ? 0 : mSubtopics.size();
    }

    public void setSubtopics(List<String> subtopics) {
        mSubtopics = subtopics;
    }

    static class SubtopicHolder extends RecyclerView.ViewHolder {
        private final TextView mSubtopic;

        private SubtopicHolder(@NonNull View itemView) {
            super(itemView);
            mSubtopic = itemView.findViewById(android.R.id.text1);
        }
    }
}
