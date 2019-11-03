package ru.sberbankmobile.learningprogram;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.List;

import ru.sberbankmobile.learningprogram.adapters.GroupingSpinnerAdapter;
import ru.sberbankmobile.learningprogram.adapters.LecturerSpinnerAdapter;
import ru.sberbankmobile.learningprogram.adapters.LecturesAdapter;
import ru.sberbankmobile.learningprogram.models.Lecture;
import ru.sberbankmobile.learningprogram.providers.LearningProgramProvider;

public class MainActivity extends AppCompatActivity {

    private static final int POSITION_ALL = 0;

    private LearningProgramProvider mLearningProgramProvider;
    private LecturesAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLearningProgramProvider = new LearningProgramProvider(getResources());
        if (mLearningProgramProvider.provideLectures() != null) {
            initRecyclerView(savedInstanceState == null);
            initGroupDividerSpinner();
            initSpinner();
        } else {
            new LoadLecturesTask(this, savedInstanceState == null).execute();
        }
    }

    private void initGroupDividerSpinner() {
        Spinner divider = findViewById(R.id.group_divider);
        final List<String> groupTypes = mLearningProgramProvider.provideGroupTypes();
        divider.setAdapter(new GroupingSpinnerAdapter(groupTypes));

        divider.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == LecturesAdapter.LECTURE_TYPE) {
                    mAdapter.groupByWeek(false);
                    mAdapter.notifyDataSetChanged();
                } else if (position == LecturesAdapter.WEEK_TYPE) {
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

    private void initRecyclerView(boolean isFirstStart) {
        RecyclerView recyclerView = findViewById(R.id.learning_program_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new LecturesAdapter();
        mAdapter.setLectures(mLearningProgramProvider.provideLectures());
        mAdapter.setWeekNames(mLearningProgramProvider.provideWeekNames());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(mAdapter);
        if (isFirstStart) {
            recyclerView.scrollToPosition(mAdapter.getNextLectureIndex());
        }
    }

    private static class LoadLecturesTask extends AsyncTask<Void, Void, List<Lecture>> {
        private final WeakReference<MainActivity> mMainActivityReference;
        private final LearningProgramProvider mProvider;
        private final boolean mIsFirstStart;

        private LoadLecturesTask(@NonNull MainActivity mainActivity, boolean isFirstStart) {
            mMainActivityReference = new WeakReference<>(mainActivity);
            mProvider = mainActivity.mLearningProgramProvider;
            mIsFirstStart = isFirstStart;
        }

        @Override
        protected List<Lecture> doInBackground(Void... arg) {
            return mProvider.loadLecturesFromWeb();
        }

        @Override
        protected void onPostExecute(List<Lecture> lectures) {
            MainActivity mainActivity = mMainActivityReference.get();
            if (mainActivity == null) {
                return;
            }
            if (lectures == null) {
                Toast.makeText(mainActivity.getApplicationContext(), R.string.failed_to_load_lectures, Toast.LENGTH_SHORT).show();
            } else {
                mainActivity.initRecyclerView(mIsFirstStart);
                mainActivity.initGroupDividerSpinner();
                mainActivity.initSpinner();
            }
        }
    }
}
