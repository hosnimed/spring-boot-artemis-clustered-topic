package com.sbm.artemis.demo.models;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
 
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
 
@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class,property="@id", scope = Company.class)
public class Company {
    private String name;
 
    private List<Product> products;
	
    public Company(){
    }
    
    public Company(String name, List<Product> products){
    	this.name = name;
    	this.products = products;
    }
    
    // name
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    // products
    public void setProducts(List<Product> products){
    	this.products = products;
    }
    
    public List<Product> getProducts(){
    	return this.products;
    }
 
	/**
	 * 
	 * Show Detail View
	 */
	public String toString(){
		JSONObject jsonInfo = new JSONObject();
		
		try {
			jsonInfo.put("name", this.name);
 
			JSONArray productArray = new JSONArray();
			if (this.products != null) {
				this.products.forEach(product -> {
					JSONObject subJson = new JSONObject();
					try {
						subJson.put("name", product.getName());
					} catch (JSONException e) {}
					
					productArray.put(subJson);
				});
			}
			jsonInfo.put("products", productArray);
		} catch (JSONException e1) {}
		return jsonInfo.toString();
	}
 
}