package game;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.UnsupportedAudioFileException;

public class MyAudioPlayer {
	Map<String, AudioInputStream> streamMap = new HashMap<String, AudioInputStream>();
	Clip audioClip;

	public MyAudioPlayer(){
			
			try {
				streamMap.put("background",AudioSystem.getAudioInputStream(getClass().getResource("/res/music/bg.wav")));
			} catch (UnsupportedAudioFileException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	
	public void SetFile(String fileName) {
		try {
			
			AudioInputStream s = streamMap.get(fileName);
			audioClip = AudioSystem.getClip();
			audioClip.open(s);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void playMusic() {
		audioClip.start();
		audioClip.setFramePosition(0);
		
	}
	public void loopMuisc() {
		audioClip.loop(Clip.LOOP_CONTINUOUSLY);
	}
	public void stopMusic() {
		audioClip.stop();
	}
}
