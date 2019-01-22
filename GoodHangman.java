import java.util.HashSet;
import java.util.Set;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.LinkedList;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;

public class GoodHangman {
	private static final int MAX_INCORRECT_GUESSES = 6;

	// Group words by length (key is length, value is word list)
	private Map<Integer, LinkedList<String>> dictionary = new HashMap<Integer, LinkedList<String>>();
	
	// Initially, the list of words of a given length
	// Subsequently, the words remaining based on the filter
	private LinkedList<String> candidates = new LinkedList<String>();

	// The key is the pattern (e.g., -ee), the value is the list of matching words (e.g., see, bee)
	private Map<String, LinkedList<String>> filter = new HashMap<String, LinkedList<String>>();

	// The pattern
	private String word;
	// What the user typed
	private Set<Character> guesses = new HashSet<Character>();
	// What the user typed that was in the word
	private Set<Character> correct = new HashSet<Character>();
	
	GoodHangman() {
		// Note that dictionary file has a lot of words, which makes it hard to win the game
		// Warning: I got this file from Internet, so it contains inappropriate words. 
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
    // Unlike Hangman, we will not select correct answer since  we will chose pattern later.
    // So initially correct answered word should be "-----" (depends on the length)
    // And will decide candidates here
	public void setLength(int length) {
        // Initially, the list of words of a given length
		candidates = dictionary.get(length);
        // Initially word will be all "-"
		word = candidates.get(new Random().nextInt(candidates.size()));
        
	}
	// Return the answer (we may have more than one)
    // Initially, we will need to print the "list of words" (candidates) of a given length
    // Subsequently, the words remaining based on the filter
	public String toString() {
		return word;
	}
	// The set of characters the user typed so far
	public Set<Character> getGuesses() {
		return guesses;
	}
	// Check if user typed letter is in word
    // read character by character from word and compare with typed letter
    // I used this method from pattern, but you may include this part inside pattern
	public boolean hasLetter(String word, char typed) {
		return word.contains("" + typed);
	}

	// Given a word, construct a pattern based on what the user typed
	// If the typed character isn't in the word, return null (we don't need to create pattern for this word)
    // I called this from createFilter. Of course, you may include this part inside createFilter
    // refer to the method visible(). It is similar
    // guesses.contains(letter) || letter == typed ? letter : '-'
	public String pattern(String word, char typed) {
		StringBuilder built = new StringBuilder();
		for (char letter : word.toCharArray()) {
			built.append(correct.contains(letter) || letter == typed ? letter : '-');
		}
		return (hasLetter(word, typed) ? built.toString() : null);
	}

	// Group words by common pattern
	// For example:
	// -ee => fee, see, bee, ...
	// -e- => bed, beg, bet, few, hex, ...
	public void createFilter(char typed) {
		filter.clear();
		for(String n : candidates){
			if(!filter.containsKey(pattern(n, typed))){
				filter.put(pattern( n, typed), new LinkedList<String>());
			}
			filter.get(pattern(n, typed)).add(n);
		}
		filter.remove(null);
	}
	// Select the pattern with the most words in it (largest linkedList size)
    // Print out filter and candidates here
	public void choosePattern() {
		String bigWord = filter.keySet().iterator().next();
		for(String nextWord : filter.keySet()){
			if(filter.get(nextWord).size() > filter.get(bigWord).size()){
				bigWord = nextWord;
			}
		}
		candidates = filter.get(bigWord);
		word = candidates.get(new Random().nextInt(candidates.size()));
	}

	// User makes a guess. If the character is new, add it to the set.
	// Otherwise return false so the user may guess again
	public boolean makeGuess(char letter) {
		// Only allow lower-case a-z
		if (letter < 'a' || letter > 'z') {
			return false;
		}
		// If we already guessed, don't bother
		if (guesses.contains(letter)) {
			return false;
		}
		createFilter(letter);
		if(!filter.isEmpty()) {
			choosePattern();
		}
		Possibilities();
		guesses.add(letter);
		
		if (word.contains("" + letter)) {
			correct.add(letter);
		}
		return true;
	}

	public void Possibilities(){
		for(String c : filter.keySet()) {
			System.out.println(c + " " + filter.get(c).size());
		}
		System.out.println(candidates + "\n");
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
