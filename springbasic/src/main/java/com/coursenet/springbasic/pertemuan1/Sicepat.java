package com.coursenet.springbasic.pertemuan1;

import org.springframework.stereotype.Component;

@Component
public class Sicepat extends Shipper implements ShipperInterface {
	//constructor
		public Sicepat() {
			name = "Sicepat";
		}
		
		
		public void sendPackage(Order order) {
			System.out.println("Paket " + order.getGoodsName()
			+ " telah dikirimkan oleh " + name);
		}
}
