/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaException;
import javafx.scene.media.MediaPlayer;
import javax.sound.sampled.AudioInputStream;
import sun.audio.AudioData;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;
import sun.audio.ContinuousAudioDataStream;

/**
 *
 * @author Mohini
 */
public class AudioHandler {

//    String audioName;
//
//    private URL resource;
//    private MediaPlayer backgroundAudio;
//    private Media backgroundAudioPath;

    private String path;

    private InputStream backgroundAudioInputStream;
    private InputStream fireballInputStream;
    private AudioStream backgroundAudioAudioStream;
    private AudioStream fireballAudioStream;

    private AudioData audioData;
    private ContinuousAudioDataStream loop = null;

    public String backgroundAudio = "preliminary_background_music_return_of_warcraft.wav";
    public String fireballAudio = "fireball_throw.wav";
    
    //plays the background audio, supposed to loop but plays only once for now
    private void startBackgroundAudio() {
        try {
            System.out.println("finding media file");

//            System.out.println(System.getProperty("user.dir"));
//            resource = getClass().getResource("C:\\Users\\Mohini\\Documents\\NetBeansProjects\\Return-of-Warlocks\\src\\resources\\preliminary_background_music_return_of_warcraft.wav");
//            System.out.println(resource.toString());
//            backgroundAudioPath = new Media(resource.toString());
//            backgroundAudio = new MediaPlayer(backgroundAudioPath);
//            backgroundAudio.play();
            
            System.out.println("path to audio: " + path);

            backgroundAudioInputStream = new FileInputStream(path);
            backgroundAudioAudioStream = new AudioStream(backgroundAudioInputStream);
            
//            audioData = backgroundAudioAudioStream.getData();
//            loop = new ContinuousAudioDataStream(audioData);

            System.out.println("media file has been found");
            System.out.println("trying to play...");

            AudioPlayer.player.start(backgroundAudioAudioStream);
//            AudioPlayer.player.start(loop);
            
            System.out.println("media is playing");
        } catch (NullPointerException e) {
            System.out.println("could not load audio file");
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            System.out.println("audio parameter is wrong");
            e.printStackTrace();
        } catch (MediaException e) {
            System.out.println("other kind of problem with audio");
            e.printStackTrace();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(AudioHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AudioHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //plays soundeffects, no need for looping the audio
    private void playSoundEffect() {
        try {
            System.out.println("finding media file");
            System.out.println("path to audio: " + path);

            fireballInputStream = new FileInputStream(path);
            fireballAudioStream = new AudioStream(backgroundAudioInputStream);

            System.out.println("media file has been found");
            System.out.println("trying to play...");

            AudioPlayer.player.start(backgroundAudioAudioStream);

            System.out.println("media is playing");
        } catch (NullPointerException e) {
            System.out.println("could not load audio file");
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            System.out.println("audio parameter is wrong");
            e.printStackTrace();
        } catch (MediaException e) {
            System.out.println("other kind of problem with audio");
            e.printStackTrace();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(AudioHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AudioHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //stops the playing audio
    public void stop() {
        System.out.println("background audio stopped");
        AudioPlayer.player.stop(backgroundAudioAudioStream);
    }

    //defines the audio redirects to accurate method
    public void defineAudioPath(String audioName) {
        path = System.getProperty("user.dir") + "\\src\\resources\\" + audioName;

        if (audioName == backgroundAudio) {
            startBackgroundAudio();
        } else if (audioName == fireballAudio) {
            playSoundEffect();
        }

    }
}
