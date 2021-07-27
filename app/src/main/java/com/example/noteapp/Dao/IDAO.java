package com.example.noteapp.Dao;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.noteapp.models.Note;

import java.util.List;

@Dao
public interface IDAO {

    @Query("select * From notes ORDER BY id DESC")
    List<Note> getAllNotes();


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertNote(Note note);



}
