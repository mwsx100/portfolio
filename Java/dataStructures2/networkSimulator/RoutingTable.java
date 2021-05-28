package project5;
//package project4;
//This class is completed for you. No need to edit.
//But you might want to know what it does if you're
//debugging your project.

/**
 * A routing table for a host in the network. This is just a hash table for
 * mapping Host(dest) to Host(next hop), but with a special toString().
 * 
 * @author K. Raven Russell
 */
public class RoutingTable extends HashTable<Host, Host> {
	/**
	 * Starts with a hash table of size 2.
	 */
	public RoutingTable() {
		super(2);
	}

	/**
	 * Produces the HTML for display of a routing table in the simulation.
	 * 
	 * @return the HTML for a routing table
	 */
	public String toString() {
		if (size() < 1)
			return "<br />No Routes";

		StringBuilder sb = new StringBuilder();
		sb.append("<br />Routing Table: <table border=0>");
		sb.append("<tr><th>To</th><th>Next Hop</th></tr>");
		for (KeyValuePair<Host, Host> pair : storage.getAllPairs()) {
			// sb.append("\n");
			sb.append("<tr><td>");
			sb.append(pair.getKey());
			// sb.append("->");
			sb.append("</td><td>");
			sb.append(pair.getValue());
			sb.append("</td></tr>");
		}
		sb.append("</table>");
		return sb.toString();
	}
}