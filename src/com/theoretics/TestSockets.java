/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.theoretics;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

/**
 *
 * @author Theoretics
 */
public class TestSockets {
    private void comms2POS(String messageOut) {
    String POSserver = "192.168.1.23";
        //System.out.println( "Loading contents of URL: " + POSserver );
        try {
            // Connect to the server
            Socket socket = new Socket(POSserver, CONSTANTS.port);

            // Create input and output streams to read from and write to the server
            PrintStream out = new PrintStream(socket.getOutputStream());
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Follow the HTTP protocol of GET <path> HTTP/1.0 followed by an empty line
            out.println(messageOut);
            out.println();

            // Read data from the server until we finish reading the document
            String line = in.readLine();
            while (line != null && in != null) {
                try {
                    System.out.println(line);
                    line = in.readLine();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            // Close our streams
            in.close();
            out.close();
            socket.close();
        } catch (java.net.ConnectException e) {
            System.out.println("No POS Server available to receive Messages 1");
            //e.printStackTrace();
        } catch (IOException ex) {
            System.out.println("No POS Server available to receive Messages 2");
            //ex.printStackTrace();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        TestSockets ts = new TestSockets();
        String cardFromReader = "A2ECEE61";
        ts.comms2POS("EXITVIP,card number:" + cardFromReader + ",NOW, Doctor");
        
    }
}
