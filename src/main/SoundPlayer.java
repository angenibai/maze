package main;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class SoundPlayer {
    Clip clip;
    Map<Sound, URL> soundUrlMap = new HashMap<>();

    // if I end up needing two instances of sound player, it would be good to abstract out the loading of the sound clips
    public SoundPlayer() {
        soundUrlMap.put(Sound.PICKUP, getClass().getResource("/sound/pickup-2.wav"));
        soundUrlMap.put(Sound.WIN, getClass().getResource("/sound/win.wav"));
    }

    public void setFile(Sound sound) {
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundUrlMap.get(sound));
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
