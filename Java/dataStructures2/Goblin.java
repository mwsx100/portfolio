package queensG;

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

class Goblin {
	private Scanner userIn;
	private String dictFileName;
	private int numLetters;
	private int numGuesses;
	private boolean playing = true;
	private boolean victory = false;
	private BetterArray<String> dictionary = new BetterArray();
	private BetterArray<Character> lettersGuessed = new BetterArray();
	private BetterArray<Character> currentWord = new BetterArray();
	
	// ADD MORE PRIVATE MEMBERS HERE IF NEEDED!
	
	private boolean checkRepeat(String word) {
		
		for(int i = 0; i< word.length(); i++) {
			char letter = word.charAt(i);
			for(int j = 0; j < word.length(); j++) {
				if(i != j) {
					if( letter == word.charAt(j)) {
						return true;
					}
				}
			}
		}
		
		return false;
	}
	
	/**
	Returns a _copy_ of the goblin's _current_ dictionary.
	@return dictionary clone
	*/		
	
	public BetterArray<String> getWords() {	
		return dictionary.clone();
	}
	
	/**
	Returns a _copy_ of the user's current guesses.
	@return letters guessed
	*/		
	public BetterArray<Character> getGuesses() {
		return lettersGuessed.clone();
	}
	
	/**
	Returns a _copy_ of the letters the user has locked in.
	@return letters locked in
	*/
	public BetterArray<Character> getCurrentWord() {
		return currentWord.clone();
	}
	
	/**
	Returns the number of wrong guesses the user has left.
	@return guesses remaining
	*/
	
	public int getGuessesRemaining() {
		return numGuesses;
	}
	
	/**
	 * initialization, returns true if conditions to start the game are met
	 * returns false if conditions are not met
	 * @return whether or not you can start the game
	 */
	
	public boolean init() {
		
		if (numLetters < 2) {
			System.out.println("Goblin can't find enough words! You win!");
			return false;
		}
			
		currentWord.ensureCapacity(numLetters);
		for (int i = 0; i < numLetters; i++) currentWord.add(i,null);
		
		File file = new File(dictFileName);
		
		try {
		Scanner dic = new Scanner(file);
		boolean isEmpty = true;
		while (dic.hasNextLine()==true) {
			String line = dic.nextLine();
			if (line.length() == numLetters) {
				if (this.checkRepeat(line) == false) {
					
					dictionary.append(line); isEmpty = false;
					continue;
				}
			}		
		}
		if (isEmpty == true) {
			System.out.println("Goblin can't find enough words! You win!");
			return false;
		}
		}		
		catch (FileNotFoundException e) {
			System.out.println("Goblin lost his dictionary! You win!");
			return false;
		}		
		/*
		Setup anything you need here. Check that the number of letters
		is valid (at least 2) and read in the dictionary. The dictionary
		contains main words which are not of the proper length, and manywith
		words with duplicate letters, don't add these to your goblin's list
		of options!
		
		If the dictionary can't be found, print the appropriate message
		and return false. If the dictionary contains no words of the given
		length, print the appropriate message and return false. If everything
		works, return true;
		
		Here are some quotes:
			"Goblin can't find enough words! You win!"
			"Goblin lost his dictionary! You win!"
		
		Can't remember how to use a Scanner?
		See: https://docs.oracle.com/javase/8/docs/api/java/util/Scanner.html
		You'll want to use the following:
			- Scanner(File) <- constructor
			- hasNextLine()
			- nextLine()
		*/		
		return true;
	}
	
	/**
	 * Divide (partition) the goblin's _current_ dictionary into X+1 lists where:
			X is the number of letter positions
			The +1 is for words where the letter doesn't occur
	 * @param guess the char that the user has input
	 * @return position of correct guess or -1 if incorrect
	 */
	public int bestPartition(char guess) {
		BetterArray<BetterArray> partition = new BetterArray(numLetters + 1);
		for (int i =0; i<= numLetters; i++) {
			BetterArray<String> position = new BetterArray();
			partition.add(i, position);
		}
		for(int i = 0; i< dictionary.size(); i++) {
			String word = dictionary.get(i); 
			for(int j=0; j< word.length(); j++) {
				if (word.charAt(j) == guess) {
					partition.get(j).append(word);
					break;
				}
				if (j == word.length()-1) {   //when char is not found in word
					partition.get(j+1).append(word);
				}
			}
		}
				
		int biggest = 0;
		int position = 0;
		BetterArray<String> newPartition = new BetterArray();
		for(int i=0; i< partition.size(); i++){
			if (partition.get(i).size() > biggest || 
					i == partition.size()-1 && partition.get(i).size() == biggest) {
				biggest =partition.get(i).size();
				newPartition = partition.get(i);
				position = i;
				if(i == partition.size()-1 && partition.get(i).size() == biggest) {
					dictionary = newPartition;
					return -1;
				}
			}
		}		
		dictionary = newPartition;	
		/*	
		Pick the biggest partition as the new dictionary and set that as the
		goblin's current dictionary. If the goblin picks a partition with the
		chosen letter (guess), return the position of that letter. If the goblin
		picks a partition without the chosen letter, return -1.
		
		If there are ties for partition size, choose the one with the earlier
		letter slot.
		*/		
		return position;
	}
	
	/**
	 * game loop prompting input from user
	 * @return true if game can continue and false if game is over
	 */
	
	public boolean step() {
		char guess;
		boolean guessedBefore = false;
		int correctGuesses = 0;
		
		while(playing == true) {
		System.out.println("Goblin says \"Guess a letter\": ");
		while(true) {
			guessedBefore = false;
			String input1 = userIn.nextLine();
			if (input1.length() ==1) {
			guess = input1.charAt(0);
			for (int i=0; i<lettersGuessed.size(); i++) {
				if (guess == lettersGuessed.get(i)) {
					System.out.println("Goblin says \"You already guessed: " + getGuesses().toString() + "\nGuess a new letter\": ");
					guessedBefore = true;
					break;
				}
			}
			if (guessedBefore == false) break;
			}
			else if (input1.length() > 1) 
				System.out.println("Goblin says \"One letter! Guess a single letter\": ");
			
		}
				
		lettersGuessed.append(guess);
		int pos = this.bestPartition(guess);
		
		if(pos == -1) {
			numGuesses--;
			System.out.println("Goblin says \"No dice! "+getGuessesRemaining()+" wrong guesses left...\"");
		}
		else {
			
			correctGuesses++;
			currentWord.replace(pos, guess);
			System.out.println("Goblin says \"Yeah, yeah, it's like this: "+ getCurrentWord().toString().replaceAll("null","-").replaceAll("[ ,]","")+"\"");	
			if (correctGuesses == numLetters) {
				victory = true;
				return false;
			}
		}
		/*
		Prompt and get a guess from the user.
		
		Check that the guess is valid (one letter which has not been
		guessed before). Give appropriate re-prompt message if not
		valid and get a new guess.
		
		Once you have a valid guess, partition the goblin's dictionary
		and choose a new set of words. The goblin should respond with
		the appropriate message and you need to update appropriate items
		you're tracking as well.
		
		Return true if the game should continue, or false if the game is over.
		
		Here are some quotes:
			"Goblin says \"Guess a letter\": "
			"Goblin says \"One letter! Guess a single letter\": "
			"Goblin says \"You already guessed: " + getGuesses().toString() + "\nGuess a new letter\": "
			"Goblin says \"No dice! "+getGuessesRemaining()+" wrong guesses left...\""
			"Goblin says \"Yeah, yeah, it's like this: "+ getCurrentWord().toString().replaceAll("null","-").replaceAll("[ ,]","")+"\"");
			
		Note: these two expressions:
			getGuesses().toString()
			getCurrentWord().toString().replaceAll("null","-").replaceAll("[ ,]","")
		are very inefficient. You can make them better if you want,
		but these should give you correct output.
		*/
		
		if (this.getGuessesRemaining() <1 ){
			playing = false;
			return false;
		}
		}
		return true;
	}
	
	/**
	 * Prints out appropriate win/lose message
	 */
	
	public void finish() {
		if (victory == true) 
		System.out.println("Goblin says \"You win... here's your stuff. But I'll get you next time!\"");		
		else {
		System.out.println("Goblin says \"I win! I was thinking of the word '"+getWords().get(0)+"'");
		System.out.println("Your stuff is all mine... I'll come back for more soon!\"");
		}
		/*
		This will be called after step() returns false. Print the appropriate
		win/lose message here.
		
		Here are some quotes:
			"Goblin says \"You win... here's your stuff. But I'll get you next time!\""
			"Goblin says \"I win! I was thinking of the word '"+getWords().get(0)+"'"
			"Your stuff is all mine... I'll come back for more soon!\""
		*/
	}
	
	// --------------------------------------------------------
	// DO NOT EDIT ANYTHING BELOW THIS LINE (except to add JavaDocs)
	// --------------------------------------------------------
	public Goblin(String dictFileName, int numLetters, int numGuesses) {
		this.userIn = new Scanner(System.in);
		this.dictFileName = dictFileName;
		this.numLetters = numLetters;
		this.numGuesses = numGuesses;
	}
	
	// --------------------------------------------------------
	// example testing code... edit this as much as you want!
	// --------------------------------------------------------
	public static void main(String[] args) throws FileNotFoundException {
		
	
		//if you don't have the mini dictionary one folder above your
		//user folder, this won't work!
		
		/*
		Sample successful run with output:
			> java Goblin
			Goblin can't find enough words! You win!
			Goblin lost his dictionary! You win!
			Yay 1
			Yay 2
			Yay 3
			Yay 4
			Yay 5
		*/
		Goblin g1 = new Goblin("../dictionary-mini.txt", 3, 10);
		Goblin g2 = new Goblin("../dictionary-mini.txt", 6, 6);
		Goblin g3 = new Goblin("../dictionary.txt", 1, 6);
		Goblin g4 = new Goblin("banana.txt", 3, 3);
		
		
		
		if(g1.init() && g2.init() && !g3.init() && !g4.init()) {
			System.out.println("Yay 1");
		}

		if(g1.getGuessesRemaining() == 10 && g1.getWords().size() == 1
			&& g2.getGuessesRemaining() == 6 && g2.getWords().size() == 5
			&& g1.getGuesses().size() == 0 && g2.getCurrentWord().size() == 6) {
			System.out.println("Yay 2");
		}
		
		BetterArray<Character> g1word = g1.getCurrentWord();
		if(g1word.get(0) == null  && g1word.get(1) == null && g1word.get(2) == null) {
			System.out.println("Yay 3");
		}
		
		//remember what == does on objects... not the same as .equals()
		if(g1.getWords() != g1.getWords() && g1.getGuesses() != g1.getGuesses()
			&& g1.getCurrentWord() != g1.getCurrentWord()) {
			System.out.println("Yay 4");
		}
		
		
		if(g2.bestPartition('l') == -1 && g2.getWords().size() == 3
			&& g2.bestPartition('p') == 0 && g2.getWords().size() == 2
			&& g2.bestPartition('n') == -1 && g2.getWords().size() == 1) {
			System.out.println("Yay 5");
		}
		
		//can't test step() or finish() this way... requires user input!
		//maybe you need to test another way...
	}
}