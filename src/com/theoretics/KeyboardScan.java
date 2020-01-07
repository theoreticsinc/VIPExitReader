/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.theoretics;

import java.util.Scanner;
import java.math.BigInteger;


/**
 *
 * @author Theoretics
 */
public class KeyboardScan {
    
    static String bin2hex(byte[] data) {
	    return String.format("%0" + (data.length * 2) + "X", new BigInteger(1,data));
	}
    
    public static void main(String[] args) {

     Scanner scan= new Scanner(System.in);

     String text = null;
     String cardUID = null;
     while(true){
         text = scan.nextLine();
         if (null != text) {
             cardUID = Integer.toHexString(Integer.parseInt(text));
             cardUID = cardUID.toUpperCase();
             System.out.println("UID: " + cardUID.substring(6, 8) + cardUID.substring(4, 6) + cardUID.substring(2, 4) + cardUID.substring(0, 2));
         }
     }
     
    }
}
