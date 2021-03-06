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

    private String backgroundAudioPath;
    private String soundEffectPath;

    private InputStream backgroundAudioInputStream;
    private InputStream fireballInputStream;
    private AudioStream backgroundAudioAudioStream;
    private AudioStream fireballAudioStream;

    public boolean playback;
    
    private AudioData audioData;
    private ContinuousAudioDataStream loop = null;

    public String background_audio = "preliminary_background_music_return_of_warcraft.wav";
    public String fireball_throw = "fireball_throw_v2.wav";
    public String fireball_hit = "fireball_hard_hit_v2.wav";
    
    Thread t = new Thread();
    
    //plays the background audio, supposed to loop but plays only once for now
    private void startBackgroundAudio() {
        try {
            System.out.println("finding media file");
            
            System.out.println("path to background audio: " + backgroundAudioPath);

            backgroundAudioInputStream = new FileInputStream(backgroundAudioPath);
            backgroundAudioAudioStream = new AudioStream(backgroundAudioInputStream);
            
//            audioData = backgroundAudioAudioStream.getData();
//            loop = new ContinuousAudioDataStream(audioData);

            System.out.println("media file has been found");
            System.out.println("trying to play...");

            AudioPlayer.player.start(backgroundAudioAudioStream);
            
            System.out.println("background media is playing");
            
            do{
                try {
                    t.sleep(1);
                } catch (InterruptedException ex) {
                    Logger.getLogger(AudioHandler.class.getName()).log(Level.SEVERE, null, ex);
                }
            } while(backgroundAudioAudioStream.available() > 0 && playback);
            if (playback) {
                startBackgroundAudio();
                
            }
            
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
            System.out.println("path to sound effect: " + soundEffectPath);

            fireballInputStream = new FileInputStream(soundEffectPath);
            fireballAudioStream = new AudioStream(fireballInputStream);

            System.out.println("media file has been found");
            System.out.println("trying to play sound effect...");

            AudioPlayer.player.start(fireballAudioStream);

            System.out.println("sound effect is playing");
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
        playback = false;
        System.out.println("background audio stopped");
        AudioPlayer.player.stop(backgroundAudioAudioStream);
        AudioPlayer.player.stop(fireballAudioStream);
    }

    //defines the audio redirects to accurate method
    public void defineAudioPath(String audioName) {
//        backgroundAudioPath = System.getProperty("user.dir") + "\\src\\resources\\" + audioName;

        if (audioName == background_audio) {
            backgroundAudioPath = System.getProperty("user.dir") + "\\src\\resources\\" + audioName;
            startBackgroundAudio();
        } else if (audioName == fireball_throw) {
            soundEffectPath = System.getProperty("user.dir") + "\\src\\resources\\" + audioName;
            playSoundEffect();
        } else if (audioName == fireball_hit){
            soundEffectPath = System.getProperty("user.dir") + "\\src\\resources\\" + audioName;
            playSoundEffect();
        }

    }
}
