package com.marocgeo.als.utils;

import javax.net.ssl.ManagerFactoryParameters;

import com.marocgeo.als.business.DefaultMouvementManager;
import com.marocgeo.als.business.MouvementManager;
import com.marocgeo.als.dao.MouvementDao;
import com.marocgeo.als.dao.MouvementDaoMysql;

public class MouvementManagerFactory {

	private static MouvementManager manager;
	
	static{
		MouvementDao dao = new MouvementDaoMysql();
		manager = new DefaultMouvementManager(dao);
	}

	public static MouvementManager getManager() {
		return manager;
	}

	public static void setManager(MouvementManager manager) {
		MouvementManagerFactory.manager = manager;
	}
}
