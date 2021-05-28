package project3;
import java.util.Scanner;
import java.util.InputMismatchException;

public class StrieDemo {
	public static void main(String[] args) {

		Strie myStrie = new Strie();
		Scanner in = new Scanner(System.in);

		while(true) {
			int choice = doMenu(in);
			try {
				switch(choice) {
					case 1:
						System.out.print("Enter a word: ");
						String word = in.nextLine().trim();
						myStrie.insert(word);
						break;
					case 2:
						System.out.print("Enter a word: ");
						word = in.nextLine().trim();

					 	boolean isFound = myStrie.contains(word);
						if(isFound == true) System.out.println("Your Strie contains this word!");
						else System.out.println("Your strie doesn't contain this word!");
						break;
					case 3:
						System.out.print("Enter a word: ");
						word = in.nextLine().trim();
					
						boolean removed = myStrie.remove(word);
						if(removed  == true) System.out.println("Word removed from Strie");
						else if(removed == false) System.out.println("Invalid character entered. Characters must be between 'a' and 'z' ");
						
						break;
					case 4:
						String ret = Strie.levelOrderTraversal(myStrie).trim();
						System.out.println(ret);
						break;
					case 5:
						try{
							String[] allStrings = Strie.getStrieWords(myStrie);
							for(String str : allStrings)
							System.out.println(str);
						}catch(RuntimeException e){
							System.out.println(e.getMessage());
						}
						break;
					case 6:
						System.out.print("Enter a word: ");
						word = in.nextLine().trim();
						try{
							System.out.println(Strie.getLongestPrefix(myStrie, word));
						}catch(RuntimeException e){
							System.out.println(e.getMessage());
						}
						break;
					case 7:
						System.out.print("Enter a word: ");
						word = in.nextLine().trim();
						try{
							String[] allStrings = Strie.getAllSuffixes(myStrie, word);
							for(String str : allStrings)
							System.out.println(str);
						}catch(RuntimeException e){
							System.out.println(e.getMessage());
						}
						break;
					case 8:
						System.out.print("Enter a word: ");
						word = in.nextLine().trim();
						try{
							String[] allStrings = Strie.getClosestMatch(myStrie, word);
							for(String str : allStrings)
							System.out.println(str);
						}catch(RuntimeException e){
							System.out.println(e.getMessage());
						}
						break;
					case 9: in.close(); System.exit(0);
				}
			}
			catch(RuntimeException e) {
				System.out.println("\n"+e.getMessage());
			}
		}
	}

	public static int doMenu(Scanner in) {
		while(true) {
			try {
				System.out.println("\nYou can:");
				System.out.println(" 1) insert a word");
				System.out.println(" 2) Look up a word");
				System.out.println(" 3) remove a word");
				System.out.println(" 4) Do a level order traversal of your Strie");
				System.out.println(" 5) Display all existing words from your Strie");
				System.out.println(" 6) Get the longest prefix of a word");
				System.out.println(" 7) Suggest all words from your Strie for a given prefix");
				System.out.println(" 8) Get the closest match of your word from Strie");
				System.out.println(" 9) Quit");
				System.out.print("\nWhat's your choice? ");
				int choice = in.nextInt();
				in.nextLine();

				if(choice < 1 || choice > 9) {
					System.out.println("Invalid selection");
					continue;
				}

				return choice;
			}
			catch(InputMismatchException e) {
				System.out.println("Invalid selection");
				in.nextLine();
			}
		}
	}
}
