package com.marocgeo.als.business;

import java.util.List;

import com.marocgeo.als.models.Compte;
import com.marocgeo.als.models.LoadStock;
import com.marocgeo.als.models.Mouvement;

public interface MouvementManager {
	public LoadStock laodStock(Compte cp);
	public String makemouvement(List<Mouvement> mvs,Compte cp,String label);
	public String makeechange(List<Mouvement> mvs, Compte cp, String label,String clt);
}
