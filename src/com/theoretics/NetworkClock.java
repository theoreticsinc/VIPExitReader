/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.theoretics;

import com.theoretics.DataBaseHandler;
import com.theoretics.SystemStatus;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Theoretics Inc
 */
public class NetworkClock extends Thread {

    ArrayList<String> cards;
    //String serverIP = "192.168.1.10";
    DataBaseHandler dbh = new DataBaseHandler(CONSTANTS.serverIP);
    static Logger log = LogManager.getLogger(NetworkClock.class.getName());
    //String entranceID = "Entry Zone 2";
    SystemStatus ss = new SystemStatus();
    String serverTime = "";

    public NetworkClock(ArrayList<String> cards) {
        this.cards = cards;
    }

    private void comms2POS(String messageOut) {

        //System.out.println( "Loading contents of URL: " + POSserver );
        try {
            // Connect to the server
            Socket socket = new Socket(CONSTANTS.POSserver, CONSTANTS.port);

            // Create input and output streams to read from and write to the server
            PrintStream out = new PrintStream(socket.getOutputStream());
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Follow the HTTP protocol of GET <path> HTTP/1.0 followed by an empty line
            out.println(messageOut);
            out.println();

            // Read data from the server until we finish reading the document
            String line = in.readLine();
            while (line != null && in != null) {
                            System.out.println(line);
                line = in.readLine();
            }
            // Close our streams
            in.close();
            out.close();
            socket.close();
        } catch (Exception e) {
            System.out.println("No POS Server available to receive messages");
//            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (true) {
            try {                
                //cards.add("ABCD123");
                if (cards.isEmpty() == false) {
                    String cardFromReader = cards.get(0);
                    boolean online = true;//ss.checkPING(CONSTANTS.serverIP);//LINUX USE ONLY - also check your root password
                    if (online == true) {
                        System.out.print("ONLINE ");
                        System.out.print("`/ ");
                        System.out.println("✓");
                        //SAVE Card to DATABASE
                        boolean isValid = false;
                        boolean isUpdated = false;
                        //boolean alreadyExists = dbh.findEntranceCard(cardFromReader);
                        boolean isRecordedVIP = dbh.deleteVIP_DTR(cardFromReader);
                        if (isRecordedVIP) {
                            //isUpdated = dbh.updateVIPEntryRecordWPix(cardFromReader, CONSTANTS.entranceID);
                            System.out.println(cardFromReader + "isDeleted" + isRecordedVIP);
                            //cards.remove(0);
                        } else {
//                                isInserted = dbh.writeVIPEntryWithPix(CONSTANTS.entranceID, cardFromReader, "V", "");
                            System.out.println(cardFromReader + " DTR UnDeleted:" + isRecordedVIP);
                            //cards.remove(0);
                        }                        

                    } else if (online == false) {
                        System.out.println("OFFLINE");
                        System.out.print("-");
                    }
                    //this.currentThread().sleep(1000);
                    //ss.updateTimeOnChip(serverTime);
                    //System.out.println("NETWORK");
                    //resetAdmin();
                    //Thread.sleep(2000);
                } else {
                    //Thread.yield();
                    //this.currentThread().yield();
                    
                }
                //this.currentThread().sleep(1000);                    
//                System.out.println("`/");
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
//            System.out.print("./ ");
            //ss.checkTemp();
            
        }

    }

}
