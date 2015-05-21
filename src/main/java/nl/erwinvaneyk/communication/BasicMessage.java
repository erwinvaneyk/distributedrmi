package nl.erwinvaneyk.communication;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import lombok.Data;
import lombok.EqualsAndHashCode;
import nl.erwinvaneyk.core.NodeAddress;

@Data
@EqualsAndHashCode(exclude = {"timestampReceived"})
public class BasicMessage implements Message {

	private final String context;
	private final NodeAddress origin;
	private final long timestampSend;
	private long timestampReceived;
	private final HashMap<String, Serializable> contents = new HashMap<>();

	public BasicMessage(String context, NodeAddress origin) {
		this.context = context;
		this.origin = origin;
		this.timestampSend = new Date().getTime();
	}

	public void setTimestampReceived() {
		timestampReceived = new Date().getTime();
	}

	@Override
	public Serializable get(String fieldName) {
		return contents.get(fieldName);
	}

	@Override
	public Map<String, Serializable> getFields() {
		return contents;
	}

	@Override
	public Serializable getOrThrow(String fieldname) {
		Serializable val = get(fieldname);
		if(val == null) {
			throw new IllegalArgumentException("Missing expected argument '" + fieldname + "' in message '" + this + "'");
		}
		return val;
	}

	@Override
	public Message put(String fieldName, Serializable val) {
		contents.put(fieldName, val);
		return this;
	}
}
