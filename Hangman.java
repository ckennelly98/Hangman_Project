import java.util.HashSet;
import java.util.Set;
import java.util.Map;
import java.util.LinkedList;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

public class Hangman {
	private static final int MAX_INCORRECT_GUESSES = 6;
	private Map<Integer, LinkedList<String>> dictionary = new HashMap<Integer, LinkedList<String>>();

	private String word;

	/*
	 All three List, Set, and Map are interfaces in Java
	 The Set interface provides a collection of unique objects, 
	 i.e. Set doesn't allow duplicates, while Map holds two objects per Entry (key-value pair) 
	 and it may contain duplicate values but keys are always unique.
	 The main difference between List and Set interface in Java is that 
	 List allows duplicates while Set doesn't allow duplicates.
	 */

	// What the user typed (should be unique, so use Set interface)
	private Set<Character> guesses = new HashSet<Character>();
	// What the user typed that was in the word
	private Set<Character> correct = new HashSet<Character>();

	Hangman() {
		// Note that dictionary file has a lot of words, which makes it hard to win the game
		// Warning: I got this file from Internet, so it may contain inappropriate words. 
		File file = new File("dictionary.txt");
		FileReader reader;
		try {
			reader = new FileReader(file);
			Scanner scanner = new Scanner(reader);
			// adding words to HashMap. (key: word length, values: words list)
			while (scanner.hasNextLine()) {
				addWord(scanner.nextLine());
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	// Construct the dictionary
	public void addWord(String word) {
		if (!dictionary.containsKey(word.length())) {
			dictionary.put(word.length(), new LinkedList<String>());
		}
		dictionary.get(word.length()).add(word);
	}

	// Reset the game
	public void reset() {
		guesses.clear();
		correct.clear();
		// Set a random word length (between 2 and 8)
		setLength(new Random().nextInt(6)+2);
	}
	// Begin the game by setting the word length
	public void setLength(int length) {
		// This will return linked list size
		int size = dictionary.get(length).size();
		// This will select random word from the linked list and will be used as an "answer"
		word = dictionary.get(length).get(new Random().nextInt(size));
	}
	// Return an answer
	public String toString() {
		return word;
	}
	// Return set of characters of guesses 
	public Set<Character> getGuesses() {
		return guesses;
	}
	// User makes a guess. If the character is new, add it to the set.
	// Otherwise return false so the user may guess again
	public boolean makeGuess(char letter) {
		// Only allow lower-case a-z
		if (letter < 'a' || letter > 'z') {
			return false;
		}
		if (guesses.contains(letter)) {
			return false;
		}
		guesses.add(letter);
		if (word.contains("" + letter)) {
			correct.add(letter);
		}
		return true;
	}
	// What can the player see?
	public String visible() {
		// StringBuilder concatenates all guessed letter and "-"
		StringBuilder b = new StringBuilder();
		// Compare each character from the answer with guessed letter
		for (char letter : word.toCharArray()) {
			b.append(guesses.contains(letter) ? letter : '-');
		}
		return b.toString();
	}
	// Did the player win?
	public boolean won() {
		return word.equals(visible());
	}
	// How many guesses remain?
	public int guessesRemaining() {
		return MAX_INCORRECT_GUESSES - (guesses.size() - correct.size());
	}
	// Is the game over?
	public boolean isOver() {
		return (guessesRemaining() <= 0) || won();
	}
}