package com.example.translatorapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private static String TAG_CONTENTS = "contents";
    private static String TAG_TRANSLATED = "translated";
    private static String API_YODA_URL = "https://api.funtranslations.com/translate/yoda.json?text=";
    private static String API_PIRATE_URL = "https://api.funtranslations.com/translate/pirate.json?text=";

    Button yodaButton;
    Button pirateButton;
    EditText textEditor;
    TextView translationTextView;

    JSONObject contents = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        yodaButton = findViewById(R.id.yoda_button);
        pirateButton = findViewById(R.id.pirate_button);
        textEditor = findViewById(R.id.text_editor);
        translationTextView = findViewById(R.id.translation_text_view);

        yodaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textToTranslate = textEditor.getText().toString();
                String url = null;
                try {
                    url = API_YODA_URL + URLEncoder.encode(textToTranslate, "UTF-8");
                    new ParseTask().execute(url);
                    Log.d("JSONPARSE","at yodabutton");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

            }
        });

        pirateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textToTranslate = textEditor.getText().toString();
                String url = null;
                try {
                    url = API_PIRATE_URL + URLEncoder.encode(textToTranslate, "UTF-8");
                    new ParseTask().execute(url);
                    Log.d("JSONPARSE","at piratebutton");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    private class ParseTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            //ProgressBar bar = (ProgressBar) findViewById(R.id.progressbar);
            //bar.setVisibility(View.VISIBLE);
        }

        @Override   protected String doInBackground(String... params) {
            String url = params[0];
            // Creating JSON Parser instance
            JSONParser jParser = new JSONParser();
            // getting JSON string from URL
            JSONObject json = jParser.getJSONFromUrl(url);
            Log.d("JSONPARSE", "at doInBackground");
            if(json !=null) {
                Log.d("JSONPARSE", "json: " + json.toString());
            }
            String translated = null;


            try {
                // Getting Array of Contacts
                try {

                    if(json != null) contents = json.getJSONObject(TAG_CONTENTS);
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }

                //JSONObject c = contents.getJSONObject(i);

                    // Storing each json item in variable
                if(contents!=null) {
                    translated = contents.getString(TAG_TRANSLATED);
                    Log.d("JSONPARSE", "contents, translated: " + contents.getString(TAG_TRANSLATED));
                    Log.d("JSONPARSE", "translated: " + translated);
                }
                else {
                    translated = "Could not get translation at this time.";
                }



            } catch (JSONException e) {
                e.printStackTrace();
            }
            return translated;
        }

        protected void onPostExecute(String translatedText) {
            /**    * Updating parsed JSON data into TextView   * */
            Log.d("JSONPARSE", "at onPostExecute");
            Log.d("JSONPARSE", "translation: " + translatedText);
            translationTextView.setText(translatedText);
            //ProgressBar invisible
            //ProgressBar bar=(ProgressBar)findViewById(R.id.progressbar);
            //bar.setVisibility(View.INVISIBLE);
            }

    }
}
