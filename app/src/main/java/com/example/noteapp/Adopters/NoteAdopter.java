package com.example.noteapp.Adopters;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.noteapp.Listeners.NoteListeners;
import com.example.noteapp.R;
import com.example.noteapp.models.Note;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

public class NoteAdopter extends RecyclerView.Adapter<NoteAdopter.NoteVH> {

    List<Note> noteList;
    Context context;
    LayoutInflater inflater;
    private NoteListeners noteListeners;

    public NoteAdopter(Context context,List<Note> noteList,NoteListeners noteListeners) {

        this.noteList = noteList;
        this.context=context;
        this.noteListeners=noteListeners;

    }


    @NonNull
    @Override
    public NoteVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.from(parent.getContext()).inflate(R.layout.item_continer_note, parent, false);
        return new NoteVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteVH holder, int position) {
        holder.setNote(noteList.get(position));
        holder.layoutNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                noteListeners.onNoteClicked(noteList.get(position),position);

            }
        });


    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    static class NoteVH extends RecyclerView.ViewHolder {


        AppCompatTextView textTitle, txtSubtitle, textDateTime;
        RelativeLayout layoutNote;
        RoundedImageView imageNoteShow;

        public NoteVH(@NonNull View itemView) {
            super(itemView);


            textTitle = itemView.findViewById(R.id.textTitle);
            txtSubtitle = itemView.findViewById(R.id.txtSubtitle);
            textDateTime = itemView.findViewById(R.id.textDateTime);
            layoutNote = itemView.findViewById(R.id.layoutNote);
            imageNoteShow = itemView.findViewById(R.id.imageNoteShow);
        }

       public void setNote(Note note) {

            if (note.getImagePath() != null) {
                imageNoteShow.setImageBitmap(BitmapFactory.decodeFile(note.getImagePath()));
                imageNoteShow.setVisibility(View.VISIBLE);
            } else {
                imageNoteShow.setVisibility(View.GONE);
            }


            textTitle.setText(note.getTitle());
            if (note.getSubtitle().trim().isEmpty()) {
                txtSubtitle.setVisibility(View.GONE);
            } else {
                txtSubtitle.setText(note.getSubtitle());
            }
            textDateTime.setText(note.getDateTime());

            GradientDrawable gradientDrawable = (GradientDrawable) layoutNote.getBackground();

            if (note.getColor() != null) {
                gradientDrawable.setColor(Color.parseColor(note.getColor()));
            } else {
                gradientDrawable.setColor(Color.parseColor("#333333"));
            }


        }




    }
}
