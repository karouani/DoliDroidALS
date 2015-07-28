package com.marocgeo.als.dao;

import com.marocgeo.als.models.Compte;
import com.marocgeo.als.models.ProspectData;
import com.marocgeo.als.models.Prospection;

public interface CommercialDao {
	public String insert(Compte c,Prospection prospect);
	public ProspectData getInfos(Compte c);
}
