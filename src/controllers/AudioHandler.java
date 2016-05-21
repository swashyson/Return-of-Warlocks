/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.io.File;
import java.net.URL;
import java.nio.file.Paths;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaException;
import javafx.scene.media.MediaPlayer;

/**
 *
 * @author Mohini
 */
public class AudioHandler {

    private URL resource;
    private MediaPlayer backgroundAudio;
    private Media backgroundAudioPath;// = new Media(getClass().getResource("audioResources/preliminary_background_music_return_of_warlocks.wav").toString());
    private AudioClip audioClip;// = new AudioClip("/audioResources/preliminary_background_music_return_of_warlocks.wav");

    public void defineAudio() {
        try {
            System.out.println("finding media file");
            System.out.println(System.getProperty("user.dir"));

            resource = getClass().getResource("src/audioResources/preliminary_background_music_return_of_warlocks.mp3");
            backgroundAudioPath = new Media(resource.toString());
            //System.out.println(System.getProperty("user.dir"));
            //backgroundAudioPath = new Media(new File("file:///" + System.getProperty("user.dir").replace('\\', '/') + "/" + "preliminary_background_music_return_of_warlocks.wav").toURI().toString());
            System.out.println("media file has been found");
            System.out.println("trying to play...");
//            backgroundAudio = new MediaPlayer(backgroundAudioPath);
//            backgroundAudio.setCycleCount(MediaPlayer.INDEFINITE);
//            backgroundAudio.setVolume(0.5);
//            backgroundSound.setAutoPlay(true);
            //backgroundAudio.play();

//            audioClip = new AudioClip(resource.toString());
//            audioClip.play();
            play();

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
        }
    }

    private void play() {
        try {
            System.out.println("playing the sound");
            backgroundAudio = new MediaPlayer(backgroundAudioPath);
            backgroundAudio.setCycleCount(MediaPlayer.INDEFINITE);
            backgroundAudio.setVolume(0.5);
//      backgroundSound.setAutoPlay(true);
            backgroundAudio.play();

        //audioClip = new AudioClip(resource.toString());
            //audioClip.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //This method turns off the specified sound in the class it's called from, whatever sound it is that it is playing
    public void stop() {
        System.out.println("background audio stopped");
        //backgroundAudio.stop();
        
        //backgroundAudio.stop();
        
    }
}
