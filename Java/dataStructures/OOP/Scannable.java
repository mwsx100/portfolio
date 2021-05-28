package project2;
import java.util.ArrayList;
public interface Scannable {
	
	public static String EMPTY_BARCODE = "000000000000";
	
	default String getBarcode() {
		return EMPTY_BARCODE;
	}
	
	public static String generateBarcode(String name) {
			ArrayList<Integer> numList = new ArrayList<Integer>();
			ArrayList<Integer> numList2 = new ArrayList<Integer>();
			String barcode2 = "";
			if (name != null) {
				for (int i = 0; i < name.length(); i++) {
					if (Character.isLetter(name.charAt(i))) {
					int newNum = Character.getNumericValue(name.charAt(i)) - 9; 
					numList.add(newNum);	
					 }
					else { numList.add(0); }
					}
				for (int i = 0; i < numList.size() ; i= i + 2) {
					int a = i;
					int b = i + 1;
					int c = i + 2;
					if (b == numList.size()) break;
					if (c == numList.size() ) {
						int newNum = numList.get(a) + numList.get(b) + 0;
						numList2.add(newNum);
						break;
					}
					else {
					int newNum = numList.get(a) + numList.get(b) + numList.get(c);
					numList2.add(newNum);
				}
				}
				for (int i = 0; i < 6; i++) {
					if (i >= numList2.size()) 
						break;
					if (numList2.get(i) < 10) {
						barcode2 = barcode2 + "0" + numList2.get(i);
					}
					else {
					barcode2 = barcode2 + numList2.get(i);
					}
				}
				int barcodeL = barcode2.length();
				for (int i = 0; i < 12 - barcodeL ; i++) {
					barcode2 = barcode2 + "0";
				}
				return barcode2;
				}
			else return "00000000000000";
			}	
				
	}

