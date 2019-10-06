package ru.sberbankmobile.learningprogram;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

import ru.sberbankmobile.learningprogram.models.Lecture;

public class MainActivity extends AppCompatActivity {

    private static final int POSITION_ALL = 0;

    private LearningProgramProvider mLearningProgramProvider = new LearningProgramProvider();
    private LearningProgramAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initRecyclerView();
        initGroupDividerSpinner();
        initSpinner();
    }

    private void initGroupDividerSpinner() {
        Spinner divider = findViewById(R.id.group_divider);
        final List<String> groupTypes = mLearningProgramProvider.provideGroupTypes();
        divider.setAdapter(new GroupDividerAdapter(groupTypes));

        divider.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String groupType = groupTypes.get(position);
                if (position == LearningProgramAdapter.LECTURE_TYPE) {
                    mAdapter.groupByWeek(false);
                    mAdapter.notifyDataSetChanged();
                } else if (position == LearningProgramAdapter.WEEK_TYPE) {
                    mAdapter.groupByWeek(true);
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initSpinner() {
        Spinner spinner = findViewById(R.id.lectors_spinner);
        final List<String> lecturers = mLearningProgramProvider.provideLecturers();
        Collections.sort(lecturers);
        lecturers.add(POSITION_ALL, getResources().getString(R.string.all));
        spinner.setAdapter(new LecturerSpinnerAdapter(lecturers));

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String lecturer = lecturers.get(position);
                List<Lecture> filteredLectures;
                if (position == POSITION_ALL) {
                    filteredLectures = mLearningProgramProvider.provideLectures();
                } else {
                    filteredLectures = mLearningProgramProvider.filterBy(lecturer);
                }
                mAdapter.setLectures(filteredLectures);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.learning_program_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new LearningProgramAdapter();
        mAdapter.setLectures(mLearningProgramProvider.provideLectures());
        mAdapter.setWeekNames(mLearningProgramProvider.provideWeekNames());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(mAdapter);
    }
}
