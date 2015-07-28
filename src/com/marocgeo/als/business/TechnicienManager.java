package com.marocgeo.als.business;

import java.util.List;

import com.marocgeo.als.models.BordereauGps;
import com.marocgeo.als.models.BordreauIntervention;
import com.marocgeo.als.models.Client;
import com.marocgeo.als.models.Compte;
import com.marocgeo.als.models.ImageTechnicien;
import com.marocgeo.als.models.Services;

public interface TechnicienManager {
	
	public String insertBordereau(BordreauIntervention bi,Compte c);
	public String insertBordereauoff(BordreauIntervention bi, Compte c);
	public List<BordereauGps> selectAllBordereau(Compte c);
	
	public List<Services> allServices(Compte c);
	public void inesrtImage(List<ImageTechnicien> imgs,String lien);

	public List<Client> selectAllClient(Compte c);
}
