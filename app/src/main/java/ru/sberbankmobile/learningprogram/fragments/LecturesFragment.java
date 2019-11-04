package ru.sberbankmobile.learningprogram.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.List;

import ru.sberbankmobile.learningprogram.R;
import ru.sberbankmobile.learningprogram.adapters.GroupingSpinnerAdapter;
import ru.sberbankmobile.learningprogram.adapters.LecturerSpinnerAdapter;
import ru.sberbankmobile.learningprogram.adapters.LecturesAdapter;
import ru.sberbankmobile.learningprogram.adapters.OnItemClickListener;
import ru.sberbankmobile.learningprogram.models.Lecture;
import ru.sberbankmobile.learningprogram.providers.LearningProgramProvider;

public class LecturesFragment extends Fragment implements OnItemClickListener {

    private static final int POSITION_ALL = 0;

    private final LearningProgramProvider mLearningProgramProvider = new LearningProgramProvider();
    private LecturesAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private Spinner mLecturersSpinner;
    private Spinner mGroupDivider;

    public static Fragment newInstance() {
        return new LecturesFragment();
    }

    {
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_lectures, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = view.findViewById(R.id.learning_program_recycler);
        mLecturersSpinner = view.findViewById(R.id.lecturers_spinner);
        mGroupDivider = view.findViewById(R.id.group_divider_spinner);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        List<Lecture> lectures = mLearningProgramProvider.provideLectures();
        if (lectures == null) {
            new LoadLecturesTask(this, savedInstanceState == null).execute();
        } else {
            initRecyclerView(savedInstanceState == null);
            initSpinner();
            initGroupDividerSpinner();
        }
    }

    private void initGroupDividerSpinner() {
        final List<String> groupTypes = mLearningProgramProvider.provideGroupTypes(getResources());
        mGroupDivider.setAdapter(new GroupingSpinnerAdapter(groupTypes));

        mGroupDivider.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        final List<String> lecturers = mLearningProgramProvider.provideLecturers();
        Collections.sort(lecturers);
        lecturers.add(POSITION_ALL, getResources().getString(R.string.all));
        mLecturersSpinner.setAdapter(new LecturerSpinnerAdapter(lecturers));

        mLecturersSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

    @Override
    public void onClick(Lecture lecture) {
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.root, DetailsFragment.newInstance(lecture))
                .addToBackStack(DetailsFragment.class.getSimpleName())
                .commit();
    }

    private void initRecyclerView(boolean isFirstStart) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);

        mAdapter = new LecturesAdapter(this);
        mAdapter.setLectures(mLearningProgramProvider.provideLectures());
        mAdapter.setWeekNames(mLearningProgramProvider.provideWeekNames(getResources()));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(dividerItemDecoration);
        mRecyclerView.setAdapter(mAdapter);
        if (isFirstStart) {
            mRecyclerView.scrollToPosition(mAdapter.getNextLectureIndex());
        }
    }

    private static class LoadLecturesTask extends AsyncTask<Void, Void, List<Lecture>> {
        private final WeakReference<LecturesFragment> mFragmentReference;
        private final LearningProgramProvider mProvider;
        private final boolean mIsFirstStart;

        private LoadLecturesTask(@NonNull LecturesFragment fragment, boolean isFirstStart) {
            mFragmentReference = new WeakReference<>(fragment);
            mProvider = fragment.mLearningProgramProvider;
            mIsFirstStart = isFirstStart;
        }


        @Override
        protected List<Lecture> doInBackground(Void... arg) {
            return mProvider.loadLecturesFromWeb();
        }

        @Override
        protected void onPostExecute(List<Lecture> lectures) {
            LecturesFragment fragment = mFragmentReference.get();
            if (fragment == null) {
                return;
            }
            if (lectures == null) {
                Toast.makeText(fragment.requireContext(), R.string.failed_to_load_lectures, Toast.LENGTH_SHORT).show();
            } else {
                fragment.initRecyclerView(mIsFirstStart);
                fragment.initSpinner();
                fragment.initGroupDividerSpinner();
            }
        }
    }
}
