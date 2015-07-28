package com.marocgeo.als.utils;

import com.marocgeo.als.business.DefaultFactureManager;
import com.marocgeo.als.business.DefaultPayementManager;
import com.marocgeo.als.business.FactureManager;
import com.marocgeo.als.business.PayementManager;
import com.marocgeo.als.dao.FactureDao;
import com.marocgeo.als.dao.FactureDaoMysql;
import com.marocgeo.als.dao.PayementDao;
import com.marocgeo.als.dao.PayementDaoMysql;



public class PayementManagerFactory {

	private static PayementManager payementFactory;
	

	
	static{
		PayementDao dao = new PayementDaoMysql();
		payementFactory = new DefaultPayementManager(dao);
	}



	public static PayementManager getPayementFactory() {
		return payementFactory;
	}
	
}

