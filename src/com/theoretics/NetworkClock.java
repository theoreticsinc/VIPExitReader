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
                    String serverTime = "";
                    SystemStatus ss = new SystemStatus();
                    boolean online = ss.checkPING(CONSTANTS.serverIP);//LINUX USE ONLY - also check your root password
                    if (cards.isEmpty() == false) {
                        String cardFromReader = cards.get(0);
                        if (online == true) {
                            System.out.println("ONLINE");
                            System.out.print("`/");
                            
                            //SAVE Card to DATABASE
                            boolean isInserted = false;
                            boolean isUpdated = false;
                            serverTime = dbh.getServerTime();
                            System.out.println("Time On Card*" + cardFromReader + "* :: " + serverTime);
                            boolean isRecordedVIP = dbh.deleteVIP_DTR(cardFromReader);
                            if (isRecordedVIP) {
                                //isUpdated = dbh.updateVIPEntryRecordWPix(cardFromReader, CONSTANTS.entranceID);
                                System.out.println(cardFromReader + "isUpdated" + isUpdated);
                                System.out.println(cardFromReader + "isUpdated" + isUpdated);
                                cards.remove(0);
                            } else {
                                isInserted = dbh.writeVIPEntryWithPix(CONSTANTS.entranceID, cardFromReader, "V", "");
                                System.out.println(cardFromReader + " isInserted:" + isInserted);
                                System.out.println(cardFromReader + " isInserted:" + isInserted);
                                cards.remove(0);
                            }

                        } else if (online == false) {
                            System.out.println("OFFLINE");
                            System.out.print("-");
                        }
                        Thread.sleep(100);
                        ss.updateTimeOnChip(serverTime);
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
