package ru.sberbankmobile.learningprogram.providers;

import android.content.res.Resources;

import androidx.annotation.Nullable;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ru.sberbankmobile.learningprogram.R;
import ru.sberbankmobile.learningprogram.models.Lecture;

public class LearningProgramProvider {

    private static final String LECTURES_URL = "http://landsovet.ru/learning_program.json";
    private List<Lecture> mLectures;

    public List<Lecture> provideLectures() {
        return mLectures;
    }

    public List<String> provideLecturers() {
        Set<String> lecturersSet = new HashSet<>();
        for (Lecture lecture : mLectures) {
            lecturersSet.add(lecture.getLecturer());
        }
        return new ArrayList<>(lecturersSet);
    }

    public List<String> provideGroupTypes(Resources resources) {
        return Arrays.asList(resources.getString(R.string.do_not_group), resources.getString(R.string.group_by_week));
    }

    public List<String> provideWeekNames(Resources resources) {
        String[] mWeekNames = resources.getStringArray(R.array.week_names);
        return new ArrayList<>(Arrays.asList(mWeekNames));
    }

    public List<Lecture> filterBy(String lecturer) {
        List<Lecture> filteredLectures = new ArrayList<>();
        for (Lecture lecture : provideLectures()) {
            if (lecture.getLecturer().equals(lecturer)) {
                filteredLectures.add(lecture);
            }
        }
        return filteredLectures;
    }

    @Nullable
    public List<Lecture> loadLecturesFromWeb() {
        if (mLectures != null) {
            return mLectures;
        }
        InputStream is = null;
        try {
            final URL url = new URL(LECTURES_URL);
            URLConnection connection = url.openConnection();
            is = connection.getInputStream();
            ObjectMapper mapper = new ObjectMapper();
            Lecture[] lectures = mapper.readValue(is, Lecture[].class);
            mLectures = Arrays.asList(lectures);
            return new ArrayList<>(mLectures);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
