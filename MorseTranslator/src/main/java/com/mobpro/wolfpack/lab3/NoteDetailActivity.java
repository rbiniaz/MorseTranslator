package com.mobpro.wolfpack.lab3;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by evan on 9/15/13.
 */
public class NoteDetailActivity extends Activity implements SurfaceHolder.Callback {

    public Camera mCamera;
    public SurfaceHolder mHolder;
    HashMap<String,String> morse;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");

        TextView morseText = (TextView) findViewById(R.id.morse_text);
        Button vibrateButton = (Button) findViewById(R.id.vibrate_button);
        Button flashButton = (Button) findViewById(R.id.flash_button);

        //flash shit
        SurfaceView preview = (SurfaceView) findViewById(R.id.surfaceView);

        mHolder = preview.getHolder();
        mHolder.addCallback(this);
        mCamera = Camera.open();
        try{
            mCamera.setPreviewDisplay(mHolder);
        }catch (IOException e){
            Log.e("IOException", e.getMessage());
        }

        //Morse-translation related code
        this.morse = getMorse();


        final Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //vibrate
                vibrator.vibrate(1000);
            }
        });

        flashButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //flash
                flashForTime(1000);
            }
        });

        morseText.setText(name);
    }

    private void flashForTime(int t){
        //flashes
        Camera.Parameters params = mCamera.getParameters();
        params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        mCamera.setParameters(params);
        mCamera.startPreview();
        try {
            wait(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
        mCamera.setParameters(params);
        mCamera.stopPreview();
        //new MyAsyncTask().execute();

    }


    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {}

    public void surfaceCreated(SurfaceHolder holder) {
        mHolder = holder;
        try{
            mCamera.setPreviewDisplay(mHolder);
        }catch (IOException e){
            Log.e("IOException", e.getMessage());
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {

        mCamera.stopPreview();
        mCamera.release();
        mHolder = null;
    }

    public HashMap<String, String> getMorse() {
        return new HashMap<String, String>(){{
            put("a","*-");put("b","-***");put("c", "-*-*");put("d", "-**");
            put("d","-**");put("e","*");put("f","**-*");put("g","--*");
            put("h","****");put("i","**");put("j","*---");put("k","-*-");
            put("l","*-**");put("m","--");put("n","-*");put("n","-*");
            put("o","---");put("p","*--*"); put("q", "--*-");put("r","*-*");
            put("s","***");put("t","-");put("u","**-");put("v", "***-");
            put("w","*--");put("x", "-**-");put("y","-*--"); put("z","--**");

        }};
    }

    public ArrayList<String> englishToMorse(String english){
        String[] characters = english.split("");
        ArrayList<String> morseCode = new ArrayList<String>();
        for (int i = 0; i == english.length(); i++){
            if(this.morse.containsKey(characters[i])){
                morseCode.add(morse.get(characters[i]));
                morseCode.add("___");
            }
            else if (characters[i] == " "){
                morseCode.add("____");
            }
            else {}
        }
        return morseCode;
    }

    public void readMorse(ArrayList<String> morseInput){

    }

    public Time[] readChar(String s){
        String[] t = s.split("");
        return null;
    }
    /*
    private class MyAsyncTask extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                wait(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Camera.Parameters params = mCamera.getParameters();
            params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            mCamera.setParameters(params);
            mCamera.stopPreview();
           return null;
        }
    }*/
}
