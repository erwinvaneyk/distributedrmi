package nl.erwinvaneyk.core;

import java.io.Serializable;

/**
 * Interface responsible for providing a single consistent, shareable identifier
 * for nodes across the system.
 */
public interface NodeAddress extends Serializable {

	/**
	 * The type describes the type of the node. This is by default be the classname,
	 *  but can also be some custom identifier
	 *
	 * @return a string-based type of the node.
	 */
	String getType();

	/**
	 * The identifier should be an unique value to distinguish this node from other nodes.
	 *
	 * @return a string-based identifier of the node
	 */
	String getIdentifier();

	/**
	 * The location provides the actual ip:port on which the node can be found.
	 *
	 * @return the address of the node
	 */
	Address getLocation();
}
