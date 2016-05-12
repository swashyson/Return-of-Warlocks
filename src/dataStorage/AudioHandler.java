/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataStorage;

import java.io.File;
import java.nio.file.Paths;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 *
 * @author Mohini
 */
public class AudioHandler {
    
    private MediaPlayer backgroundAudio;
    private Media backgroundAudioPath;// = new Media(getClass().getResource("audioResources/preliminary_background_music_return_of_warlocks.wav").toString());
    
    public void playBackgroundAudio() {
        try {
            System.out.println("finding media file");
            backgroundAudioPath = new Media(new File("preliminary_background_music_return_of_warlocks.mp3").toURI().toString());
            System.out.println("media file has been found");
            backgroundAudio = new MediaPlayer(backgroundAudioPath);
            backgroundAudio.setCycleCount(MediaPlayer.INDEFINITE);
            backgroundAudio.setVolume(0.5);
//            backgroundSound.setAutoPlay(true);
            backgroundAudio.setAutoPlay(true);
            System.out.println("media is playing");
        } catch (Exception e) {
            System.out.println("media file has not been found");
            e.printStackTrace();
        }
    }

    //This method turns off the specified sound in the class it's called from, whatever sound it is that it is playing
    public void stop() {
        System.out.println("background audio stopped");
        backgroundAudio.stop();
    }
}
