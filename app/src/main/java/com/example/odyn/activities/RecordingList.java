package com.example.odyn.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.odyn.R;
/**
 * Jest to aktywność odpowiadająca za wyświetlanie listy nagrań.
 */
public class RecordingList extends AppCompatActivity {

    /**
     * Jest to metoda tworząca listę nagrań.
     * @param savedInstanceState Wiązka argumentów
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recording_list);
    }
}