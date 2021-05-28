package project5;

//This class represents a "connection" in a network (e.g. a wired connection,
//a bluetooth connection, etc. depending on the network structure). It is
//complete and you do not need to edit it for this project.

//If you are interested, this class contains two "patterns" common in
//software development: (1) generating unique ids per instance and
//(2) creating a "factory" that creates instances.

import org.apache.commons.collections15.Factory;
import java.awt.Color;
import java.util.Random;

/**
 * A host in the network.
 * 
 * @author K. Raven Russell
 */
public class Connection {
	/**
	 * The last id given to a connection.
	 */
	public static int LAST_ID = -1;

	/**
	 * A random number generator. Do not change the seed!
	 */
	private static final Random rand = new Random(0);

	/**
	 * The unique id of this connection.
	 */
	private final int id;

	/**
	 * The weight of the edge.
	 */
	private final int weight;

	/**
	 * The color of this connection in the visualization.
	 */
	private Color color = Color.BLACK;

	/**
	 * Makes a new connections with a random weight between 1 and 10.
	 */
	private Connection() {
		id = ++LAST_ID;
		weight = rand.nextInt(10) + 1;
	}

	/**
	 * Returns the id of the connection.
	 * 
	 * @return the connection's unique identifier
	 */
	public int getId() {
		return id;
	}

	/**
	 * Returns the color of the connection in the simulation.
	 * 
	 * @return the connection's current color
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * Returns the weight of the connection (time to send across this wire).
	 * 
	 * @return the connection's weight
	 */
	public int getWeight() {
		return weight;
	}

	/**
	 * Sets the color of the connection in the simulation.
	 * 
	 * @param color
	 *            the new color to use
	 */
	public void setColor(Color color) {
		this.color = color;
	}

	/**
	 * The string representation of a connection is just it's weight.
	 * 
	 * @return the string representation of the connection
	 */
	@Override
	public String toString() {
		return "" + weight;
	}

	/**
	 * Sets the hash code of the connection (weight * id).
	 * 
	 * @return the hash code of the connection
	 */
	@Override
	public int hashCode() {
		return weight * id;
	}

	/**
	 * Two connections are equal if they have the same id.
	 * 
	 * @return whether two connections are equal
	 */
	@Override
	public boolean equals(Object other) {
		if (other instanceof Connection) {
			return this.id == ((Connection) other).id;
		}
		return false;
	}

	/**
	 * This is a code pattern called a "factory". A factory for connections
	 * makes instances of connections!
	 * 
	 * @return a factory that can make connections
	 */
	public static Factory<Connection> getFactory() {
		return new Factory<Connection>() {
			public Connection create() {
				return new Connection();
			}
		};
	}
}