package com.marocgeo.als.utils;

import com.marocgeo.als.business.DefaultFactureManager;
import com.marocgeo.als.business.FactureManager;
import com.marocgeo.als.dao.FactureDao;
import com.marocgeo.als.dao.FactureDaoMysql;



public class FactureManagerFactory {

	private static FactureManager factureManager;
	

	
	static{
		FactureDao dao = new FactureDaoMysql();
		factureManager = new DefaultFactureManager(dao);
	}

	public static FactureManager getFactureManager() {
		return factureManager;
	}
}

