package com.marocgeo.als.business;

import java.util.List;
import java.util.Map;

import com.marocgeo.als.models.Commandeview;
import com.marocgeo.als.models.Compte;
import com.marocgeo.als.models.Produit;
import com.marocgeo.als.models.Remises;

public interface CommandeManager {

	public List<Commandeview> charger_commandes(Compte c);
	public String insertCommande(List<Produit> prd, String idclt,  Compte compte, Map<String, Remises> allremises);
	public String CmdToFacture(Commandeview cv,Compte cp);
}
