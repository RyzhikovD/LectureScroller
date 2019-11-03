package ru.sberbankmobile.learningprogram.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ru.sberbankmobile.learningprogram.R;
import ru.sberbankmobile.learningprogram.models.Lecture;

public class LecturesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String DATE_FORMAT = "dd.MM.yyyy";

    private boolean mGroupByWeek;

    public static final int LECTURE_TYPE = 0;
    public static final int WEEK_TYPE = 1;

    private List<Lecture> mLectures;
    private List<String> mWeekNames;
    private List<Object> mLecturesAndWeeks;

    private OnItemClickListener mClickListener;

    public LecturesAdapter(OnItemClickListener clickListener) {
        mClickListener = clickListener;
    }

    @Override
    public int getItemViewType(int position) {
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

                lectureHolder.setClickListener(lecture);
                break;
            case WEEK_TYPE:
                WeekHolder weekHolder = (WeekHolder) holder;
                weekHolder.mWeekName.setText((String) mLecturesAndWeeks.get(position));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mLecturesAndWeeks == null ? 0 : mLecturesAndWeeks.size();
    }

    public void setLectures(List<Lecture> lectures) {
        mLectures = lectures;
        mLecturesAndWeeks = new ArrayList<>();
        if (mGroupByWeek) {
            int weekIndex = -1;
            for (Lecture lecture : lectures) {
                if (lecture.getWeekIndex() > weekIndex) {
                    mLecturesAndWeeks.add(mWeekNames.get((weekIndex = lecture.getWeekIndex())));
                }
                mLecturesAndWeeks.add(lecture);
            }
        } else {
            if (lectures != null) {
                mLecturesAndWeeks = new ArrayList<Object>(lectures);
            }
        }
    }

    public void setWeekNames(List<String> weekNames) {
        mWeekNames = weekNames;
    }

    public void groupByWeek(boolean groupByWeek) {
        mGroupByWeek = groupByWeek;
        setLectures(mLectures);
    }

    private class LectureHolder extends RecyclerView.ViewHolder {
        private final TextView mNumber;
        private final TextView mDate;
        private final TextView mTheme;
        private final TextView mLecturer;

        private LectureHolder(@NonNull final View itemView) {
            super(itemView);
            mNumber = itemView.findViewById(R.id.number);
            mDate = itemView.findViewById(R.id.date);
            mTheme = itemView.findViewById(R.id.theme);
            mLecturer = itemView.findViewById(R.id.lecturer);
        }

        private void setClickListener(final Lecture lecture) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mClickListener.onClick(lecture);
                }
            });
        }
    }

    private static class WeekHolder extends RecyclerView.ViewHolder {
        private final TextView mWeekName;

        private WeekHolder(@NonNull View itemView) {
            super(itemView);
            mWeekName = itemView.findViewById(R.id.week_name);
        }
    }

    public int getNextLectureIndex() {
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
        for (Lecture lecture : mLectures) {
            try {
                Date lectureDate = format.parse(lecture.getDate());
                Date currentDate = new Date();
                if (lectureDate != null && lectureDate.after(currentDate)) {
                    return mLecturesAndWeeks.indexOf(lecture);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }
}
