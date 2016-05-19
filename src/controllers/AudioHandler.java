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
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

/**
 *
 * @author Mohini
 */
public class AudioHandler {
    
    String audioName;

    private URL resource;
    private MediaPlayer backgroundAudio;
    private Media backgroundAudioPath;
    
    InputStream is;
    AudioStream as;
    
    public void startBackgroundAudio() {
        try {
            System.out.println("finding media file");
            System.out.println(System.getProperty("user.dir"));
            
            
//            resource = getClass().getResource("C:\\Users\\Mohini\\Documents\\NetBeansProjects\\Return-of-Warlocks\\src\\resources\\preliminary_background_music_return_of_warcraft.wav");
//            System.out.println(resource.toString());
//            backgroundAudioPath = new Media(resource.toString());
//            backgroundAudio = new MediaPlayer(backgroundAudioPath);
//            backgroundAudio.play();
            
            System.out.println(System.getProperty("user.dir") + "\\Return-of-Warlocks\\src\\resources\\preliminary_background_music_return_of_warcraft.wav");
            
            is = new FileInputStream(System.getProperty("user.dir") + "\\Return-of-Warlocks\\src\\resources\\preliminary_background_music_return_of_warcraft.wav");
            as = new AudioStream(is);
            
            System.out.println("media file has been found");
            System.out.println("trying to play...");
            
            AudioPlayer.player.start(as);

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

    //This method turns off the specified sound in the class it's called from, whatever sound it is that it is playing
    public void stop() {
        System.out.println("background audio stopped");
        AudioPlayer.player.stop(is);
    }
}
