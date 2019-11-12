package main;

import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JOptionPane;

public class SoundEffect {

	Clip clip;
	
	public SoundEffect() {
		
	
		
		
	}
	

	public void setURL(URL musicURL) {
		
		try {
			AudioInputStream audio = AudioSystem.getAudioInputStream(musicURL);
			clip = AudioSystem.getClip();
			clip.open(audio);
		} catch (UnsupportedAudioFileException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		} catch (LineUnavailableException e) {

			e.printStackTrace();
		}

	}
	
	public void play() {
		clip.loop(4);
	}
	
	public void close() {
		clip.close();
	}

}
