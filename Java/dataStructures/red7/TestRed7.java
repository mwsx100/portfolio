package red7;

/**
Code to test several cases for each type of rule check
Only possibilities for ties (considered losing rule in game) is green and violet
Checking ties (fall through to highest in set), and invert. Also checking a case for clear winner

@author Cody Narber
*/
public class TestRed7{

	public static void main(String[] args){
		Red7CardSet r7h = new Red7CardSet();
		r7h.add(new Red7Card("Yellow",7));

		Red7CardSet r7h2 = new Red7CardSet();
		r7h2.add(new Red7Card("Orange",3));
		r7h2.add(new Red7Card("Violet",5));
       
       Red7CardSet r7h3 = new Red7CardSet();
       r7h3.add(new Red7Card("Indigo",6));
       r7h3.add(new Red7Card("Indigo",2));
       
       Red7CardSet r7h4 = new Red7CardSet();
       r7h4.add(new Red7Card("Yellow",6));
       r7h4.add(new Red7Card("Yellow",4));
       
       Red7CardSet r7h5 = new Red7CardSet();
       r7h5.add(new Red7Card("Green",6));
       r7h5.add(new Red7Card("Green",5));
     
       boolean rc1 = r7h.compareRule(r7h2,"red") > 0;
       boolean rc2 = r7h2.compareRule(r7h,"red") < 0;
		System.out.println("Red - Should be true: " + rc1);
       System.out.println("Red - Should be true: " + rc2);
      // System.out.println(r7h.toString());   System.out.println(r7h2.toString()); System.out.println(r7h3.toString());
       boolean oc1 = r7h.compareRule(r7h2,"orange") > 0;
       boolean oc2 = r7h2.compareRule(r7h,"orange") < 0;
       boolean oc3 = r7h2.compareRule(r7h3,"orange") < 0;
       System.out.println("Orange - Should be true: " + oc1);
       System.out.println("Orange - Should be true: " + oc2);
       System.out.println("Orange - Should be true: " + oc3);
      // System.out.println(r7h.toString());   System.out.println(r7h2.toString()); System.out.println(r7h3.toString());
       boolean yc1 = r7h.compareRule(r7h2,"yellow") > 0;
       boolean yc2 = r7h2.compareRule(r7h,"yellow") < 0;
       boolean yc3 = r7h2.compareRule(r7h3,"yellow") < 0;
       System.out.println("Yellow - Should be true: " + yc1); //before, lines 48 and 49
       System.out.println("Yellow - Should be true: " + yc2); //were identical to 47
       System.out.println("Yellow - Should be true: " + yc3);//i think you wanted it to be like this so i changed it
       
       boolean gc1 = r7h.compareRule(r7h3,"green") < 0;
       boolean gc2 = r7h3.compareRule(r7h,"green") > 0;
       boolean gc3 = r7h2.compareRule(r7h,"green") == 0;
       boolean gc4 = r7h4.compareRule(r7h3,"green") > 0;
       System.out.println("Green - Should be true: " + gc1);
       System.out.println("Green - Should be true: " + gc2);
       System.out.println("Green - Should be true: " + gc3);
       System.out.println("Green - Should be true: " + gc4);
       
       boolean bc1 = r7h.compareRule(r7h3,"blue") > 0;
       boolean bc2 = r7h3.compareRule(r7h,"blue") < 0;
       boolean bc3 = r7h2.compareRule(r7h,"blue") > 0;
       System.out.println("Blue - Should be true: " + bc1);
       System.out.println("Blue - Should be true: " + bc2);
       System.out.println("Blue - Should be true: " + bc3);
       
       boolean ic1 = r7h.compareRule(r7h2,"indigo") > 0;
       boolean ic2 = r7h2.compareRule(r7h,"indigo") < 0;
       boolean ic3 = r7h5.compareRule(r7h,"indigo") > 0;
       System.out.println("Indigo - Should be true: " + ic1);
       System.out.println("Indigo - Should be true: " + ic2);
       System.out.println("Indigo - Should be true: " + ic3);
       
       boolean vc1 = r7h.compareRule(r7h4,"violet") == 0;
       boolean vc2 = r7h2.compareRule(r7h3,"violet") > 0;
       boolean vc3 = r7h3.compareRule(r7h2,"violet") < 0;
       boolean vc4 = r7h.compareRule(r7h2,"violet") < 0;
       System.out.println("Violet - Should be true: " + vc1);
       System.out.println("Violet - Should be true: " + vc2);
       System.out.println("Violet - Should be true: " + vc3);
       System.out.println("Violet - Should be true: " + vc4);
       
       
       Red7CardSet r7h6 = new Red7CardSet();
       r7h6.add(new Red7Card("Orange",3));
       r7h6.add(new Red7Card("Violet",5));
       r7h6.add(new Red7Card("Indigo",6));
       r7h6.add(new Red7Card("Indigo",2));
       r7h6.add(new Red7Card("Yellow",6));
       r7h6.add(new Red7Card("Yellow",4));
       
       System.out.println("\nVerifying toString for the GUI compatibility...");
       System.out.println("\nShould be in form \"#Color,#Color,#Color\", Example: \"3Orange,5Violet\"");
       System.out.println(r7h6);
       String[] parts = r7h6.toString().split(",");
       if(parts.length<6) System.out.println("ERROR: Not enough Commas to separate cards in set");
       if(parts.length>6) System.out.println("ERROR: Too many Commas to separate cards");
       for(int i=0; i<parts.length; i++){
           if(parts[i].isEmpty()) System.out.println("ERROR: ("+i+") A card token contains no text");
           else {
               char cnum = parts[i].charAt(0);
               if(!Character.isDigit(cnum)) System.out.println("ERROR: ("+i+") The first character of a word token is not a number");
               String s = parts[i].substring(1).trim().toUpperCase();
               if(s.isEmpty()) System.out.println("ERROR: ("+i+") The color part in the word token does not exist");
               else {
                   if(!"ROYGBIV".contains(s.charAt(0)+"")) System.out.println("ERROR: ("+i+") Color token's first letter is not of any of the 7 colors: ROYGBIV");
               }
           }
       }
       System.out.println("...\nif no error messages displayed, your code should work in the GUI");
       
       
	}
}
