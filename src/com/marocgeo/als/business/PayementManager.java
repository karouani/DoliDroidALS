package com.marocgeo.als.business;

import java.util.List;

import com.marocgeo.als.models.Compte;
import com.marocgeo.als.models.Payement;
import com.marocgeo.als.models.Reglement;

public interface PayementManager {
	public List<Payement> getFactures(Compte c);
	public String insertPayement(Reglement reg,Compte c);
}
