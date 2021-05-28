package project2;

public class Inventory {
	private Item[] items;
	
	public Inventory(int size) {
		items = new Item[size];
	}
	
	public void restockItem(String prodName, int count) {
		String barcode1 = Scannable.generateBarcode(prodName);
		for (int i = 0; i < items.length; i++) {
			if (items[i] == null) {
				items[i] = new Item("0", 0);
				 }
		}
		
		for (int i = 0; i < items.length; i++) {
			
			if (barcode1.equals(items[i].getBarcode())) {
				items[i].addStock(count);
				break; }
			if (i == items.length -1 ) {
				Item zero = new Item("0", 0);
				if (zero.compareTo(items[i]) < 0) {
					System.out.println("Your inventory is full.\n");
					break;
				}
				for(int j = items.length - 1; j >= 0; j--) {
					if (barcode1.compareTo(items[j].getBarcode()) > 0 && j != items.length - 1 && j != 0) {
						items[j+1] = items[j];
					}
					if (barcode1.compareTo(items[j].getBarcode()) < 0 && j != items.length-1) {
						items[j+1] = new Item(prodName, count);
						break;
					}
					if (j == 0 && barcode1.compareTo(items[j].getBarcode()) > 0) {
						items[j+1] = items[j];
						items[j] = new Item(prodName, count);
						break;
					}
					
				}
				
			}
		}
		
	}
	
	public void sellItem(String name) {
		String barcode = Scannable.generateBarcode(name);
		for (int i = 0; i < items.length; i++){
			if (barcode.equals(items[i].getBarcode())) {
				items[i].sellUnit();
				if (items[i].getAmount() == 0) {
					for (int j = i; j < items.length; j++) {
						if (j != items.length -1)
						items[j] = items[j+1];
						else items[j] = new Item("0", 0);
					}
				}
			}
		}
	}
	
	public String toString() {
		String rString = "";
		for (int i = 0; i < items.length; i++) {
			if (items[i] == null)
				break;
			rString = rString + items[i].toString() + "\n";
		}
		return rString;
	}
	

}
