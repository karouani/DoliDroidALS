package com.marocgeo.als.utils;

import com.marocgeo.als.business.DefaultVendeurManager;
import com.marocgeo.als.business.VendeurManager;
import com.marocgeo.als.dao.VendeurDao;
import com.marocgeo.als.dao.VendeurDaoMysql;


public class VendeurManagerFactory {

	private static VendeurManager vendeurManager;
	

	
	static{
		VendeurDao dao = new VendeurDaoMysql();
		vendeurManager = new DefaultVendeurManager(dao);
	}

	public static VendeurManager getClientManager() {
		return vendeurManager;
	}
}
