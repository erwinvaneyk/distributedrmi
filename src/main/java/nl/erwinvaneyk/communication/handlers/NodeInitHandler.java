package nl.erwinvaneyk.communication.handlers;

import nl.erwinvaneyk.communication.BasicMessage;
import nl.erwinvaneyk.communication.Connector;
import nl.erwinvaneyk.communication.Message;
import nl.erwinvaneyk.communication.MessageReceivedHandler;
import nl.erwinvaneyk.communication.exceptions.CommunicationException;
import nl.erwinvaneyk.core.NodeAddress;
import nl.erwinvaneyk.core.NodeState;

/**
 * Responsible for generating and reserving an id for a new node
 */
public class NodeInitHandler implements MessageReceivedHandler {

	public static final String NODE_CONNECT = "NODE_INIT";
	private final Connector connector;
	private final NodeState nodeState;

	public NodeInitHandler(Connector connector, NodeState nodeState) {
		this.connector = connector;
		this.nodeState = nodeState;
	}

	@Override
	public Message onMessage(Message message) throws CommunicationException {
		String nodeType = (String) message.getOrThrow("type");
		// TODO: reserve id (to prevent conflicts)
		Message response = new BasicMessage(NODE_CONNECT, nodeState.getAddress())
				.put("id", determineId(nodeType))
				.put("clusterId", nodeState.getClusterId());
		return response;
	}

	@Override
	public String getContext() {
		return NODE_CONNECT;
	}

	private int determineId(String nodeType) {
		return Math.max(nodeState.getConnectedNodes().stream()
				.filter(node -> node.getType().equals(nodeType))
				.mapToInt(NodeAddress::getId)
				.max()
				.orElse(0), nodeState.getAddress().getId()) + 1;
	}
}
