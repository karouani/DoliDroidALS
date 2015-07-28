package com.marocgeo.als.dao;

import com.marocgeo.als.models.Compte;
import com.marocgeo.als.models.ConfigGps;
import com.marocgeo.als.models.MyTicketWitouhtProduct;
import com.marocgeo.als.models.Services;


public interface ConnexionDao {
	
	public Compte login(String login,String password);
	public ConfigGps getGpsConfig();
	public Services getService(String login, String password);
	public MyTicketWitouhtProduct lodSociete(String st);
}
