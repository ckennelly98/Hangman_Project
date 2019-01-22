# Hangman_Project

The Hangman.java was the base code given to me by my professor. This code plays the normal way hangman is played when HangmanGUI is run.

The GoodHangman.java file is the altered code where I was given a list of methods that must be implemented into the code in order
to make a better version of hangman. When GoodHangman.java is put into HangmanGUI.java and run, every time the player inputs a letter, 
then the code filters the list of words in the dictionary.txt by that specific letter and categorizes them by where the letter is
located in the word. After adding more and more letters, the list of possible words gets smaller and smaller until there is only one
word in the list left which will end the game with a victory screen.

The HangmanGUI.java file was written by my professor and is used to run the window for the Hangman game.
In order to switch between Hangman.java and GoodHangman.java, you must comment out the line of code of either 
"Hangman game = new Hangman();" or "GoodHangman game = new GoodHangman();" depending on which file you want to use.

