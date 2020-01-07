/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.theoretics;

import com.theoretics.DataBaseHandler;
import com.theoretics.SystemStatus;
import java.util.ArrayList;

/**
 *
 * @author Theoretics Inc
 */
public class NetworkClock implements Runnable {
    
    ArrayList<String> cards;
    //String serverIP = "192.168.1.10";
    DataBaseHandler dbh = new DataBaseHandler(CONSTANTS.serverIP);
    //String entranceID = "Entry Zone 2";

    public NetworkClock(ArrayList<String> cards) {
        this.cards = cards; 
    }
    
        @Override
        public void run() {

            try {
                while (true) {
                    SystemStatus ss = new SystemStatus();
                    boolean online = ss.checkPING(CONSTANTS.serverIP);//LINUX USE ONLY - also check your root password
                    if (cards.isEmpty() == false) {
                        String cardFromReader = cards.get(0);
                        if (online == true) {
                            System.out.println("ONLINE");
                            System.out.print("`/");
                            
                            //SAVE Card to DATABASE
                            boolean isValid = false;
                            boolean isUpdated = false;
                            String serverTime = dbh.getServerTime();
                            System.out.println("Time On Card*" + cardFromReader + "* :: " + serverTime);
                            boolean alreadyExists = dbh.findCGHCard(cardFromReader);
                            if (alreadyExists) {
                                isUpdated = dbh.updateEntryRecordWPix(cardFromReader, CONSTANTS.entranceID);
                                System.out.println(cardFromReader + "isUpdated" + isUpdated);
                                System.out.println(cardFromReader + "isUpdated" + isUpdated);
                                cards.remove(0);
                            } else {
                                isValid = dbh.writeCGHEntryWithPix(CONSTANTS.entranceID, cardFromReader, "R", "");
                                System.out.println(cardFromReader + " isValid:" + isValid);
                                System.out.println(cardFromReader + " isValid:" + isValid);
                                cards.remove(0);
                            }

                        } else if (online == false) {
                            System.out.println("OFFLINE");
                            System.out.print("-");
                        }
                        Thread.sleep(100);
                        System.out.println("NETWORK");
                        //resetAdmin();
                        //Thread.sleep(2000);
                    }
                    System.out.println(".");
                }
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }

    }
