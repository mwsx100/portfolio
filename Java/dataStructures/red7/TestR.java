package red7;

public class TestR {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Red7CardSet bob = new Red7CardSet();
		Red7CardSet bob2 = new Red7CardSet();
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
     
		
		bob.add(new Red7Card("Indigo",1));
        bob.add(new Red7Card("Indigo",2));
        bob.add(new Red7Card("Yellow",3));
        bob.add(new Red7Card("Red",7));
        

		bob2.add(new Red7Card("Indigo",6));
        bob2.add(new Red7Card("Indigo",2));
        bob2.add(new Red7Card("Red",5));
        bob2.add(new Red7Card("Yellow",3));
        System.out.println(r7h.toString());   System.out.println(r7h2.toString()); System.out.println(r7h3.toString());
        System.out.println(r7h.compareRule(r7h4,"violet"));
        System.out.println(r7h2.compareRule(r7h3,"violet"));
        System.out.println( r7h3.compareRule(r7h2,"violet"));
        
       // bob.remove(bob.get(1));
       // System.out.println(bob);
        //&& amountOfEach[i].get(amountOfEach[i].size-1).compareTo(amountOfEach[highInd2].get(amountOfEach[highInd2].size-1)) > 0)
       //System.out.println(bob2.compareRule(bob, "Indigo"));

	}

}
