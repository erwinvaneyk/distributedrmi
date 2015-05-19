# distributedrmi

Example:
```java
// Setup cluster
Node node1 = NodeImpl.startCluster(1819, "test-cluster");
Node node2 = NodeImpl.connectToCluster(1820, node1.getState().getAddress());

// Setup logger printing to the terminal
LogNode logNode1 = LogNode.connectToCluster(1821, node1.getState().getAddress());
logNode1.setLogger(new PrintLogger());

// Create a Connector; needed for sending messages
Connector connector = new ConnectorImpl(node1);
Message message = new LogMessage("Test message", node1.getState().getAddress());
connector.log(message);

// Shutdown cluster (it does not matter in what order the nodes are shutdown)
node1.disconnect();
node2.disconnect();
```
Note: In this case the nodes are still running from a common thread.
