package com.marocgeo.als.business;

import java.util.List;

import com.marocgeo.als.dao.TechnicienDao;
import com.marocgeo.als.models.BordereauGps;
import com.marocgeo.als.models.BordreauIntervention;
import com.marocgeo.als.models.Client;
import com.marocgeo.als.models.Compte;
import com.marocgeo.als.models.ImageTechnicien;
import com.marocgeo.als.models.Services;


public class DefaultTechnicienManager implements TechnicienManager {

	private TechnicienDao dao;
	
	public DefaultTechnicienManager() {
	}

	
	public DefaultTechnicienManager(TechnicienDao dao) {
		super();
		this.dao = dao;
	}

	public TechnicienDao getDao() {
		return dao;
	}


	public void setDao(TechnicienDao dao) {
		this.dao = dao;
	}


	@Override
	public String insertBordereau(BordreauIntervention bi, Compte c) {
		return dao.insertBordereau(bi, c);
	}


	@Override
	public List<BordereauGps> selectAllBordereau(Compte c) {
		return dao.selectAllBordereau(c);
	}


	@Override
	public List<Services> allServices(Compte c) {
		return dao.allServices(c);
	}


	@Override
	public void inesrtImage(List<ImageTechnicien> imgs,String lien) {
		dao.inesrtImage(imgs,lien);
	}


	@Override
	public List<Client> selectAllClient(Compte c) {
		// TODO Auto-generated method stub
		return dao.selectAllClient(c);
	}


	@Override
	public String insertBordereauoff(BordreauIntervention bi, Compte c) {
		// TODO Auto-generated method stub
		return dao.insertBordereauoff(bi, c);
	}



}
