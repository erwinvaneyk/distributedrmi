package nl.erwinvaneyk.communication.handlers;

import java.util.HashSet;
import java.util.Set;

import nl.erwinvaneyk.communication.BasicMessage;
import nl.erwinvaneyk.communication.Message;
import nl.erwinvaneyk.communication.MessageReceivedHandler;
import nl.erwinvaneyk.communication.Socket;
import nl.erwinvaneyk.communication.exceptions.CommunicationException;
import nl.erwinvaneyk.communication.rmi.RMISocket;
import nl.erwinvaneyk.core.NodeAddress;
import nl.erwinvaneyk.core.NodeState;

/**
 * Responsible for connecting nodes with each other. Also returns the node state of this node.
 */
public class NodeConnectHandler implements MessageReceivedHandler {

	public static final String CONNECT = "NODE_CONNECT";
	private final NodeState nodeState;

	public NodeConnectHandler(NodeState nodeState) {
		this.nodeState = nodeState;
	}

	@Override
	public Message onMessage(Message message) throws CommunicationException {
		nodeState.getConnectedNodes().add(message.getOrigin());
		return new BasicMessage(CONNECT, nodeState.getAddress())
				.put("state", nodeState);
	}

	@Override
	public String getContext() {
		return CONNECT;
	}

	public static Set<NodeAddress> discoverNetwork(NodeAddress me, NodeAddress entryPoint) throws CommunicationException {
		Set<NodeAddress> connectedNodes = new HashSet<>();
		connectedNodes.add(entryPoint);
		Message connectMessage = new BasicMessage(CONNECT, me);
		Socket socket = new RMISocket(entryPoint);
		Message response = socket.sendRequest(connectMessage);
		NodeState otherState = (NodeState) response.getOrThrow("state");
		otherState.getConnectedNodes().forEach(na -> {
			try {
				discoverNetwork(me, na, connectedNodes);
			}
			catch (CommunicationException e) {
				e.printStackTrace();
			}
		});
		return connectedNodes;
	}

	private static void discoverNetwork(NodeAddress me, NodeAddress otherNode, Set<NodeAddress> nodes) throws CommunicationException {
		if(nodes.contains(otherNode) || me.equals(otherNode)) {
			return;
		}
		nodes.add(otherNode);
		Message connectMessage = new BasicMessage(CONNECT, me);
		Socket socket = new RMISocket(otherNode);
		Message response = socket.sendRequest(connectMessage);
		NodeState otherState = (NodeState) response.getOrThrow("state");
		otherState.getConnectedNodes().forEach(na -> {
			try {
				discoverNetwork(me, na, nodes);
			}
			catch (CommunicationException e) {
				e.printStackTrace();
			}
		});
	}


}
