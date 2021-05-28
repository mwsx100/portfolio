package heaps;

public class TestingH {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Heap<Integer> bob = new Heap<Integer>();
		
		Integer a = new Integer(1);
		Integer b = new Integer(5);
		Integer c = new Integer(3);
		Integer d = new Integer(7);
		Integer e = new Integer(11);
		Integer f = new Integer(8);
		
		bob.enqueue(1);
		bob.enqueue(5);
		bob.enqueue(3);
		bob.enqueue(7);
		bob.enqueue(11);
		bob.enqueue(8);
		
		System.out.println(bob);
		
	
		System.out.println(bob.dequeue() + "\n");
		System.out.println(bob);
		System.out.println(bob.dequeue() + "\n");
		System.out.println(bob);
		System.out.println(bob.dequeue() + "\n");
		System.out.println(bob);
		System.out.println(bob.dequeue() + "\n");
		System.out.println(bob);
		System.out.println(bob.dequeue() + "\n");
		System.out.println(bob);
		System.out.println(bob.dequeue() + "\n");
		System.out.println(bob);
		System.out.println(bob.dequeue() + "\n");
		System.out.println(bob);
		
		System.out.println(3 / 2);

	}

}
