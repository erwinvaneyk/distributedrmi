package nl.erwinvaneyk.core;

import java.io.Serializable;
import java.util.HashSet;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@ToString
public class NodeState implements Serializable {

	@Getter private final NodeAddress address;

	@Getter private final String clusterId;

	@Getter private final HashSet<NodeAddress> connectedNodes = new HashSet<>();
}
