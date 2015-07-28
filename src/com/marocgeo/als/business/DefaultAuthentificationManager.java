package com.marocgeo.als.business;

import com.marocgeo.als.dao.ConnexionDao;
import com.marocgeo.als.models.Compte;
import com.marocgeo.als.models.ConfigGps;
import com.marocgeo.als.models.MyTicketWitouhtProduct;
import com.marocgeo.als.models.Services;

public class DefaultAuthentificationManager implements AuthentificationManager {

	private ConnexionDao dao;
	
	public DefaultAuthentificationManager() {
	}
	
	public DefaultAuthentificationManager(ConnexionDao dao) {
		super();
		this.dao = dao;
	}

	public ConnexionDao getDao() {
		return dao;
	}

	public void setDao(ConnexionDao dao) {
		this.dao = dao;
	}

	@Override
	public Compte login(String login, String password) {
		return dao.login(login, password);
	}

	@Override
	public ConfigGps getGpsConfig() {
		return dao.getGpsConfig();
	}

	@Override
	public Services getService(String login, String password) {
		return dao.getService(login, password);
	}

	@Override
	public MyTicketWitouhtProduct lodSociete(String st) {
		// TODO Auto-generated method stub
		return dao.lodSociete(st);
	}


}
