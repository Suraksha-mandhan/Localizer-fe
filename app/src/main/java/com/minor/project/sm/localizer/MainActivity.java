package com.minor.project.sm.localizer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowInsets;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.minor.project.sm.localizer.model.LocalizationRequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Spinner spinner1;
    private Button btnSubmit;
    private EditText textToTranslate;
    private EditText translatedText;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        populateLanguageSpinner();
        addListenerOnButton();
        addListenerOnSelectedItem();
    }

    public void addListenerOnSelectedItem() {
        LocalizationRequest request = new LocalizationRequest();
        spinner1 = (Spinner) findViewById(R.id.spinner1);
        CustomOnItemSelectedListener listener = CustomOnItemSelectedListener.getInstance();
        spinner1.setOnItemSelectedListener(listener);
    }

    public void populateLanguageSpinner() {
        spinner1 = (Spinner) findViewById(R.id.spinner1);
        LocalizationClient.populateSpinnerWithSupportedLanguages(spinner1, MainActivity.this);
    }
    // get the selected dropdown list value
    public void addListenerOnButton() {
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        textToTranslate = (EditText) findViewById(R.id.textToTranslate);
        translatedText = (EditText) findViewById(R.id.translatedText);
        String text = textToTranslate.getText().toString();
        btnSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String text = String.valueOf(textToTranslate.getText());
                String toLanguage = String.valueOf(spinner1.getSelectedItem());
                LocalizationClient.getTranslatedText(MainActivity.this,
                        text,
                        toLanguage,
                        translatedText);
            }
        });
    }
}