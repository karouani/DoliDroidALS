package com.marocgeo.als.utils;

import javax.net.ssl.ManagerFactoryParameters;

import com.marocgeo.als.business.CommandeManager;
import com.marocgeo.als.business.DefaultCommandeManager;
import com.marocgeo.als.dao.CommandeDao;
import com.marocgeo.als.dao.CommandeDaoMysql;

public class CommandeManagerFactory {

	private static CommandeManager manager;
	
	static{
		CommandeDao dao = new CommandeDaoMysql();
		manager = new DefaultCommandeManager(dao);
	}

	public static CommandeManager getManager() {
		return manager;
	}

	
}
