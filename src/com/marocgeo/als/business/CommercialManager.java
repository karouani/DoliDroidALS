package com.marocgeo.als.business;

import com.marocgeo.als.models.Compte;
import com.marocgeo.als.models.ProspectData;
import com.marocgeo.als.models.Prospection;

public interface CommercialManager {
	public String insert(Compte c,Prospection prospect);
	public ProspectData getInfos(Compte c);
}
