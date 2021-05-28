package red7; 

public class Red7CardSet {
	
	private Red7Card[] set;
	private int size;
	
	public Red7CardSet(){ 
		size = 0;
	}
	
	
	public void add(Red7Card rc) {
		
		Red7Card newArray[] = new Red7Card[this.getSize() +1]; 
		for (int i = 0; i < this.getSize(); i++) {
			newArray[i] = this.set[i];
		}
		for (int i = 0; i < size; i++) {
			if (newArray[i].compareTo(rc) >0) {
				Red7Card temp = newArray[i];
				newArray[i] = rc;
				rc = temp;
			}
		}
		newArray[this.getSize()] = rc;
		this.set = newArray;  size++;
		
	}
	
	public void remove(Red7Card rc) {
		for (int i = 0; i < size; i++) {
			if (set[i] == rc) {
				//System.out.println("found it");
				for (int j = i; j < size-1; j++) {
					set[j] = set[j+1];
				}
			}
		} 
		Red7Card newArray[] = new Red7Card[this.getSize() -1]; 
		for (int i = 0; i < this.getSize()-1; i++) {
			newArray[i] = this.set[i];
		}
		
		set = newArray;
		
		size--;
	}
	
	public Red7Card get(int index) {
		return set[index];
	}
	
	public int getSize() {
		return size ;	
	}
	public int compareRule(Red7CardSet rs, String color) {
		int value = -9;
		switch(color.toUpperCase()) {
		case "VIOLET": value=this.compareViolet(rs); break;
		case "INDIGO": value=this.compareIndigo(rs);break;
		case "BLUE": value=this.compareBlue(rs);break;
		case "GREEN": value=this.compareGreen(rs);break;
		case "YELLOW": value=this.compareYellow(rs);break;
		case "ORANGE": value=this.compareOrange(rs);break;
		case "RED": value=this.compareRed(rs); break;
		//default: System.out.println("not a color"); return -9;
		}
		return value;
	}
	
	public int compareRed(Red7CardSet rs) {
		int high = 0; Red7Card hc = new Red7Card("yellow",1);
		Red7Card hc2 = new Red7Card("yellow",2);
		for (int i = 0; i < this.getSize(); i++) {
			if (this.set[i].getOverallVal() > high) {
				high = this.set[i].getOverallVal(); hc = this.set[i];
			}
		}
		high = 0;
		for (int i = 0; i < rs.getSize(); i++) {
			if (rs.set[i].getOverallVal() > high) {
				high = rs.set[i].getOverallVal(); hc2 = rs.set[i];
			}
		}
		return hc.compareTo(hc2);
	}
	
	public int compareOrange(Red7CardSet rs) {
		int highInd = 0, highInd2 = 0;
		Red7CardSet one = new Red7CardSet();Red7CardSet two = new Red7CardSet();
		Red7CardSet three = new Red7CardSet();Red7CardSet four = new Red7CardSet();
		Red7CardSet five = new Red7CardSet();Red7CardSet six = new Red7CardSet();
		Red7CardSet seven = new Red7CardSet();
		Red7CardSet one2 = new Red7CardSet();Red7CardSet two2 = new Red7CardSet();
		Red7CardSet three2 = new Red7CardSet();Red7CardSet four2 = new Red7CardSet();
		Red7CardSet five2 = new Red7CardSet();Red7CardSet six2 = new Red7CardSet();
		Red7CardSet seven2 = new Red7CardSet();
		for (int i = 0; i < this.getSize(); i++) {
			//System.out.println(this.set[i].getNumber());
			switch(this.set[i].getNumber()) {
			case 1: one.add(this.set[i]); break;
			case 2: two.add(this.set[i]); break;
			case 3: three.add(this.set[i]); break;
			case 4: four.add(this.set[i]); break;
			case 5: five.add(this.set[i]); break;
			case 6: six.add(this.set[i]);  break;
			case 7: seven.add(this.set[i]); break;
			}	
		}
		Red7CardSet[] amountOfEach = {one, two, three, four, five, six, seven};
		int high = 0;
		for (int i = 0; i < amountOfEach.length; i++) {
			if (amountOfEach[i].size> high || high ==1 ) {high = amountOfEach[i].size; highInd = i;}
		}
		int high2 = 0;
		for (int i = 0; i < rs.getSize(); i++) {
			switch(rs.set[i].getNumber()) {
			case 1: one2.add(rs.set[i]); break;
			case 2: two2.add(rs.set[i]); break;
			case 3: three2.add(rs.set[i]); break;
			case 4: four2.add(rs.set[i]); break;
			case 5: five2.add(rs.set[i]); break;
			case 6: six2.add(rs.set[i]); break;
			case 7: seven2.add(rs.set[i]); break;
			}	
		}
		Red7CardSet[] amountOfEach2 = {one2, two2, three2, four2, five2, six2, seven2};
		for (int i = 0; i < amountOfEach2.length; i++) {
			if (amountOfEach2[i].size > high2){ high2 = amountOfEach2[i].size;highInd2=i; }
		}
		Red7CardSet hiSet = amountOfEach[highInd]; 
		Red7CardSet hiSet2 = amountOfEach2[highInd2]; 
	//	System.out.println( hiSet.get(hiSet.size-1).toString());
		if (high == high2) {
			//System.out.println(hiSet.get(hiSet.size-1));
			return hiSet.get(hiSet.size-1).compareTo(hiSet2.get(hiSet2.size-1));
			}
		if (high > high2) return 1;
		else return -1;
	}
	
	public int compareYellow(Red7CardSet rs) {
		Red7CardSet violet = new Red7CardSet();Red7CardSet indigo = new Red7CardSet();
		Red7CardSet blue = new Red7CardSet();Red7CardSet green = new Red7CardSet();
		Red7CardSet yellow = new Red7CardSet();Red7CardSet orange = new Red7CardSet();
		Red7CardSet red = new Red7CardSet();
		Red7CardSet violet2 = new Red7CardSet();Red7CardSet indigo2 = new Red7CardSet();
		Red7CardSet blue2 = new Red7CardSet();Red7CardSet green2 = new Red7CardSet();
		Red7CardSet yellow2 = new Red7CardSet();Red7CardSet orange2 = new Red7CardSet();
		Red7CardSet red2 = new Red7CardSet();

		for (int i = 0; i < this.getSize(); i++) {
			//System.out.println(this.set[i].getNumber());
			switch(this.set[i].getColor().toUpperCase()) {
			case "VIOLET": violet.add(this.set[i]);  break;
			case "INDIGO": indigo.add(this.set[i]); break;
			case "BLUE": blue.add(this.set[i]);break;
			case "GREEN":green.add(this.set[i]); break;
			case "YELLOW": yellow.add(this.set[i]); break;
			case "ORANGE": orange.add(this.set[i]);  break;
			case "RED": red.add(this.set[i]); break;
			}	
		}
		Red7CardSet[] amountOfEach = {violet, indigo, blue, green, yellow, orange, red};
	//	System.out.println(sxct);
		int high = 0; int highInd = 0;
		for (int i = 0; i < amountOfEach.length; i++) {
			if (amountOfEach[i].size > high) {high = amountOfEach[i].size; highInd = i;}
		}
		int high2 = 0;
		for (int i = 0; i < rs.getSize(); i++) {
			switch(rs.set[i].getColor().toUpperCase()) {
			case "VIOLET": violet2.add(rs.set[i]);  break;
			case "INDIGO": indigo2.add(rs.set[i]); break;
			case "BLUE": blue2.add(rs.set[i]);break;
			case "GREEN": green2.add(rs.set[i]); break;
			case "YELLOW": yellow2.add(rs.set[i]); break;
			case "ORANGE": orange2.add(rs.set[i]);  break;
			case "RED": red2.add(rs.set[i]); break;

			}	
		}
		Red7CardSet[] amountOfEach2 = {violet2, indigo2, blue2, green2, yellow2, orange2, red2}; 
		int highInd2 = 0;
		for (int i = 0; i < amountOfEach2.length; i++) {
			if (amountOfEach2[i].size> high2) {high2 = amountOfEach2[i].size; highInd2 = i;}
		}
		//System.out.println(high + " " + high2);
		Red7CardSet hiSet = amountOfEach[highInd];
		Red7CardSet hiSet2 = amountOfEach2[highInd2];
		if (high == high2) {
			return hiSet.get(hiSet.size-1).compareTo(hiSet2.get(hiSet2.size-1));
			
		}
		else if (high > high2) return 1;
		else return -1;
	}
	
	public int compareGreen(Red7CardSet rs) {
		//int[] evens = new int[this.getSize()];
		int evens = 0; int evens2 = 0; Red7Card highest =  new Red7Card("red", 7); 
		Red7Card highest2 =  new Red7Card("red", 7);
		for (int i =0; i < this.getSize(); i++) {
			if (this.set[i].getNumber() % 2 == 0) {evens++; highest = this.set[i];}
		}
		for (int i =0; i < rs.getSize(); i++) {
			if (rs.set[i].getNumber() % 2 == 0) {evens2++; highest2 = rs.set[i];}
		}
		if (evens == evens2) {
			return highest.compareTo(highest2);
		}
		else if (evens > evens2) return 1;
		else return -1;
		
	}
	
	public int compareBlue(Red7CardSet rs) {
		int clramt = 0; int clramt2 = 0;
		boolean[] colors = {false, false, false, false, false, false, false};
		for (int i =0; i< this.getSize(); i++) {
			switch(this.set[i].getColor().toUpperCase()) {
			case "VIOLET": colors[0] =  true; break;
			case "INDIGO": colors[1] = true; break;
			case "BLUE": colors[2] =  true; break;
			case "GREEN": colors[3] =  true; break;
			case "YELLOW": colors[4] =  true; break;
			case "ORANGE": colors[5] =  true; break;
			case "RED": colors[6] =  true; break;
			}	
		}
		boolean[] colors2 = {false, false, false, false, false, false, false};
		for (int i =0; i< rs.getSize(); i++) {
			switch(rs.set[i].getColor().toUpperCase()) {
			case "VIOLET": colors2[0] =  true; break;
			case "INDIGO": colors2[1] = true; break;
			case "BLUE": colors2[2] =  true; break;
			case "GREEN": colors2[3] =  true; break;
			case "YELLOW": colors2[4] =  true; break;
			case "ORANGE": colors2[5] =  true; break;
			case "RED": colors2[6] =  true; break;
			}	
		}
		for (int i =0; i< colors.length; i++) {
			if (colors[i]==true) clramt++;
			if (colors2[i]==true) clramt2++;
		}
		if (clramt == clramt2) return this.compareRed(rs);
		if (clramt > clramt2) return 1;
		else return -1;
	}
	public int compareIndigo(Red7CardSet rs) {
		int strtAmt=0; int strtAmt2=0; int high = 0; int high2 = 0;
		boolean[] numbers = {false, false, false, false, false, false, false};
		for (int i=0; i< this.size; i++) {
			switch(this.set[i].getNumber()) {
			case 1: numbers[0] = true; break;
			case 2: numbers[1] = true; break;
			case 3: numbers[2] = true; break;
			case 4: numbers[3] = true; break;
			case 5: numbers[4] = true; break;
			case 6: numbers[5] = true; break;
			case 7: numbers[6] = true; break;
			}
		}
		boolean[] numbers2 = {false, false, false, false, false, false, false};
		for (int i=0; i< rs.size; i++) {
			switch(rs.set[i].getNumber()) {
			case 1: numbers2[0] = true;break;
			case 2: numbers2[1] = true;break;
			case 3: numbers2[2] = true;break;
			case 4: numbers2[3] = true;break;
			case 5: numbers2[4] = true;break;
			case 6: numbers2[5] = true;break;
			case 7: numbers2[6] = true;break;
			}
		}
		int highInd = 0; int highInd2 = 0;
		for (int i=0; i < numbers.length; i++) {
			if (numbers[i] == true) {
				strtAmt++;
				if (strtAmt > high){ high = strtAmt; highInd = i;}
			}
			else {
				if (strtAmt > high) high = strtAmt;
				strtAmt = 0;
			}
			if (numbers2[i] == true) {
				strtAmt2++;
				if (strtAmt2 > high2) {high2 = strtAmt2; highInd2 = i;}
			}
			else {
				if (strtAmt2 > high2) high2 = strtAmt2;
				strtAmt2 = 0;
			}
		}
		if (high + high2 == 2) return this.compareRed(rs);
		if (high == high2) {
			Red7CardSet num1 = new Red7CardSet();
			Red7CardSet num2 = new Red7CardSet();
			for (int i=0; i <this.size; i++){
				if (this.set[i].getNumber()== highInd+1) num1.add(this.set[i]);
			}
			for (int i=0; i <rs.size; i++){
				if (rs.set[i].getNumber()== highInd2+1) num2.add(rs.set[i]);
			}
			return num1.compareRed(num2);}
		if (high > high2) return 1;
		else return -1;	
	}
	
	public int compareViolet(Red7CardSet rs) {
		Red7CardSet num1 = new Red7CardSet();
		Red7CardSet num2 = new Red7CardSet();
		int b4=0; int b42=0;
		for (int i=0; i< this.size; i++) {
			if (this.set[i].getNumber() < 4){ b4++;num1.add(this.set[i]);}
		}
		for (int i=0; i< rs.size; i++) {
			if (rs.set[i].getNumber() < 4){ b42++;num2.add(rs.set[i]);}
		}
		//System.out.println(b4 + " "+b42);
		if (b4 +b42 == 0) return 0;
		if (b4 == b42) return num1.compareRed(num2);
		if (b4 > b42) return 1;
		else return -1;
	}
	
	public String toString() {
		String r7Str = "";
		for (int i = 0; i < set.length; i++) {
			r7Str += set[i].toString();
		}
		return r7Str;
		
	}
}




