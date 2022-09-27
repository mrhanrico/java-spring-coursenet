package com.coursenet.springbasic.util;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.coursenet.springbasic.pertemuan1.JNE;
import com.coursenet.springbasic.pertemuan1.ShipperInterface;
import com.coursenet.springbasic.pertemuan1.Sicepat;

@Component
public class ShipperMapperUtil {
	@Autowired
	private JNE jne;
	
	@Autowired
	private Sicepat sicepat;
	
	private Map<String, ShipperInterface> shipperMap;
	
	public ShipperInterface getShipper(String shipperName) {
		if (shipperMap == null) {
			shipperMap = new HashMap<>();
			shipperMap.put("JNE", jne);
			shipperMap.put("Sicepat", sicepat);
		}
		
		
		return shipperMap.get(shipperName);
	}
	
}
