package com.marocgeo.als.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.marocgeo.als.models.Compte;
import com.marocgeo.als.models.ProspectData;
import com.marocgeo.als.models.Prospection;
import com.marocgeo.als.utils.JSONParser;
import com.marocgeo.als.utils.URL;

public class CommercialDaoMysql implements CommercialDao{

	private String urlData = URL.URL+"prospection.php";
	private JSONParser parser ;

	public CommercialDaoMysql() {
		// TODO Auto-generated constructor stub
		parser = new JSONParser();
	}

	@Override
	public String insert(Compte c,Prospection p) {
		//Log.e("Appel INSERTION", p.toString());
		
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		
		nameValuePairs.add(new BasicNameValuePair("username",c.getLogin()));
		nameValuePairs.add(new BasicNameValuePair("password",c.getPassword()));
		nameValuePairs.add(new BasicNameValuePair("create","create"));
		
		if (p.getParticulier() == 1) {
			nameValuePairs.add(new BasicNameValuePair("nom",p.getFirstname()));
		}else{
			nameValuePairs.add(new BasicNameValuePair("nom",p.getName()));
		}
		
        nameValuePairs.add(new BasicNameValuePair("firstname",p.getLastname()));
        nameValuePairs.add(new BasicNameValuePair("particulier",p.getParticulier()+""));
        nameValuePairs.add(new BasicNameValuePair("client",p.getClient()+""));
        nameValuePairs.add(new BasicNameValuePair("address",p.getAddress()));
        nameValuePairs.add(new BasicNameValuePair("zip",p.getZip()));
        nameValuePairs.add(new BasicNameValuePair("town",p.getTown()));
        nameValuePairs.add(new BasicNameValuePair("phone",p.getPhone()));
        nameValuePairs.add(new BasicNameValuePair("fax",p.getFax()));
        nameValuePairs.add(new BasicNameValuePair("email",p.getEmail()));
        nameValuePairs.add(new BasicNameValuePair("capital",p.getCapital()));
        nameValuePairs.add(new BasicNameValuePair("idprof1",p.getIdprof1()));
        nameValuePairs.add(new BasicNameValuePair("idprof2",p.getIdprof2()));
        nameValuePairs.add(new BasicNameValuePair("idprof3",p.getIdprof3()));
        nameValuePairs.add(new BasicNameValuePair("idprof4",p.getIdprof4()));
        nameValuePairs.add(new BasicNameValuePair("typent_id",p.getTypent_id()));
        nameValuePairs.add(new BasicNameValuePair("effectif_id",p.getEffectif_id()));
        nameValuePairs.add(new BasicNameValuePair("assujtva_value",p.getTva_assuj()+""));
        nameValuePairs.add(new BasicNameValuePair("status",p.getStatus()+""));
		nameValuePairs.add(new BasicNameValuePair("commercial_id",c.getIduser()));
        nameValuePairs.add(new BasicNameValuePair("country_id",p.getCountry_id()+""));
        nameValuePairs.add(new BasicNameValuePair("forme_juridique_code",p.getForme_juridique_code()));
		
        nameValuePairs.add(new BasicNameValuePair("latitude",p.getLatitude()+""));
        nameValuePairs.add(new BasicNameValuePair("longitude",p.getLangitude()+""));
		
		
		String retour = "-1";
		
		try {
			String json = parser.makeHttpRequest(urlData, "POST", nameValuePairs);
			String stfomat = json.substring(json.indexOf("{"),json.lastIndexOf("}")+1);
			
			Log.e("Insertion Message old", json);
			
			JSONObject obj = new JSONObject(stfomat);
			if(obj.has("message")){
				JSONArray arr = obj.getJSONArray("message");
				int k =arr.length() - 1;
				//retour = arr.getString(k);
			}
			//$message['client']
			retour = obj.getString("client");
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			retour ="-1";
			Log.e("json insert prospect",e.getMessage() +" << ");
		}
		
		Log.e("Retour >> 00",retour);
		return retour;
	}

	@Override
	public ProspectData getInfos(Compte c) {
		
		ProspectData data = new ProspectData();
		
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				
		nameValuePairs.add(new BasicNameValuePair("username",c.getLogin()));
		nameValuePairs.add(new BasicNameValuePair("password",c.getPassword()));
		
		String json = parser.makeHttpRequest(urlData, "POST", nameValuePairs);
		
		
		/*{
			"town":[
			        {"ville":"CASABLANCA"},{"ville":"RABAT"}],
			"formJuridique":[
			                 {"code":"2121","nom":"Soci\u00e9t\u00e9 A R\u00e9sponsabilit\u00e9 Limit\u00e9e"}]
	    }*/
		
		Log.e("RepondreMoi json", json);
		try {
			String stfomat = json.substring(json.indexOf("{"),json.lastIndexOf("}")+1);
			Log.e("RepondreMoi cc", stfomat);
			JSONObject obj = new JSONObject(stfomat);
			JSONArray jarrayTown = obj.getJSONArray("town");
			JSONArray jarrayForm = obj.getJSONArray("formJuridique");
			JSONArray jarrayType = obj.getJSONArray("typent");
			JSONArray jarrayreqfields = obj.getJSONArray("requiredfields");
			
			List<String> list = new ArrayList<>();
			
			List<String> juridique = new ArrayList<>();
			List<String> typent = new ArrayList<>();
			List<String> reqfield = new ArrayList<>();
			
			HashMap<String, String> juridique_code= new HashMap<>();
			HashMap<String,String> typent_code= new HashMap<>();
			HashMap<String,String> typent_id= new HashMap<>();
		
			
			for (int i = 0; i < jarrayTown.length(); i++) {
				if(!"".equals(jarrayTown.getJSONObject(i).getString("ville")) || !"null".equals(jarrayTown.getJSONObject(i).getString("ville") )){
					list.add(jarrayTown.getJSONObject(i).getString("ville"));
				}
			}
			
			for (int i = 0; i < jarrayForm.length(); i++) {
				juridique.add(jarrayForm.getJSONObject(i).getString("nom"));
				juridique_code.put(jarrayForm.getJSONObject(i).getString("nom"), jarrayForm.getJSONObject(i).getString("code"));
			}
			
			for (int i = 0; i < jarrayType.length(); i++) {
				typent.add(jarrayType.getJSONObject(i).getString("labelle"));
				typent_code.put(jarrayType.getJSONObject(i).getString("labelle"), jarrayType.getJSONObject(i).getString("code"));
				typent_id.put(jarrayType.getJSONObject(i).getString("labelle"), jarrayType.getJSONObject(i).getString("id"));
			}
			
			for (int i = 0; i < jarrayreqfields.length(); i++) {
				reqfield.add(jarrayreqfields.getJSONObject(i).getString("libelle"));
			}
			
			data.setJuridique(juridique);
			data.setVilles(list);
			data.setTypent(typent);
			data.setJuridique_code(juridique_code);
			data.setTypent_code(typent_code);
			data.setTypent_id(typent_id);
			data.setLsrequired(reqfield);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return data;
	}

}
