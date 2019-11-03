package ru.sberbankmobile.learningprogram.models;

import androidx.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class Lecture {

    private static final int LECTURES_PER_WEEK = 3;

    private final String mNumber;
    private final String mDate;
    private final String mTheme;
    private final String mLecturer;
    private final int mWeekIndex;
    private final List<String> mSubtopics;

    @JsonCreator
    public Lecture(
            @JsonProperty("number") @NonNull String number,
            @JsonProperty("date") @NonNull String date,
            @JsonProperty("theme") @NonNull String theme,
            @JsonProperty("lector") @NonNull String lecturer,
            @JsonProperty("subtopics") @NonNull List<String> subtopics
    ) {
        mNumber = number;
        mDate = date;
        mTheme = theme;
        mLecturer = lecturer;
        mWeekIndex = (Integer.parseInt(mNumber) - 1) / LECTURES_PER_WEEK;
        mSubtopics = new ArrayList<>(subtopics);
    }

    public String getNumber() {
        return mNumber;
    }

    public String getDate() {
        return mDate;
    }

    public String getTheme() {
        return mTheme;
    }

    public String getLecturer() {
        return mLecturer;
    }

    public int getWeekIndex() {
        return mWeekIndex;
    }

    public List<String> getSubtopics() {
        return mSubtopics;
    }
}
