package nl.erwinvaneyk.communication;

import lombok.extern.slf4j.Slf4j;
import nl.erwinvaneyk.communication.exceptions.CommunicationException;
import nl.erwinvaneyk.communication.rmi.RMISocket;
import nl.erwinvaneyk.core.NodeAddress;
import nl.erwinvaneyk.core.NodeState;
import nl.erwinvaneyk.core.logging.LogNode;

import java.util.HashSet;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

// TODO: separate from rmi
@Slf4j
public class ConnectorImpl implements Connector {
    private final NodeState me;

    public ConnectorImpl(NodeState me) {
        this.me = me;
    }

    @Override
    public void sendMessage(Message message, NodeAddress destination) throws CommunicationException {
        RMISocket socket = new RMISocket(destination);
        socket.sendMessage(message);
    }

    @Override
    public Message sendRequest(Message message, NodeAddress destination) throws CommunicationException {
        RMISocket socket = new RMISocket(destination);
        return socket.sendRequest(message);
    }

    @Override
    public Set<NodeAddress> broadcast(Message message) {
        Set<NodeAddress> nodes = new HashSet<>(me.getConnectedNodes());
        nodes.add(me.getAddress());

        return broadcast(message, nodes);
    }

    public Set<NodeAddress> broadcast(Message message, String identifierFilter) {
        Set<NodeAddress> nodes = new HashSet<>(me.getConnectedNodes());
        nodes.add(me.getAddress());

        nodes = nodes.stream()
                    .filter(n -> n.getIdentifier().matches("(.*)" + identifierFilter + "(.*)"))
                    .collect(toSet());

        return broadcast(message, nodes);
    }

    private Set<NodeAddress> broadcast(Message message, Set<NodeAddress> nodes) {
        nodes.stream().forEach(address -> {
            try {
                sendMessage(message, address);
            } catch (CommunicationException e) {
                e.printStackTrace();
            }
        });

        return nodes;
    }

	@Override
	public void log(Message message) {
		if(me.getConnectedNodes().stream().anyMatch(node -> LogNode.NODE_TYPE.equals(node.getType()))) {
			broadcast(message, LogNode.NODE_TYPE);
		} else {
			log.debug("No logger: " + message);
		}
	}
}
