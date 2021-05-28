package graphs;
import java.util.ArrayList;

public class Graph <T extends Comparable<T>> {
	
	private ArrayList<T> vertices;
	private int[][] edges;
	private int size;
	
	public Graph() {
		size = 0;
		edges = new int[size][size];
		vertices = new ArrayList<T>();
		for (int i = 0; i< size; i++) {
			for (int j = 0; j < size; j++) {
				edges[i][j] = 0;
			}
		}
	}
	
	public void addVertex(T data) {
		vertices.add(data);
		
		//System.out.println(edges.length);
		//System.out.println(size);
		if (size >= edges.length) {
		int[][] newEdges = new int[this.size *2][this.size*2];
		for (int i = 0; i< size*2; i++) {
			for (int j = 0; j < size*2; j++) {
				if (i < size -1 && j < size -1) {
				newEdges[i][j] = edges[i][j]; }
				else newEdges[i][j] = 0;
			}
		}
		edges = newEdges;
		}
		size++;
		//size++;
	}
	
	public int indexOf(T data) {
		return vertices.indexOf(data);
	}
	
	public void addEdge(int x, int y, int weight) {
		edges[x][y] = weight;
		edges[y][x] = weight;
	}
	
	public int getMinV(int vertex) {
		int min = Integer.MAX_VALUE;
		int minV = 0;
		for (int i = 0; i < vertices.size(); i++) {
			if (edges[vertex][i] < min && edges[vertex][i] !=0) {
				min = edges[vertex][i]; minV = i;
			}
		}
		return minV;
	}
	
	
		
	
	
	public String mST(T ver) {
		int vertex = vertices.indexOf(ver);
		String MST = "";
		ArrayList<Edge> ee = new ArrayList<Edge>();
		for (int i = 0; i < size; i++) { 
			for (int j = 0; j < size; j++) {
				if (edges[i][j] > 0 && i <= j ){
					Edge e = new Edge(i, j, edges[i][j]);
					ee.add(e);
				}
			}	
		}
		Heap<Edge> vis = new Heap();
		boolean[] visited = new boolean[size];
		for (int i = 0; i < size; i++) visited[i] = false;
		visited[vertex] = true;
		int vCount = 1;
		int v = 0;
		while (vCount < size) {
		for (int i = 0; i < ee.size(); i++) {
			if (ee.get(0).contains(vertex)) {
				vis.enqueue(ee.remove(0)); continue;
			}
			if (ee.get(i).contains(vertex) ) {
				vis.enqueue(ee.remove(i)); i =0;
				}
			}
		Edge x = new Edge(1,1,1);
		x = vis.dequeue();
		if (x.getV1() == vertex) v = x.getV2();
		else if (x.getV2() == vertex) v = x.getV1();
		else {vertex = x.getV1(); v = x.getV2();}//if vertex v isnt already visited add it to the string
		if (visited[v]==false && visited[vertex] ==true){ MST += vertices.get(x.getV1()) + "--" + x.getW() + "--" +vertices.get(x.getV2()) + "\n" ;//x.toString() + " m";
		visited[v] = true;
		vCount++; vertex = v; continue; }
		if (visited[v]==true && visited[vertex] ==false){ MST += vertices.get(x.getV1()) + "--" + x.getW() + "--" +vertices.get(x.getV2()) + "\n";//+ x.toString() + " m";
		visited[vertex] = true;
		vCount++;   continue;}
		vertex = v;
		}	
		return MST;
	}

}

