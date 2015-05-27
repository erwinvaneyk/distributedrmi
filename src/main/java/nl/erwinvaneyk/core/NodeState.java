package nl.erwinvaneyk.core;

import static java.util.stream.Collectors.toSet;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import nl.erwinvaneyk.core.logging.LogNode;

@AllArgsConstructor
@ToString
public class NodeState implements Serializable {

	@Getter private final NodeAddress address;

	@Getter private final String clusterId;

	@Getter private final HashSet<NodeAddress> connectedNodes = new HashSet<>();

	public Set<NodeAddress> getNonLogNodes() {
		return connectedNodes.stream().filter(n -> !n.getType().equals(LogNode.NODE_TYPE)).collect(toSet());
	}
}
