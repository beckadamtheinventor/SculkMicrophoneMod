package me.towo.sculkmic.client.sound;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Line;
import javax.sound.sampled.Mixer;
import java.util.ArrayList;
import java.util.List;

public interface AudioManager {
    class Input {
        private static Mixer defaultDevice = null;
        public static Mixer[] getAllDevices() {
            ArrayList<Mixer> result = new ArrayList<>();
            Mixer.Info[] infos = AudioSystem.getMixerInfo();
            for (Mixer.Info info : infos) {
                Mixer mixer = AudioSystem.getMixer(info);
                Line.Info [] targetLineInfos = mixer.getTargetLineInfo();
                for (Line.Info targetLineInfo : targetLineInfos)
                    if(targetLineInfo instanceof DataLine.Info)
                        result.add(mixer);
            }
            if (defaultDevice == null && !result.isEmpty()) {
                defaultDevice = result.get(0);
            }
            return result.toArray(new Mixer[0]);
        }

        public static List<String> getAllDeviceNames() {
            List<String> result = new ArrayList<>();
            for (Mixer mixer : getAllDevices()) {
                result.add(mixer.getMixerInfo().getName());
            }
            return result;
        }

        public static Mixer get(String deviceName) {
            Mixer[] mixers = getAllDevices();
            for (Mixer mixer : mixers) {
                if (mixer.getMixerInfo().getName().equals(deviceName))
                    return mixer;
            }
            return defaultDevice;
        }

        public static boolean exists(String deviceName) {
            Mixer[] mixers = getAllDevices();
            for (Mixer mixer : mixers) {
                if (mixer.getMixerInfo().getName().equals(deviceName))
                    return true;
            }
            return false;
        }

        public static String defaultDeviceName() {
            if (defaultDevice != null) {
                return defaultDevice.getMixerInfo().getName();
            }
            return "?";
        }
    }
}
