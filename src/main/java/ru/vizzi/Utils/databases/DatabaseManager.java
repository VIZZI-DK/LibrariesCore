package ru.vizzi.Utils.databases;

import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.github.shyiko.mysql.binlog.event.*;
import lombok.Getter;
import lombok.Setter;
import ru.vizzi.Utils.LibrariesCore;
import ru.vizzi.Utils.databases.mariadb.ConfigDBType;
import ru.vizzi.Utils.databases.mariadb.ConfigMariaList;
import ru.vizzi.Utils.databases.mariadb.ConfigReplicatorType;
import ru.vizzi.Utils.databases.mariadb.MariaDBProvider;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class DatabaseManager {

	@Getter
	private static ExecutorService executorService;
	private static ConfigMariaList configMariaDBV2;
	private static ArrayList<MariaDBProvider> dbProviders = new ArrayList<>();

	private static ArrayList<BinaryLogClient> replicationProviders = new ArrayList<>() ;

	public static ArrayList<ReplicationEvent> replicationEvents = new ArrayList<>();;

	@Getter
	private static boolean init;


	public static void shutdown() {
		executorService.shutdown();
//		for(MariaDBProvider db: dbProviders){
//		//	db.shutdown();
//		}
		init = false;
		try {
			executorService.awaitTermination(10000, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	
//
	}

	private static ArrayList<ReplicationEvent> getReplicationEvent(long id) {
		synchronized (replicationEvents) {
			return replicationEvents.stream()
					.filter(e -> e.getServerID() == id)
					.collect(Collectors.toCollection(ArrayList::new));
		}
	}

	public static MariaDBProvider getMysql(int id){
		return dbProviders.get(id);
	}

	public static BinaryLogClient getReplication(int id){
		if(id>=0 && id<replicationProviders.size()){
			return replicationProviders.get(id);
		}
		return null;
	}
	static Map<Long, String> tableMap = new HashMap<>();

	public static void init(){
		if(!init){
			configMariaDBV2 = new ConfigMariaList("config/"+ LibrariesCore.MODID + "/mariaConfig.json");
			ArrayList<ConfigDBType> configMaria = new ArrayList<>();
			ConfigDBType configDBType = new ConfigDBType();
			configDBType.setDatabase("databasesName");
			configDBType.setHost("localhost");
			configDBType.setPort(3306);
			configDBType.setUsername("root");
			configDBType.setPassword("password");
			configMaria.add(configDBType);
			configMariaDBV2.setConfigMariaDBS(configMaria);
			configMariaDBV2.load();
			if(configMariaDBV2.isEnabled()) {
				executorService = Executors.newFixedThreadPool(16);
				for (ConfigDBType dbType : configMariaDBV2.getConfigMariaDBS()) {
					MariaDBProvider mariaDBProviderV2 = new MariaDBProvider(dbType);
					//mariaDBProviderV2.init();
					dbProviders.add(mariaDBProviderV2);
				}
				for (ConfigReplicatorType dbType : configMariaDBV2.getReplicationDatabase()) {
					BinaryLogClient reClient = new BinaryLogClient(dbType.getHost(), dbType.getPort(), dbType.getUsername(), dbType.getPassword());
					reClient.setServerId(dbType.getReplicationID());
					new Thread(() -> {
						try {

							reClient.registerEventListener(event -> {
								EventData data = event.getData();
								if (data instanceof TableMapEventData) {
									TableMapEventData tableMapData = (TableMapEventData) data;
									long tableId = tableMapData.getTableId();
									String databases = tableMapData.getDatabase();
									String fullName = tableMapData.getTable();
									tableMap.put(tableId, databases+":"+fullName);
								} else if (data instanceof WriteRowsEventData) {
									WriteRowsEventData writeData = (WriteRowsEventData) data;
									String force = tableMap.get(writeData.getTableId());
									String[] strim = force.split(":");

									ArrayList<ReplicationEvent> replicationEvent = getReplicationEvent(event.getHeader().getServerId());
									for(ReplicationEvent eventR : replicationEvent){
										eventR.writeEvent(strim[0], strim[1], writeData);
									}

								} else if (data instanceof UpdateRowsEventData) {
									UpdateRowsEventData updateData = (UpdateRowsEventData) data;
									String force = tableMap.get(updateData.getTableId());
									String[] strim = force.split(":");
									ArrayList<ReplicationEvent> replicationEvent = getReplicationEvent(event.getHeader().getServerId());
									for(ReplicationEvent eventR : replicationEvent){
										eventR.updateEvent(strim[0], strim[1], updateData);
									}

								} else if (data instanceof DeleteRowsEventData) {
									DeleteRowsEventData deleteData = (DeleteRowsEventData) data;
									String force = tableMap.get(deleteData.getTableId());
									String[] strim = force.split(":");


									ArrayList<ReplicationEvent> replicationEvent = getReplicationEvent(event.getHeader().getServerId());
									for(ReplicationEvent eventR : replicationEvent){
										eventR.deleteEvent(strim[0], strim[1], deleteData);
									}
								}
							});


							reClient.connect();
							//	System.out.println("Connected to " + dbType.getHost() + ":" + dbType.getPort() + " as " + dbType.getUsername() + "as "+ dbType.getReplicationID());
						} catch (Exception e) {
							e.printStackTrace();
						}
					}, "BinlogClient-" + dbType.getUsername()).start();
					//mariaDBProviderV2.init();
					replicationProviders.add(reClient);
				}
				init = true;
			}
		}
	}

}
