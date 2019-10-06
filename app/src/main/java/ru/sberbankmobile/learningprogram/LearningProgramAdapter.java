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

public class LearningProgramAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int LECTURE_TYPE = 0;
    public static final int WEEK_TYPE = 1;

    private List<Lecture> mLectures;
    private List<String> mWeekNames;
    private List<Object> mLecturesAndWeeks;
    private boolean groupByWeek;

    @Override
    public int getItemViewType(int position) {
//        return groupByWeek ? LECTURE_TYPE : WEEK_TYPE;
        return mLecturesAndWeeks.get(position) instanceof Lecture ? LECTURE_TYPE : WEEK_TYPE;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case LECTURE_TYPE:
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lecture, parent, false);
                return new LectureHolder(view);
            case WEEK_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_week, parent, false);
                return new WeekHolder(view);
            default:
                throw new IllegalStateException("Unexpected viewType: " + viewType);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case LECTURE_TYPE:
                Lecture lecture = (Lecture) mLecturesAndWeeks.get(position);
                LectureHolder lectureHolder = (LectureHolder) holder;
                lectureHolder.mNumber.setText(lecture.getNumber());
                lectureHolder.mLecturer.setText(lecture.getLecturer());
                lectureHolder.mTheme.setText(lecture.getTheme());
                lectureHolder.mDate.setText(lecture.getDate());
                break;
            case WEEK_TYPE:
                WeekHolder weekHolder = (WeekHolder) holder;
                weekHolder.weekName.setText((String) mLecturesAndWeeks.get(position));
                break;
        }
    }

    @Override
    public int getItemCount() {
//        if (groupByWeek) {
//            return mWeekNames == null ? 0 : mWeekNames.size();
//        } else {
//            return mLectures == null ? 0 : mLectures.size();
//        }
        return mLecturesAndWeeks == null ? 0 : mLecturesAndWeeks.size();
    }

    public void setLectures(List<Lecture> lectures) {
        mLectures = lectures;
        mLecturesAndWeeks = new ArrayList<>();
        if (groupByWeek) {
            int weekIndex = -1;
            for (Lecture lecture : lectures) {
                if (lecture.getWeekIndex() > weekIndex) {
                    mLecturesAndWeeks.add(mWeekNames.get((weekIndex = lecture.getWeekIndex())));
                }
                mLecturesAndWeeks.add(lecture);
            }
        } else {
            if (lectures != null) {
                mLecturesAndWeeks.addAll(lectures);
            }
        }
    }

    public void setWeekNames(List<String> mWeekNames) {
        this.mWeekNames = mWeekNames;
    }

    public void groupByWeek(boolean groupByWeek) {
        this.groupByWeek = groupByWeek;
        setLectures(mLectures);
    }

    private static class LectureHolder extends RecyclerView.ViewHolder {
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

    private static class WeekHolder extends RecyclerView.ViewHolder {
        private final TextView weekName;

        private WeekHolder(@NonNull View itemView) {
            super(itemView);
            weekName = itemView.findViewById(R.id.week_name);
        }
    }
}
