package ru.sberbankmobile.learningprogram;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import ru.sberbankmobile.learningprogram.fragments.LecturesFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.root, LecturesFragment.newInstance())
                    .commit();
        }
    }
}