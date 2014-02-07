package com.boss.puertos;
import java.util.Enumeration;
import gnu.io.*;
public class puertos {
@SuppressWarnings("restriction")
CommPortIdentifier ports;
	
public void mirar(){
	Enumeration<?> puertos=CommPortIdentifier.getPortIdentifiers();
	while(puertos.hasMoreElements()){
	ports=(CommPortIdentifier)puertos.nextElement();
	System.out.println("***\n"+ports.getName());
	
	
	}
}
	
	
	
	
	
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		puertos ini=new puertos();
		ini.mirar();
	}

}
