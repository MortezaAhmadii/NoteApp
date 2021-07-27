package com.example.noteapp.database;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.noteapp.Dao.IDAO;
import com.example.noteapp.models.Note;

@Database(entities = Note.class,version = 1,exportSchema = false)
public abstract class NoteDatabase extends RoomDatabase {

    public abstract  IDAO idao();

    private static NoteDatabase database=null;

    public static synchronized NoteDatabase getDatabase(Context context){

        if (database==null){

        database= Room.databaseBuilder(context,NoteDatabase.class,"note_db").build();

        }

        return database;
    }


}
