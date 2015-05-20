package nl.erwinvaneyk.communication;

import java.rmi.Remote;
import java.rmi.RemoteException;

// TODO: separate from RMI logic
public interface MessageDistributor extends Remote {

	void onMessageReceived(Message message) throws RemoteException;

	Message onRequestReceived(Message message) throws RemoteException;

	MessageDistributor bind(String key, MessageReceivedHandler handler) throws RemoteException;

	MessageDistributor bind(MessageReceivedHandler handler) throws RemoteException;
}
