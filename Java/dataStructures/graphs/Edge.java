package graphs;

public class Edge  implements Comparable<Edge> {
	private int vertex1;
	private int vertex2;
	private int weight;
	
	public Edge(int v1, int v2, int weight) {
	vertex1 = v1; vertex2 = v2; this.weight = weight; 
	//if (vertex1 > vertex2) {
		//vertex1 = v2; vertex2 = v1;
	//}
	
	}
	
	public int getV1() {
		return vertex1;
	}
	
	public int getV2() {
		return vertex2;
	}
	
	public int getW() {
		return weight;
	}
	
	public boolean contains(int v) {
		if (vertex1 == v || vertex2 == v) return true;
		else return false;
	}
	
	public int compareTo(Edge e) {
		if (this.weight == e.weight) return 0;
		if (this.weight > e.weight) return -1;
		else return 1;
	}
	
	public String toString() {
		return vertex1 + "--" + weight + "--" + vertex2;
	}

}
