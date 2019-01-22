import java.awt.Font;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;

public class HangmanGUI extends JFrame {
	
	//Hangman game = new Hangman();
	GoodHangman game = new GoodHangman();
	Font textFont, smileyFont;
	HangmanGUI() {
		game.setLength(5);
		setTitle("Hangman");
		setSize(640, 480);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		addKeyListener(new KeyControl());
		
		// Setup font with Emoji support
		// Free emoji font courtesy of: https://github.com/MorbZ/OpenSansEmoji
		try {
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			Font font = Font.createFont(Font.TRUETYPE_FONT, new File("OpenSansEmoji.ttf"));
			// 20 point font for text
			textFont = font.deriveFont(Font.PLAIN,20f);
			// 80 point font for emoji (and secret word)
			smileyFont = font.deriveFont(80f);
			ge.registerFont(font);
		} catch (Exception e) {
			e.printStackTrace();
		}
		setVisible(true);
	}
	// Handle keyboard input
	// When we type a key, make a guess and repaint
	// It only works for lower-case a-z for now
	private class KeyControl implements KeyListener {
		@Override
		public void keyTyped(KeyEvent e) {
			repaint();
		}

		@Override
		public void keyPressed(KeyEvent e) {
			game.makeGuess(e.getKeyChar());
			repaint();
		}

		@Override
		public void keyReleased(KeyEvent e) {
			repaint();
		}
	}

	@Override
	public void paint(Graphics g) {
		g.clearRect(0, 0, 640, 480);
		g.setFont(smileyFont);
		if (game.won()) {
			g.drawString("😍", 100, 100);
		} else {
			switch (game.guessesRemaining()) {
			case 6:
				g.drawString("😎", 100, 100);
				break;
			case 5:
				g.drawString("☺️", 100, 100);
				break;
			case 4:
				g.drawString("😐", 100, 100);
				break;
			case 3:
				g.drawString("😕", 100, 100);
				break;
			case 2:
				g.drawString("😮", 100, 100);
				break;
			case 1:
				g.drawString("😓", 100, 100);
				break;
			default:
				g.drawString("😠", 100, 100);
				break;
			}
		}
		g.setFont(textFont);
		g.drawString("Guesses remaining: " + game.guessesRemaining(), 100, 150);
		g.drawString(game.getGuesses().toString(), 100, 170);
		g.setFont(smileyFont);
		g.drawString(game.visible(), 100, 290);
		g.setFont(textFont);
		if (game.isOver()) {
			String result;
			if (game.won()) {
				result = "Yay, you won! It was: " + game.toString();
			} else {
				result = "You lost! It was: " + game.toString();
			}
			playAgain(result);
		}
	}
	// Ask the player what to do when the game is over
	private void playAgain(String message) {
		String[] options = new String[] {"Play again","Quit"};
		int choice = JOptionPane.showOptionDialog(null, message, "Game over", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,null,options,options[0]);

		if (choice == 0) {
			game.reset();
			repaint();
		} else {
			System.exit(0);
		}
	}

	public static void main(String[] args) {
		HangmanGUI gui = new HangmanGUI();
	}
}
