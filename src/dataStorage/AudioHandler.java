/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataStorage;

import java.io.File;
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
            System.out.println("background audio playing");
            backgroundAudioPath = new Media(getClass().getResource("audioResources/preliminary_background_music_return_of_warcraft.wav").toString());
            backgroundAudio = new MediaPlayer(backgroundAudioPath);
            backgroundAudio.setCycleCount(MediaPlayer.INDEFINITE);
            backgroundAudio.setVolume(0.5);
//            backgroundSound.setAutoPlay(true);
            backgroundAudio.setAutoPlay(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //This method turns off the specified sound in the class it's called from, whatever sound it is that it is playing
    public void stop() {
        System.out.println("background audio stopped");
        backgroundAudio.stop();
    }
}
