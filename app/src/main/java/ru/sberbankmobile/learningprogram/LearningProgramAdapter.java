package ru.sberbankmobile.learningprogram;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ru.sberbankmobile.learningprogram.models.Lecture;

public class LearningProgramAdapter extends RecyclerView.Adapter<LearningProgramAdapter.LectureHolder> {

    private List<Lecture> mLectures;
    private boolean groupByWeek;

    @NonNull
    @Override
    public LectureHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lecture, parent, false);
        return new LectureHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull LectureHolder holder, int position) {
        Lecture lecture = mLectures.get(position);
        holder.mNumber.setText(lecture.getNumber());
        holder.mLecturer.setText(lecture.getLecturer());
        holder.mTheme.setText(lecture.getTheme());
        holder.mDate.setText(lecture.getDate());
    }

//    @Override
//    public void onBindViewHolder(@NonNull WeekHolder holder, int position) {
//        Lecture lecture = mLectures.get(position);
//        holder.mWeekLectures.setText(lecture.getDate());
//    }


    @Override
    public int getItemCount() {
        return mLectures == null ? 0 : mLectures.size();
    }

    public void setLectures(List<Lecture> lectures) {
        this.mLectures = lectures == null ? null : new ArrayList<>(lectures);
    }

    static class LectureHolder extends RecyclerView.ViewHolder {
        private final TextView mNumber;
        private final TextView mDate;
        private final TextView mTheme;
        private final TextView mLecturer;

        private LectureHolder(@NonNull View itemView) {
            super(itemView);
            mNumber = itemView.findViewById(R.id.number);
            mDate = itemView.findViewById(R.id.date);
            mTheme = itemView.findViewById(R.id.theme);
            mLecturer = itemView.findViewById(R.id.lecturer);
        }
    }

    static class WeekHolder extends RecyclerView.ViewHolder {
        private final TextView mWeekLectures;

        private WeekHolder(@NonNull View itemView) {
            super(itemView);
            mWeekLectures = itemView.findViewById(R.id.week);
        }
    }
}
