package com.example.mynotes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static com.example.mynotes.ArchiveActivity.ADate;
import static com.example.mynotes.ArchiveActivity.ATitle;
import static com.example.mynotes.ArchiveActivity.Anotes;
import static com.example.mynotes.MainActivity.notes;

public class ArchiveNotesActivity extends AppCompatActivity {

    static EditText editText;
    SharedPreferences sharedPreferences;
    EditText titleEditText;
    int NotesIndex;
    TextToSpeech textToSpeech;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.notes_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        switch(item.getItemId()){

            case R.id.addNote:

                Intent intent= new Intent(getApplicationContext(),ArchiveNotesActivity.class);
                Anotes.add("");
                ATitle.add("");
                Log.i("reached","onOptionitems");
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm yyyy-MM-dd");

                        ADate.add(sdf.format(new Date()));
                sharedPreferences=getApplicationContext().getSharedPreferences("com.example.mynotes", Context.MODE_PRIVATE);
                try {
                    sharedPreferences.edit().putString("Adate",ObjectSerializer.serialize(ADate)).apply();
                    sharedPreferences.edit().putString("Anote",ObjectSerializer.serialize(Anotes)).apply();
                    sharedPreferences.edit().putString("Atitle",ObjectSerializer.serialize(ATitle)).apply();                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                ArchiveAdapt.Index=Anotes.size()-1;
                ArchiveActivity.customAdapter2.notifyDataSetChanged();
                intent.putExtra("notesIndex",ArchiveAdapt.Index);

                System.out.println("**"+ADate);
                System.out.println("**"+Anotes);
                System.out.println("**"+ATitle);
                startActivity(intent);
                return true;
            case R.id.back:finish();
                return true;
            case R.id.clear:editText.setText("");
                return true;
            case R.id.speak:     int speech=textToSpeech.speak(Anotes.get(NotesIndex),TextToSpeech.QUEUE_FLUSH,null);

                return true;
            case R.id.share:
                Intent intent1=new Intent(Intent.ACTION_SEND);
                intent1.setType("text/plain");
                intent1.putExtra(Intent.EXTRA_TEXT,Anotes.get(NotesIndex));
                startActivity(intent1.createChooser(intent1,"Your Note to be shared"));
                return  true;

        }
        return false;
    }
    public void speak(){



    }
    public void getSpeechInput(View view) {

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, 10);
        } else {
            Toast.makeText(this, "Your Device Don't Support Speech Input", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 10:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    editText.setText(result.get(0));
                }
                break;
        }
    }

    public void cam(View view){

        Intent in=new Intent(getApplicationContext(),camera.class);
        startActivity(in);

    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_archive_notes);
        Log.i("reached","oncreate Notes Activity");
        textToSpeech=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status==TextToSpeech.SUCCESS){
                    int s=textToSpeech.setLanguage(Locale.getDefault());
                }
            }
        });
        editText=findViewById(R.id.editText);
        titleEditText =  findViewById(R.id.titleEditText);

        Intent intent =getIntent();
        NotesIndex=intent.getIntExtra("notesIndex",-1);
        Log.i("reached","before if clause");

        if(NotesIndex!= -1){
            Log.i("reached ","notes activity if statment");
            editText.setText(Anotes.get(NotesIndex));

            titleEditText.setText(ATitle.get(NotesIndex));
        }
        Log.i("reached","before if clause");

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                Anotes.set(NotesIndex,String.valueOf(s));
                ArchiveActivity.customAdapter2.notifyDataSetChanged();
                sharedPreferences=getApplicationContext().getSharedPreferences("com.example.mynotes", Context.MODE_PRIVATE);
                try {
                    sharedPreferences.edit().putString("Anote",ObjectSerializer.serialize(Anotes)).apply();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        titleEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ATitle.set(NotesIndex,String.valueOf(s));
                ArchiveActivity.customAdapter2.notifyDataSetChanged();
                sharedPreferences=getApplicationContext().getSharedPreferences("com.example.mynotes", Context.MODE_PRIVATE);
                try {
                    sharedPreferences.edit().putString("Atitle",ObjectSerializer.serialize(ATitle)).apply();
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });




    }
}
