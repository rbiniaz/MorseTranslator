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

import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by evan on 9/15/13.
 */
public class NoteDetailActivity extends Activity implements SurfaceHolder.Callback {

    public Camera mCamera;
    public SurfaceHolder mHolder;
    public static long unitTime = 200;

    static long dot = unitTime;
    static long dash = unitTime*3;
    static long wait = unitTime;
    static long endLetter = unitTime*3;

    public static HashMap<String, long[]> TIMES = new HashMap<String, long[]>(){{
        put("a",new long[]{dot,wait,dash,endLetter});
        put("b",new long[] {dash, wait,dot,wait,dot,wait,dot, endLetter});
        put("c", new long[] {dash, wait,dot,wait,dash,wait,dot,endLetter});
        put("d", new long[] {dash, wait, dot, wait,dot,endLetter});
        put("e",new long[] {dot, endLetter});
        put("f", new long[]{dot, wait, dot,wait,dash,wait,dot, endLetter});
        put("g",new long[] {dash,wait,dash,wait,dot,endLetter});
        put("h",new long[] {dot,wait,dot,wait,dot,wait,dot,endLetter});
        put("i",new long[] {dot, wait,dot,endLetter});
        put("j", new long[] {dot,wait,dash,wait,dash,wait,dash,endLetter});
        put("k", new long[] {dash,wait,dot,wait,dash,endLetter});
        put("l", new long[] {dot, wait,dash,wait,dot,wait,dot,endLetter});
        put("m", new long[] {dash, wait,dash, endLetter});
        put("n", new long[] {dash, wait,dot,endLetter});
        put("n",new long[] {dash,wait,dot,endLetter});
        put("o", new long[] {dash, wait,dash,wait,dash,endLetter});
        put("p", new long[] {dot,wait,dash,wait,dash,wait,dot,endLetter});
        put("q", new long[] {dash, wait,dash,wait,dot,wait,dash,endLetter});
        put("r",new long[] {dot,wait,dash,wait,dot,endLetter});
        put("s",new long[]{dot,wait,dot,wait,dot,endLetter});
        put("t",new long[]{dash,endLetter});
        put("u",new long[] {dot,wait,dot,wait,dash,endLetter});
        put("v", new long[]{dot,wait,dot,wait,dot,wait,dash,endLetter});
        put("w",new long[] {dot,wait,dash,wait,dash,endLetter});
        put("x", new long[]{dash,wait,dot,wait,dot,wait,dash,endLetter});
        put("y",new long[] {dash,wait,dot,wait,dash,wait, dash,endLetter});
        put("z", new long[]{dash, wait,dash,wait,dot,wait,dot,endLetter});
    }
    };

    public static HashMap<String, String> MORSE = new HashMap<String, String>(){{
        put("a","*-");put("b","-***");put("c", "-*-*");put("d", "-**");
        put("e", "*");put("f","**-*");put("g","--*");
        put("h","****");put("i","**");put("j","*---");put("k","-*-");
        put("l","*-**");put("m","--");put("n","-*");put("n","-*");
        put("o","---");put("p","*--*"); put("q", "--*-");put("r","*-*");
        put("s","***");put("t","-");put("u","**-");put("v", "***-");
        put("w","*--");put("x", "-**-");put("y","-*--"); put("z","--**");

    }};


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


        final Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //vibrate
                ArrayList morse = readMorse("hello");

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
        new MyAsyncTask().execute();

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




    // _ denotes wait
    public ArrayList<String> englishToMorse(String english){
        String[] characters = english.split("");
        ArrayList<String> morseCode = new ArrayList<String>();
        for (int i = 0; i < english.length(); i++){
            if(this.MORSE.containsKey(characters[i])){
                morseCode.add(MORSE.get(characters[i]));
                morseCode.add("___");
            }
            else if (characters[i] == " "){
                morseCode.add("____");
            }
            else {}
        }
        return morseCode;
    }

    public ArrayList<Long> readMorse(String english){
        String[] characters = english.split("");
        ArrayList<Long> times = new ArrayList<Long>();
        for (int i = 0; i < english.length(); i++){
            if (this.TIMES.containsKey(characters[i])){
//                for (int j=0; j<TIMES.get())
                for (long j : TIMES.get(characters[i])){
                    times.add(j);
                }
            }
        }
        return times;

    }

    public Time[] readChar(String s){
        String[] t = s.split("");
        return null;
    }

    private class MyAsyncTask extends AsyncTask{


        @Override
        protected void onPreExecute() {
            Log.d("wolf", "onPreExecute");
            Camera.Parameters params = mCamera.getParameters();
            params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            mCamera.setParameters(params);
            mCamera.startPreview();

        }

        @Override
        protected void onPostExecute(Object o) {
            Log.d("wolf", "onPostExecute");

            Camera.Parameters params = mCamera.getParameters();
            params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            mCamera.setParameters(params);
            mCamera.stopPreview();
        }

        @Override
        protected Object doInBackground(Object[] params) {
            Log.d("wolf", "doInBackground");

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onCancelled() {
            Log.d("wolf", "onCancelled");

            Camera.Parameters params = mCamera.getParameters();
            params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            mCamera.setParameters(params);
            mCamera.stopPreview();
        }
    }

}
