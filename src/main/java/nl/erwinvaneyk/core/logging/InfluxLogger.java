package nl.erwinvaneyk.core.logging;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import nl.erwinvaneyk.communication.Message;
import nl.erwinvaneyk.core.Address;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Database;
import org.influxdb.dto.Serie;
import retrofit.RetrofitError;

@Slf4j
public class InfluxLogger implements RawLogger {

	public static final String DEFAULT_DATABASE = "distributedrmi";
	public static final TimeUnit TIME_UNIT = TimeUnit.MILLISECONDS;
	public static final String FIELD_ORIGIN = "origin";
	public static final String FIELD_SUBJECT = "subject";
	public static final String FIELD_TIMESTAMP_SEND = "timestamp_send";
	public static final String FIELD_TIMESTAMP_RECEIVED = "timestamp_received";
	public static final String FIELD_CONTEXT = "context";
	public static final String FIELD_DELIVERY_DURATION = "delivery_duration";

	private static InfluxLogger influxLogger;

	private final InfluxDB influxDB;
	private final String database;
	private boolean available = true;

	/**
	 *
	 * @param address default: http://172.17.0.2:8086
	 * @param username default: root
	 * @param password default: root
	 */
	public InfluxLogger(Address address, String username, String password, String database) {
		influxDB = InfluxDBFactory.connect(address.toString(), username, password);
		this.database = database;
		createDatabaseIfAbsent(database);
	}

	public static InfluxLogger getInstance() {
		if(influxLogger == null) {
			influxLogger =  new InfluxLogger(new Address("localhost",8086), "root", "root", InfluxLogger.DEFAULT_DATABASE);
			influxLogger.createDatabaseIfAbsent(influxLogger.database);
		}
		return influxLogger;
	}

	public void log(@NonNull String subject,@NonNull Map<String,Object> args) {
		if(!available) {
			return;
		}
		if(args.isEmpty()) {
			log.warn("Cannot write a serie with no arguments to subject: {}", subject);
			return;
		}
		Serie.Builder builder = new Serie.Builder(subject);
		String[] columns = args.keySet().toArray(new String[args.size()]);
		builder.columns(columns);
		builder.values(args.values().toArray(new Object[args.size()]));
		Serie serie = builder.build();
		log.debug("query to influxDB: {}", serie);
		try {
			influxDB.write(database, TIME_UNIT, serie);
		} catch(RuntimeException e) {
			log.error("Failed to write serie to InfluxDb of {} with arguments {} (reason: {})", subject, args, e.getMessage());
		}
	}

	@Override
	public void log(Message message) {
		log(message.getContext(), message);
	}

	@Override
	public void log(String subject, Message message) {
		Map<String, Object> args = new HashMap<>();
		for(Map.Entry<String, Serializable> contentArg : message.getFields().entrySet()) {
			args.put(contentArg.getKey(), contentArg.getValue().toString());
		}
		args.put(FIELD_ORIGIN, message.getOrigin() != null ? message.getOrigin().toString() : "");
		args.put(FIELD_SUBJECT, subject);
		args.put(FIELD_CONTEXT, message.getContext());
		args.put(FIELD_DELIVERY_DURATION, message.getTimestampReceived() - message.getTimestampSend());
		args.put(FIELD_TIMESTAMP_RECEIVED, message.getTimestampReceived());
		args.put(FIELD_TIMESTAMP_SEND, message.getTimestampSend());
		log(subject, args);
	}

	@Override
	public boolean isAvailable() {
		try {
			return influxDB.ping() != null;
		} catch (RetrofitError e) {
			return false;
		}
	}

	void deleteDatabase() {
		if(available) {
			influxDB.deleteDatabase(database);
			log.debug("Deleted InfluxDB database: {}", database);
		} else {
			log.warn("Could not delete database '{}'; no connection to InfluxDB");
		}
	}

	void createDatabaseIfAbsent(String database) {
		if(isAvailable()) {
			if(!influxDB.describeDatabases().stream().map(Database::getName).anyMatch(db -> db.equals(database))) {
				log.debug("Created InfluxDB database: {} ", database);
				influxDB.createDatabase(database);
			}
		} else {
			log.warn("Could not connect to InfluxDB, any logs will be ignored!");
			available = false;
		}
	}
}
