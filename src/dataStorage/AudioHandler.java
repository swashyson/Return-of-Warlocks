/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataStorage;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 *
 * @author Mohini
 */
public class AudioHandler {
    
    MediaPlayer mediaPlayer;
    Media mediaPath = new Media(null);
    
    private void play() {
        try {
            System.out.println("sound playing");
            mediaPlayer = new MediaPlayer(null);
            //mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            mediaPlayer.setVolume(0.5);
//            backgroundSound.setAutoPlay(true);
            mediaPlayer.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //This method turns off the specified sound in the class it's called from, whatever sound it is that it is playing
    public void stop() {
        System.out.println("sound stopped");
        mediaPlayer.stop();
    }
}
