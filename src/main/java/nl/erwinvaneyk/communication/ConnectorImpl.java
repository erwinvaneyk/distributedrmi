package nl.erwinvaneyk.communication;

import nl.erwinvaneyk.communication.exceptions.CommunicationException;
import nl.erwinvaneyk.communication.rmi.RMISocket;
import nl.erwinvaneyk.core.Node;
import nl.erwinvaneyk.core.NodeAddress;

import java.util.Set;

import static java.util.stream.Collectors.toSet;

public class ConnectorImpl implements Connector {
    private final Node me;

    public ConnectorImpl(Node me) {
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
    public Set<NodeAddress> broadcast(Message message, String... regexFilter) {
        Set<NodeAddress> nodes = me.getState().getConnectedNodes();

        if(regexFilter.length == 1) {
            nodes = nodes.stream().filter(n -> n.getIdentifier().matches(regexFilter[1])).collect(toSet());
        }

        nodes.stream().forEach(address -> {
                try {
                    sendMessage(message, address);
                } catch (CommunicationException e) {
                    e.printStackTrace();
                }
            });

        return nodes;
    }
}
