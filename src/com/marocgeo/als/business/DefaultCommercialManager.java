package com.marocgeo.als.business;

import com.marocgeo.als.dao.CommercialDao;
import com.marocgeo.als.models.Compte;
import com.marocgeo.als.models.ProspectData;
import com.marocgeo.als.models.Prospection;

public class DefaultCommercialManager implements CommercialManager {

	private CommercialDao dao;

	public DefaultCommercialManager(CommercialDao dao) {
		super();
		this.dao = dao;
	}
	
	@Override
	public String insert(Compte c,Prospection prospect) {
		// TODO Auto-generated method stub
		return dao.insert(c,prospect);
	}
	


	@Override
	public ProspectData getInfos(Compte c) {
		// TODO Auto-generated method stub
		return dao.getInfos(c);
	}

}
