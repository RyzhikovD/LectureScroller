package ru.sberbankmobile.learningprogram.models;

import androidx.annotation.NonNull;

public class Lecture {
    private final String mNumber;
    private final String mDate;
    private final String mTheme;
    private final String mLecturer;

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
}
