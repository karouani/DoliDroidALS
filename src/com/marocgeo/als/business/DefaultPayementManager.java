package com.marocgeo.als.business;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.marocgeo.als.dao.PayementDao;
import com.marocgeo.als.models.Compte;
import com.marocgeo.als.models.Payement;
import com.marocgeo.als.models.Reglement;
import com.marocgeo.als.utils.JSONParser;
import com.marocgeo.als.utils.URL;

public class DefaultPayementManager implements PayementManager{
	
	private PayementDao dao;
	
	public DefaultPayementManager() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	public DefaultPayementManager(PayementDao dao) {
		super();
		this.dao = dao;
	}


	public void setDao(PayementDao dao) {
		this.dao = dao;
	}


	@Override
	public List<Payement> getFactures(Compte c) {
		// TODO Auto-generated method stub
		return dao.getFactures(c);
	}

	@Override
	public String insertPayement(Reglement reg, Compte c) {
		// TODO Auto-generated method stub
		return dao.insertPayement(reg, c);
	}

	
	
}
