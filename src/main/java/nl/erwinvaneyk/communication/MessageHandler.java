package nl.erwinvaneyk.communication;

import java.rmi.Remote;
import java.rmi.RemoteException;

// TODO: separate from RMI logic
public interface MessageHandler extends Remote {

	void onMessageReceived(Message message) throws RemoteException;

	Message onRequestReceived(Message message) throws RemoteException;

	MessageHandler bind(String key, MessageReceivedHandler handler) throws RemoteException;

	MessageHandler bind(MessageReceivedHandler handler) throws RemoteException;
}
