package nl.erwinvaneyk.communication;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import nl.erwinvaneyk.core.NodeAddress;

public interface Message extends Serializable {

	String getContext();

	NodeAddress getOrigin();

	long getTimestampSend();

	long getTimestampReceived();

	void setTimestampReceived();

	Serializable get(String fieldName);

	Map<String, Serializable> getFields();

	Serializable getOrThrow(String fieldname);

	Message put(String fieldName, Serializable val);
}
