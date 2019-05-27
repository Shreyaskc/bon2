package com;


public class TestMain2 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String a = "UPC_Depts by Top Parent";
		if (a.toLowerCase().matches("upc(.*)")) {
			System.out.println("Yes");
		}
	}
	
	
}
