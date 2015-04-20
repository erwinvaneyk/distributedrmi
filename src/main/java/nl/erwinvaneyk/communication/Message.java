package nl.erwinvaneyk.communication;

import java.io.Serializable;
import java.util.Date;

import lombok.NonNull;
import nl.erwinvaneyk.core.NodeAddress;

public interface Message extends Serializable {

	String getContext();

	NodeAddress getOrigin();

	Date getTimestampSend();

	Date getTimestampReceived();

	void setTimestampReceived();

	Serializable get(String fieldName);

	Message put(String fieldName, Serializable val);
}
