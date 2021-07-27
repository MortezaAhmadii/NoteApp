package com.example.noteapp.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.noteapp.Listeners.NoteListeners;
import com.example.noteapp.R;
import com.example.noteapp.database.NoteDatabase;
import com.example.noteapp.databinding.ActivityCreateNoteBinding;
import com.example.noteapp.models.Note;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CreateNoteActivity extends AppCompatActivity {

    ActivityCreateNoteBinding binding;
     String selectedNoteColor="";

     private static int REQUEST_CODE_IMAGE=2;
     private  String selectedImagePath;

     private Note AlreadyNote;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding=ActivityCreateNoteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        binding.textDateTime.setText(new SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm a", Locale.getDefault()).format(new Date()));


        binding.imagesave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNote();
            }
        });

        //selectedImagePath="";
        selectedNoteColor="#333333";

        if (getIntent().getBooleanExtra("isView",false)){
            AlreadyNote= (Note) getIntent().getSerializableExtra("Note");
            isView();
        }
        initMiscellaneous();
        setSubtitleIndicatorColor();
    }

    private void isView(){

        binding.inputNoteTitle.setText(AlreadyNote.getTitle());
        binding.inputNoteSubTitle.setText(AlreadyNote.getSubtitle());
        binding.textDateTime.setText(AlreadyNote.getDateTime());
        binding.inputNote.setText(AlreadyNote.getNoteText());
        if (AlreadyNote.getImagePath() != null && !AlreadyNote.getImagePath().trim().isEmpty()){

            binding.imageNote.setImageBitmap(BitmapFactory.decodeFile(AlreadyNote.getImagePath()));
            binding.imageNote.setVisibility(View.VISIBLE);
            selectedImagePath=AlreadyNote.getImagePath();
        }
    }

    public void saveNote(){

        if (binding.inputNoteTitle.getText().toString().trim().isEmpty()){

            Toast.makeText(getApplicationContext(), getString(R.string.noteToast),Toast.LENGTH_SHORT).show();
            return;
        }else if (binding.inputNoteSubTitle.getText().toString().trim().isEmpty() && binding.inputNoteTitle.getText().toString().trim().isEmpty()){

            Toast.makeText(getApplicationContext(),getString(R.string.toastNote),Toast.LENGTH_SHORT).show();
            return;
        }

        final Note note=new Note();
        note.setTitle(binding.inputNoteTitle.getText().toString());
        note.setSubtitle(binding.inputNoteSubTitle.getText().toString());
        note.setNoteText(binding.inputNote.getText().toString());
        note.setDateTime(binding.textDateTime.getText().toString());
        note.setColor(selectedNoteColor);
        note.setImagePath(selectedImagePath);

        Log.e("","");

        @SuppressLint("StaticFieldLeak")
        class  SaveNoteTask extends AsyncTask<Void,Void,Void>{


            @Override
            protected Void doInBackground(Void... voids) {

                NoteDatabase.getDatabase(getApplicationContext()).idao().insertNote(note);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                Intent intent =new Intent();
                setResult(RESULT_OK,intent);
                finish();
            }

        }
        new SaveNoteTask().execute();



        if (AlreadyNote !=null){
            note.setId(AlreadyNote.getId());
        }

    }

    private void initMiscellaneous(){

        final LinearLayout layoutMiscellneous=findViewById(R.id.layout_miscellaneous);
        final BottomSheetBehavior<LinearLayout> bottomSheetBehavior=BottomSheetBehavior.from(layoutMiscellneous);
        layoutMiscellneous.findViewById(R.id.text_miscellaneous).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bottomSheetBehavior.getState()!= BottomSheetBehavior.STATE_EXPANDED){
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

                }else {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });


        AppCompatImageView imageColor1=layoutMiscellneous.findViewById(R.id.imageColor1);
        AppCompatImageView imageColor2=layoutMiscellneous.findViewById(R.id.imageColor2);
        AppCompatImageView imageColor3=layoutMiscellneous.findViewById(R.id.imageColor3);
        AppCompatImageView imageColor4=layoutMiscellneous.findViewById(R.id.imageColor4);
        AppCompatImageView imageColor5=layoutMiscellneous.findViewById(R.id.imageColor5);


        layoutMiscellneous.findViewById(R.id.imageColor1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedNoteColor="#333333";
                imageColor1.setImageResource(R.drawable.ic_baseline_done_24);
                imageColor2.setImageResource(0);
                imageColor3.setImageResource(0);
                imageColor4.setImageResource(0);
                imageColor5.setImageResource(0);
                setSubtitleIndicatorColor();

            }
        });



        layoutMiscellneous.findViewById(R.id.imageColor2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedNoteColor="#fdbe3b";
                imageColor1.setImageResource(0);
                imageColor2.setImageResource(R.drawable.ic_baseline_done_24);
                imageColor3.setImageResource(0);
                imageColor4.setImageResource(0);
                imageColor5.setImageResource(0);
                setSubtitleIndicatorColor();

            }
        });


        layoutMiscellneous.findViewById(R.id.imageColor3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedNoteColor="#ff4842";
                imageColor1.setImageResource(0);
                imageColor2.setImageResource(0);
                imageColor3.setImageResource(R.drawable.ic_baseline_done_24);
                imageColor4.setImageResource(0);
                imageColor5.setImageResource(0);
                setSubtitleIndicatorColor();

            }
        });


        layoutMiscellneous.findViewById(R.id.imageColor4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedNoteColor="#3a52fc";
                imageColor1.setImageResource(0);
                imageColor2.setImageResource(0);
                imageColor3.setImageResource(0);
                imageColor4.setImageResource(R.drawable.ic_baseline_done_24);
                imageColor5.setImageResource(0);
                setSubtitleIndicatorColor();

            }
        });


        layoutMiscellneous.findViewById(R.id.imageColor5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedNoteColor="#000000";
                imageColor1.setImageResource(0);
                imageColor2.setImageResource(0);
                imageColor3.setImageResource(0);
                imageColor4.setImageResource(0);
                imageColor5.setImageResource(R.drawable.ic_baseline_done_24);
                setSubtitleIndicatorColor();

            }
        });

        if (AlreadyNote !=null && AlreadyNote.getColor() !=null && !AlreadyNote.getColor().trim().isEmpty() ){


            switch (AlreadyNote.getColor()){

                case "#fdbe3b":
                    layoutMiscellneous.findViewById(R.id.imageColor2);
                    break;

                case "#ff4842":
                    layoutMiscellneous.findViewById(R.id.imageColor3);
                    break;

                case "#3a52fc":
                    layoutMiscellneous.findViewById(R.id.imageColor4);
                    break;


                case "#000000":
                    layoutMiscellneous.findViewById(R.id.imageColor5);
                    break;



            }
        }


        layoutMiscellneous.findViewById(R.id.layoutAddImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                selectImage();

                Dexter.withContext(getApplicationContext()).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE).withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {

                        selectImage();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                        
                        Toast.makeText(getApplicationContext(),"permission Denied",Toast.LENGTH_SHORT).show();
                        
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

                        
                    }
                });
            }
        });

    }

    public void setSubtitleIndicatorColor(){
        GradientDrawable gradientDrawable=(GradientDrawable)binding.ViewSubtitleIndicator.getBackground();
        gradientDrawable.setColor(Color.parseColor(selectedNoteColor));

    }


    public void selectImage(){
         Intent intent=new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(intent,REQUEST_CODE_IMAGE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ( resultCode==RESULT_OK && requestCode==REQUEST_CODE_IMAGE){

            if (data!=null){
                Uri selectedImageUri=data.getData();
                if (selectedImageUri!=null){
                    try {
                        InputStream inputStream= getContentResolver().openInputStream(selectedImageUri);
                        Bitmap bitmap= BitmapFactory.decodeStream(inputStream);
                        binding.imageNote.setImageBitmap(bitmap);
                        binding.imageNote.setVisibility(View.VISIBLE);

                        //selectedImagePath=
                       selectedImagePath=getPathFromUri(selectedImageUri);
                        Log.e("","");

                    }catch (Exception exception){
                        Toast.makeText(getApplicationContext(),exception.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }

    }



    private String getPathFromUri(Uri contentUri){
        String filePath;
        Cursor cursor=getContentResolver().query(contentUri,null,null,null,null);
        if (cursor==null){
            filePath=contentUri.getPath();
        }else {
            cursor.moveToFirst();
            int index=cursor.getColumnIndex("_data");
            filePath=cursor.getString(index);
            cursor.close();
        }
        return filePath;
    }

}