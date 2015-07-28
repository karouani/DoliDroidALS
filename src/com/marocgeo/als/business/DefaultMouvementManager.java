package com.marocgeo.als.business;

import java.util.List;

import com.marocgeo.als.dao.MouvementDao;
import com.marocgeo.als.dao.VendeurDao;
import com.marocgeo.als.models.Compte;
import com.marocgeo.als.models.LoadStock;
import com.marocgeo.als.models.Mouvement;

public class DefaultMouvementManager implements MouvementManager {

	private MouvementDao dao;
	
	public DefaultMouvementManager(MouvementDao dao) {
		super();
		this.dao = dao;
	}
	
	@Override
	public LoadStock laodStock(Compte cp) {
		// TODO Auto-generated method stub
		return dao.laodStock(cp);
	}

	@Override
	public String makemouvement(List<Mouvement> mvs, Compte cp, String label) {
		// TODO Auto-generated method stub
		return dao.makemouvement(mvs, cp, label);
	}

	@Override
	public String makeechange(List<Mouvement> mvs, Compte cp, String label,String clt) {
		// TODO Auto-generated method stub
		return dao.makeechange(mvs, cp, label,clt);
	}

}
