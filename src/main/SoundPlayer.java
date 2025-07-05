package main;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class SoundPlayer {
    Clip clip;
    SoundLoader soundLoader;

    // if I end up needing two instances of sound player, it would be good to abstract out the loading of the sound clips
    public SoundPlayer(SoundLoader soundLoader) {
        this.soundLoader = soundLoader;
    }

    public void setFile(Sound sound) {
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundLoader.get(sound));
            clip = AudioSystem.getClip();
            clip.open(ais);
        } catch (Exception e) {
            System.out.println("Error setting audio file");
        }
    }

    public void play() {
        clip.start();
    }

    public void loop() {
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void stop() {
        clip.stop();
    }
}
