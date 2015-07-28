package com.marocgeo.als.utils;

import com.marocgeo.als.business.DefaultTechnicienManager;
import com.marocgeo.als.business.TechnicienManager;
import com.marocgeo.als.dao.TechnicienDao;
import com.marocgeo.als.dao.TechnicienDaoMysql;




public class TechnicienManagerFactory {

	private static TechnicienManager technicienManager;
	

	
	static{
		TechnicienDao dao = new TechnicienDaoMysql();
		technicienManager = new DefaultTechnicienManager(dao);
	}

	public static TechnicienManager getClientManager() {
		return technicienManager;
	}
}
