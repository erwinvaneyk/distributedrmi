package nl.erwinvaneyk.communication;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;

import lombok.Data;
import lombok.EqualsAndHashCode;
import nl.erwinvaneyk.core.NodeAddress;

@Data
@EqualsAndHashCode(exclude = {"timestampReceived"})
public class BasicMessage implements Message {

	private final String context;
	private final NodeAddress origin;
	private final Date timestampSend;
	private Date timestampReceived;
	private final HashMap<String, Serializable> contents = new HashMap<>();

	public BasicMessage(String context, NodeAddress origin) {
		this.context = context;
		this.origin = origin;
		this.timestampSend = new Date();
	}

	public void setTimestampReceived() {
		timestampReceived = new Date();
	}

	@Override
	public Serializable get(String fieldName) {
		return contents.get(fieldName);
	}

	@Override
	public Message put(String fieldName, Serializable val) {
		contents.put(fieldName, val);
		return this;
	}
}
