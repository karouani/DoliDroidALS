package com.marocgeo.als.utils;

import com.marocgeo.als.business.CommercialManager;
import com.marocgeo.als.business.DefaultCommercialManager;
import com.marocgeo.als.business.DefaultVendeurManager;
import com.marocgeo.als.business.VendeurManager;
import com.marocgeo.als.dao.CommercialDao;
import com.marocgeo.als.dao.CommercialDaoMysql;
import com.marocgeo.als.dao.VendeurDao;
import com.marocgeo.als.dao.VendeurDaoMysql;


public class CommercialManagerFactory {

	private static CommercialManager commercial;
	

	
	static{
		CommercialDao dao = new CommercialDaoMysql();
		commercial = new DefaultCommercialManager(dao);
	}

	public static CommercialManager getCommercialManager() {
		return commercial;
	}
}
