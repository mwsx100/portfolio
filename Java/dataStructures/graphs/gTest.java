package graphs;

public class gTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Graph<String> bob = new Graph();
		//int a = 1;
		bob.addVertex("A");
		
		bob.addVertex("B");
		bob.addVertex("C");
		
		bob.addVertex("D");
		bob.addVertex("E");
		bob.addEdge(0, 1, 2);
		bob.addEdge(0, 2, 86);
		bob.addEdge(0, 3, 7);
		bob.addEdge(1, 2, 4);
		
		bob.addEdge(2, 3, 5);
		bob.addEdge(3, 4, 3);
		
		System.out.println(bob.mST("C"));
		
		//System.out.println(bob.getMinV(0));
		
		Graph<String> bob2 = new Graph();
		bob2.addVertex("0");
		
		bob2.addVertex("1");
		bob2.addVertex("2");
		
		bob2.addVertex("3");
		bob2.addVertex("4");
		bob2.addVertex("5");
		bob2.addEdge(0, 1, 1);
		bob2.addEdge(0, 3, 8);
		bob2.addEdge(1, 2, 2);
		bob2.addEdge(1, 3, 2);
		bob2.addEdge(1, 4, 6);
		bob2.addEdge(2, 5, 2);
		bob2.addEdge(3, 4, 1);
		bob2.addEdge(4, 5, 1);
		
		
	System.out.println(bob2.mST("0"));
		
	//	System.out.println(bob2.getMinV(0));
		

	}

}
