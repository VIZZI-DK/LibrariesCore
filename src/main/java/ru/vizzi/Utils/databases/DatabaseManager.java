package ru.vizzi.Utils.databases;

import lombok.Getter;
import lombok.Setter;
import ru.vizzi.Utils.LibrariesCore;
import ru.vizzi.Utils.databases.mariadb.ConfigDBType;
import ru.vizzi.Utils.databases.mariadb.ConfigMariaList;
import ru.vizzi.Utils.databases.mariadb.MariaDBProvider;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class DatabaseManager {

	@Getter
	private static ExecutorService executorService;
	private static ConfigMariaList configMariaDBV2;
	private static ArrayList<MariaDBProvider> dbProviders = new ArrayList<>();

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

	public static MariaDBProvider getMysql(int id){
		return dbProviders.get(id);
	}


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
				init = true;
			}
		}
	}

}
