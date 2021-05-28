package project2;

public class TestInventory {

	public static void main(String[] args) {
		Inventory inv = new Inventory(4);
	
		
		inv.restockItem("phone", 1);
		inv.restockItem("food", 6);
		inv.restockItem("game", 4);
		inv.restockItem("blanket", 2);
		System.out.println(inv.toString());
		inv.restockItem("medicine", 3);
	    inv.sellItem("phone");
	    System.out.println(inv.toString());
	    inv.restockItem("blanket", 1);
	    System.out.println(inv.toString());
		
		
		
	}

}
