import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineEvent.Type;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;

/**
 * This class allows to play a *.wav sound in a thread. You just have to give it
 * an {@link AudioInputStream} and then start the thread. It will play the
 * sound, and the thread will be stopped at the end of the sound.
 * 
 * Using a particular thread allows to do some other job while playing the
 * sound.
 * 
 * @author Antoine Neveux
 * 
 */
public class SoundPlayer extends Thread {

	/**
	 * The {@link AudioInputStream} linked to the sound to play
	 */
	private AudioInputStream audioInputStream;

	/**
	 * A custom listener in order to be notified at the end of the audio file
	 * playing
	 */
	private AudioListener listener = new AudioListener();

        /**
	 * Constructor. You can create an {@link AudioInputStream} with: {@link
	 * 
	 * @link AudioSystem#getAudioInputStream(java.io.File)} ,
	 *       {@link AudioSystem#getAudioInputStream(java.io.InputStream)},
	 *       {@link AudioSystem#getAudioInputStream(java.net.URL)}
	 * 
	 * @param audioInputStream
	 *            The {@link AudioInputStream} linked to the sound to play. You
	 *            can create an {@link AudioInputStream} using the
	 *            {@link AudioSystem} object.
	 * 
	 */
	public SoundPlayer(AudioInputStream audioInputStream) {
		super();
		this.audioInputStream = audioInputStream;
	}

	/**
	 * This method allows to actually play the sound provided from the
	 * {@link #audioInputStream}
	 * 
	 * @throws LineUnavailableException
	 *             if the {@link Clip} object can't be created
	 * @throws IOException
	 *             if the audio file can't be find
	 */
	protected void play() throws LineUnavailableException, IOException {
		Clip clip = AudioSystem.getClip();
		clip.addLineListener(listener);
		clip.open(audioInputStream);
		try {
			clip.start();
			listener.waitUntilDone();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			clip.close();
		}
		audioInputStream.close();
	}

	/**
	 * This method allows to play the sound while running the Thread
	 */
	@Override
	public void run() {
		try {
			this.play();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * This Listener allows to notify when the play of the sound is ended
	 */
	class AudioListener implements LineListener {
		private boolean done = false;

		/**
		 * This method allows to be notified for each event while playing a
		 * sound
		 */
		@Override
		public synchronized void update(LineEvent event) {
			Type eventType = event.getType();
			if (eventType == Type.STOP || eventType == Type.CLOSE) {
				done = true;
				notifyAll();
			}
		}

		/**
		 * This method allows to wait until a sound is completly played
		 * 
		 * @throws InterruptedException
		 *             as we work with thread, this exception can occur
		 */
		public synchronized void waitUntilDone() throws InterruptedException {
			while (!done) {
				wait();
			}
		}
	}

}