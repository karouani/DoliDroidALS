package com.marocgeo.als.dao;

import java.util.List;
import java.util.Map;

import com.marocgeo.als.models.Categorie;
import com.marocgeo.als.models.Compte;
import com.marocgeo.als.models.Produit;
import com.marocgeo.als.models.Remises;

public interface CategorieDao {

	public List<Categorie> LoadCategories(Compte cp);
	
}
