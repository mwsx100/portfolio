package project2;

public class Item implements Scannable, Comparable<Item> {
	
	private String name;
	private String barcode;
	private int amount;
	
	public Item(String name) {
		this.name = name;
		amount = 1;
		barcode = Scannable.generateBarcode(name);
	}
	
	public Item(String name, int amount) {
		this.name = name;
		this.amount = amount;
		barcode = Scannable.generateBarcode(name);
	}
	
	public String getName() {
		return name;
	}
	
	public String getBarcode() {
		return barcode;
	}
	
	public int getAmount() {
		return amount;
	}
	
	public void addStock(int amt) {
		amount = amount + amt;
	}
	
	public void sellUnit() {
		amount--;
	}
	
	public boolean equals(Item itm) {
		if (itm.getBarcode().equals(this.getBarcode()) )
				return true;
		else return false;
	}
	
	public int compareTo(Item itm) {
		return this.barcode.compareTo(itm.barcode);
	}
	
	public String toString() {
		return name + " (" + barcode + ") " + amount;
	}

}
