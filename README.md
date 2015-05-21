# distributedrmi

[![Build Status](https://travis-ci.org/erwinvaneyk/distributedrmi.svg)](https://travis-ci.org/erwinvaneyk/distributedrmi)

### Installation:
To use it in your project add the following to your `pom.xml`:

```xml
<repositories>
	<repository>
		<id>distributedrmi-mvn-repo</id>
		<url>https://raw.github.com/erwinvaneyk/distributedrmi/mvn-repo/</url>
		<snapshots>
			<enabled>true</enabled>
			<updatePolicy>always</updatePolicy>
		</snapshots>
	</repository>
</repositories>

<dependency>
  <groupId>nl.erwinvaneyk</groupId>
  <artifactId>distributedrmi</artifactId>
  <version>1.0.2</version>
</dependency>
```

### Example:
```java
// Setup cluster
ClusterFactory cf = ClusterFactory.getBasicFactory();
ClusterFactory lf = ClusterFactory.getLogFactory();
Node node1 = cf.startCluster(1819, "test-cluster");
Node node2 = cf.connectToCluster(1820, node1.getState().getAddress());

// Setup logger printing to the terminal
LogNode logNode1 = (LogNode) lf.connectToCluster(1821, node1.getState().getAddress());
logNode1.setLogger(new PrintLogger());

// Create a Connector; needed for sending messages
Connector connector = new ConnectorImpl(node1);
Message message = new LogMessage("Test message", node1.getState().getAddress());
connector.log(message);

// Shutdown cluster (it does not matter in what order the nodes are shutdown)
node1.disconnect();
node2.disconnect();
logNode1.disconnect();
```
Note: In this case the nodes are still running from a common thread.

### InfluxDB
This project provides advanced performance-logging capabilities. By default InfluxDB is included. To enable it, make sure that a InfluxDB instance is running on your machine.

The easiest way to do this is to use the preconfigured docker-image
```bash
# run influxdb
docker run -d -p 8083:8083 -p 8086:8086 --expose 8090 --expose 8099 --name="influxdb-rmi" tutum/influxdb

# run grafana
docker run -d -p 3000:3000 --name="grafana-rmi" grafana/grafana

```