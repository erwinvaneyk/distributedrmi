package nl.erwinvaneyk.communication.exceptions;

import java.rmi.RemoteException;

// receiver not found
// socket/network errors
// internal node exception
// TODO: separate from RMI logic
public class CommunicationException extends RemoteException {

	public CommunicationException(String message, Exception e) {
		super(message, e);
	}

	public CommunicationException(String s) {

	}
}
