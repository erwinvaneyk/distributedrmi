package nl.erwinvaneyk.core;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NodeAddressImpl implements NodeAddress {

	public static final String SEPARATOR = "-";

	private final String type;

	private final int id;

	private final Address location;

	@Override
	public String getIdentifier() {
		return type + SEPARATOR + id;
	}

	@Override
	public Address getLocation() {
		return location;
	}

	@Override
	public String toString() {
		return location + "/" + type + ((id != -1) ? SEPARATOR + id : "");
	}
}
