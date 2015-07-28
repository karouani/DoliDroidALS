package com.marocgeo.als.dao;

import java.util.HashMap;
import java.util.List;

import com.marocgeo.als.models.Client;
import com.marocgeo.als.models.Compte;
import com.marocgeo.als.models.Dictionnaire;
import com.marocgeo.als.models.Facture;
import com.marocgeo.als.models.Produit;
import com.marocgeo.als.models.Promotion;

public interface VendeurDao {
	
	public int insertFacture(Facture fac);
	
	public List<Produit> selectAllProduct(Compte c);
	public Produit selectProduct(String id,Compte c);
	public Dictionnaire getDictionnaire();
	
	public List<Client> selectAllClient(Compte c);
	public List<Promotion> getPromotions(int idclt,int idprd);
	
	public HashMap<Integer, HashMap<Integer, Promotion>> getPromotionProduits();
	public HashMap<Integer, List<Integer>> getPromotionClients();
}
