package com.marocgeo.als.utils;

import com.marocgeo.als.business.AuthentificationManager;
import com.marocgeo.als.business.DefaultAuthentificationManager;
import com.marocgeo.als.business.DefaultTechnicienManager;
import com.marocgeo.als.business.TechnicienManager;
import com.marocgeo.als.dao.ConnexionDao;
import com.marocgeo.als.dao.ConnexionDaoMysql;
import com.marocgeo.als.dao.TechnicienDao;
import com.marocgeo.als.dao.TechnicienDaoMysql;




public class ConnexionManagerFactory {

	private  static AuthentificationManager auth;
	private  static ConnexionDao connect;

	
	static{
		connect = new ConnexionDaoMysql();
		auth = new DefaultAuthentificationManager(connect);
	}

	public static AuthentificationManager getCConnexionManager() {
		return auth;
	}
}
