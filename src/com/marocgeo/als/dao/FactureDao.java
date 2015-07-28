package com.marocgeo.als.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.marocgeo.als.models.Compte;
import com.marocgeo.als.models.ConfigGps;
import com.marocgeo.als.models.FactureGps;
import com.marocgeo.als.models.FileData;
import com.marocgeo.als.models.Produit;
import com.marocgeo.als.models.Prospection;
import com.marocgeo.als.models.Reglement;
import com.marocgeo.als.models.Remises;


public interface FactureDao {
	
	public FileData insert(List<Produit> prd,String idclt,int nmb,String commentaire,Compte compte,String reglement , String amount,String numChek ,int typeImpriment, Map<String, Remises> allremises,int type_invoice);
	public FileData insertoff(Prospection pros, List<Produit> prd,String idclt,int nmb,String commentaire,Compte compte,String reglement , String amount,String numChek ,int typeImpriment, Map<String, Remises> allremises,HashMap<Integer, Reglement> hstmp);
	public List<FactureGps> listFacture(Compte c);
	
	public String insertoffline(Prospection ps,List<Produit> prd, String idclt, int nmb,
			String commentaire, Compte compte, String reglement, String amount,
			String numChek, int typeImpriment, Map<String, Remises> allremises,
			HashMap<Integer, Reglement> reg);
	
	public String insertcicin(List<Produit> prd, String idclt, int nmb,
			String commentaire, Compte compte, String reglement , String amount , String numChek ,int typeImpriment,Map<String, Remises> allremises,HashMap<Integer, Reglement> lsreg,String rs,int type_invoice);
}
