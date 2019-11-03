package ru.sberbankmobile.learningprogram.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ru.sberbankmobile.learningprogram.R;
import ru.sberbankmobile.learningprogram.adapters.DetailsAdapter;
import ru.sberbankmobile.learningprogram.models.Lecture;

public class DetailsFragment extends Fragment {

    private static final String ARG_NUMBER = "NUMBER";
    private static final String ARG_DATE = "DATE";
    private static final String ARG_THEME = "THEME";
    private static final String ARG_LECTURER = "LECTURER";
    private static final String ARG_SUBTOPICS = "SUBTOPICS";

    public static DetailsFragment newInstance(@NonNull Lecture lecture) {
        DetailsFragment fragment = new DetailsFragment();
        Bundle args = new Bundle();

        args.putString(ARG_NUMBER, lecture.getNumber());
        args.putString(ARG_DATE, lecture.getDate());
        args.putString(ARG_THEME, lecture.getTheme());
        args.putString(ARG_LECTURER, lecture.getLecturer());
        args.putStringArrayList(ARG_SUBTOPICS, (ArrayList<String>) lecture.getSubtopics());

        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Lecture lecture = getLectureFromArgs();
        ((TextView) view.findViewById(R.id.number)).setText(String.valueOf(lecture.getNumber()));
        ((TextView) view.findViewById(R.id.date)).setText(lecture.getDate());
        ((TextView) view.findViewById(R.id.theme)).setText(lecture.getTheme());
        ((TextView) view.findViewById(R.id.lecturer)).setText(lecture.getLecturer());

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_subtopics);
        DetailsAdapter adapter = new DetailsAdapter();
        adapter.setSubtopics(lecture.getSubtopics());
        recyclerView.setAdapter(adapter);
    }

    @NonNull
    private Lecture getLectureFromArgs() {
        Bundle arguments = getArguments();
        if (arguments == null) {
            throw new IllegalStateException("Arguments must be set");
        }

        String number = arguments.getString(ARG_NUMBER);
        String date = arguments.getString(ARG_DATE);
        String theme = arguments.getString(ARG_THEME);
        String lecturer = arguments.getString(ARG_LECTURER);
        List<String> subtopics = arguments.getStringArrayList(ARG_SUBTOPICS);

        if (number == null || date == null || theme == null || lecturer == null || subtopics == null) {
            throw new IllegalStateException("Lecture must be set");
        }

        return new Lecture(number, date, theme, lecturer, subtopics);
    }
}
