package com.example.noteapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.noteapp.Activity.CreateNoteActivity;
import com.example.noteapp.Adopters.NoteAdopter;
import com.example.noteapp.Listeners.NoteListeners;
import com.example.noteapp.database.NoteDatabase;
import com.example.noteapp.databinding.ActivityMainBinding;
import com.example.noteapp.models.Note;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NoteListeners {

    public static final int REQUEST_CODE_ADD_NOTE=1;
    public static final int REQUEST_CODE_UPDATE_NOTE=2;
    public static final int REQUEST_CODE_SHOW_NOTE=3;
    private int noteClickedPosition=-1;


    NoteAdopter noteAdopter;
    List<Note>noteList;
    ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.imageAddNoteMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getApplicationContext(),CreateNoteActivity.class),REQUEST_CODE_ADD_NOTE);

            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();


        LoadRecycler();
        getAllNote(REQUEST_CODE_SHOW_NOTE);

    }

    private void LoadRecycler() {
        binding.noteRecycelerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        noteList=new ArrayList<>();
        noteAdopter=new NoteAdopter(getApplicationContext(),noteList,this);
        binding.noteRecycelerView.setAdapter(noteAdopter);
    }

    private void getAllNote(final int requestCode){

        @SuppressLint("StaticFieldLeak")
        class getAllNotesTask extends AsyncTask<Void,Void, List<Note>>{


            @Override
            protected List<Note> doInBackground(Void... voids) {
                return NoteDatabase.getDatabase(getApplicationContext()).idao().getAllNotes();

            }

            @Override
            protected void onPostExecute(List<Note> notes) {
                super.onPostExecute(notes);
               if (requestCode==REQUEST_CODE_SHOW_NOTE){
                   noteList.addAll(notes);
                   noteAdopter.notifyDataSetChanged();
               }
//               else if (requestCode==REQUEST_CODE_ADD_NOTE){
//                   noteList.add(0,notes.get(0));
//                   noteAdopter.notifyItemInserted(0);
//                   binding.noteRecycelerView.smoothScrollToPosition(0);
//
//               }
               else if (requestCode==REQUEST_CODE_UPDATE_NOTE){

                   noteList.remove(noteClickedPosition);
                   noteList.add(noteClickedPosition,notes.get(noteClickedPosition));
                   noteAdopter.notifyItemChanged(noteClickedPosition);
               }

            }
        }
        new getAllNotesTask().execute();



    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ( resultCode==RESULT_OK && requestCode==REQUEST_CODE_ADD_NOTE ){

            getAllNote(requestCode);

            if (requestCode ==REQUEST_CODE_ADD_NOTE && resultCode==RESULT_OK){

                getAllNote(REQUEST_CODE_ADD_NOTE);

            }else if(requestCode==REQUEST_CODE_UPDATE_NOTE && resultCode==RESULT_OK){

                if (data!=null){
                    getAllNote(REQUEST_CODE_UPDATE_NOTE);
                }
            }

        }
    }




    @Override
    public void onNoteClicked(Note note, int position) {

        noteClickedPosition=position;

        Intent intent=new Intent(getApplicationContext(),CreateNoteActivity.class);
        intent.putExtra("isView",true);
        intent.putExtra("Note",note);
        startActivityForResult(intent,REQUEST_CODE_UPDATE_NOTE);


    }
}