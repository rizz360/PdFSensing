package com.rizz.pdf.pdfsensing.Handlers;

import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.util.Log;

import java.io.IOException;

/**
 * Created by Rizz on 01/10/2015.
 * Handles audio record, playing and triggers file management related to these actions
 */
public class AudioHandler {
    private static final String LOG_TAG = "AudioHandler";
    private static AudioHandler audioHandler = null;
    private static MediaPlayer mediaPlayer = null;
    private static MediaRecorder mediaRecorder = null;
    private static boolean isRecording = false;

    private AudioHandler() {
    }

    public static void init(Activity activity) {
        if(audioHandler == null) audioHandler = new AudioHandler();
    }

    public static int getLastRecordingsLength() {
        if(mediaPlayer == null) return 0;
        return mediaPlayer.getDuration();
    }
    //something strange is going on here...
    public static int getMaxAmplitude() {
        if (mediaRecorder == null) return 0;
        return  mediaRecorder.getMaxAmplitude();
    }

    public static void startPlaying() {
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(FileHandler.getRecFileName());
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            Log.e(LOG_TAG, "mediaPlayer.prepare() failed");
        }
    }

    public static void stopPlaying() {
        mediaPlayer.release();
        mediaPlayer = null;
    }

    public static void startRecording() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        FileHandler.updateFileNameTemplate();
        mediaRecorder.setOutputFile(FileHandler.getRecFileName());
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
        } catch (IOException e) {
            Log.e(LOG_TAG, "mediaRecorder.prepare() or mediaRecorder.start() failed");
        }

        isRecording = true;
    }

    public static boolean isRecording() {
        return isRecording;
    }


    public static void stopRecording() {
        if (!isRecording()) return;
        mediaRecorder.stop();
        mediaRecorder.release();
        FileHandler.writeMetaFile();
        mediaRecorder = null;
        isRecording = false;
    }

    public static void onPause() {
        if (mediaRecorder != null) {
            mediaRecorder.release();
            mediaRecorder = null;
        }

        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
