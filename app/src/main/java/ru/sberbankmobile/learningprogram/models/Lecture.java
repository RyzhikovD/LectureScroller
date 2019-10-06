package ru.sberbankmobile.learningprogram.models;

import androidx.annotation.NonNull;

public class Lecture {

    private static final int LECTURES_PER_WEEK = 3;

    private final String mNumber;
    private final String mDate;
    private final String mTheme;
    private final String mLecturer;
    private final int weekIndex;

    public Lecture(
            @NonNull String mNumber,
            @NonNull String mDate,
            @NonNull String mTheme,
            @NonNull String mLecturer
    ) {
        this.mNumber = mNumber;
        this.mDate = mDate;
        this.mTheme = mTheme;
        this.mLecturer = mLecturer;
        this.weekIndex = (Integer.parseInt(mNumber) - 1) / LECTURES_PER_WEEK;
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
        return weekIndex;
    }
}
