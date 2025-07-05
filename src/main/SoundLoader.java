package main;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class SoundLoader {
    Map<Sound, URL> soundUrlMap = new HashMap<>();

    public SoundLoader() {
        soundUrlMap.put(Sound.PICKUP, getClass().getResource("/sound/munch.wav"));
        soundUrlMap.put(Sound.WIN, getClass().getResource("/sound/win.wav"));
        soundUrlMap.put(Sound.THEME, getClass().getResource("/sound/melancholic-walk.wav"));
        soundUrlMap.put(Sound.THEME_FAST, getClass().getResource("/sound/trippy-trip-trop.wav"));
    }

    public URL get(Sound sound) {
        return soundUrlMap.get(sound);
    }
}
