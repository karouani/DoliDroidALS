package com.marocgeo.als.dao;

import java.util.List;

import com.marocgeo.als.models.Compte;
import com.marocgeo.als.models.LoadStock;
import com.marocgeo.als.models.Mouvement;
import com.marocgeo.als.models.Produit;

public interface MouvementDao {

	public LoadStock laodStock(Compte cp);
	public String makemouvement(List<Mouvement> mvs,Compte cp,String label);
	public String makeechange(List<Mouvement> mvs,Compte cp,String label,String clt);
}
