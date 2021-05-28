package project5;

//This class represents a "host" in a network (i.e. a computer or
//a router depending on the network structure). It is complete and
//you do not need to edit it for this project.

//If you are interested, this class contains two "patterns" common in
//software development: (1) generating unique ids per instance and
//(2) creating a "factory" that creates instances.

import org.apache.commons.collections15.Factory;

import java.awt.Color;

/**
 * A host in the network.
 * 
 * @author K. Raven Russell
 */
public class Host {
	/**
	 * The last id given to a node.
	 */
	public static int LAST_ID = -1;

	/**
	 * The unique id of this node.
	 */
	private final int id;

	/**
	 * The color of this node in the visualization.
	 */
	private Color other = ThreeTenNetwork.COLOR_DEFAULT_NODE;

	/**
	 * The routing table that this node is aware of.
	 */
	private RoutingTable routes = new RoutingTable();

	/**
	 * Sets the id of the host.
	 */
	private Host() {
		id = ++LAST_ID;
	}

	/**
	 * Creates a host with a given ID, but only if that host has previously been
	 * generated previously with the standard constructor. (This is to allow
	 * making host objects that are equal to each other, but not hosts with id
	 * numbers out of sequence.)
	 * 
	 * @param id
	 *            the unique identifier of the node
	 */
	public Host(int id) {
		if (id < 0 || id > LAST_ID) {
			throw new IllegalArgumentException("Cannot create a node with an arbitrary id.");
		}
		this.id = id;
	}

	/**
	 * Returns the id of the host.
	 * 
	 * @return the host's unique identifier
	 */
	public int getId() {
		return id;
	}

	/**
	 * Returns the color of the host in the simulation.
	 * 
	 * @return the host's current color
	 */
	public Color getColor() {
		return other;
	}

	/**
	 * Sets the color of the host in the simulation.
	 * 
	 * @param other
	 *            the new color to use
	 */
	public void setColor(Color other) {
		this.other = other;
	}

	/**
	 * Sets the hashcode of the host to be a hash of the string value which
	 * contains the id.
	 * 
	 * @return the hash code of the host
	 */
	@Override
	public int hashCode() {
		return toString().hashCode();
	}

	/**
	 * The string representation of a host is it's IPv4 address.
	 * 
	 * @return the string representation of the host
	 */
	public String toString() {
		return "192.168.1." + id;
	}

	/**
	 * Returns the routing table for host.
	 * 
	 * @return the full routing table
	 */
	public RoutingTable getRoutingTable() {
		return routes;
	}

	/**
	 * Two hosts are equal if they have the same id.
	 * 
	 * @return whether two hosts are equal
	 */
	@Override
	public boolean equals(Object o) {
		if (o instanceof Host) {
			return this.id == ((Host) o).id;
		}
		return false;
	}

	/**
	 * This is a code pattern called a "factory". A factory for hosts makes
	 * instances of hosts!
	 * 
	 * @return a factory that can make hosts
	 */
	public static Factory<Host> getFactory() {
		return new Factory<Host>() {
			public Host create() {
				return new Host();
			}
		};
	}
}