package com.theoretics;

import com.pi4j.wiringpi.Spi;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinMode;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import com.pi4j.platform.PlatformManager;
import com.pi4j.system.NetworkInfo;
import com.pi4j.system.SystemInfo;
import com.pi4j.wiringpi.Gpio;
import com.theoretics.Convert;
import com.theoretics.DateConversionHandler;
import com.theoretics.RaspRC522;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class MainStart {

    String version = "v.3.0.2";
    String entranceID = "VIP EXIT CARD READER 1";

    String cardFromReader = "";

    ArrayList<String> cards;
    DateConversionHandler dch = new DateConversionHandler();
    private Thread ThrNetworkClock;
//    final GpioPinDigitalOutput pin1;

    AudioInputStream welcomeAudioIn = null;
    AudioInputStream thankyouAudioIn = null;
    AudioInputStream pleasewaitAudioIn = null;
    AudioInputStream errorAudioIn = null;
    AudioInputStream beepAudioIn = null;
    AudioInputStream cartoonCardAudioIn = null;
    AudioInputStream bgAudioIn = null;
    Clip welcomeClip = null;
    Clip pleaseWaitClip = null;
    Clip thankyouClip = null;
    Clip beepClip = null;
    Clip cartoonCardClip = null;
    Clip errorClip = null;
    Clip bgClip = null;

    String strUID = "";
    String prevUID = "0";

    final GpioController gpio = GpioFactory.getInstance();

//    final GpioPinDigitalOutput relayBarrier = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_05, "BARRIER", PinState.LOW);
    final GpioPinDigitalOutput relayBarrier = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_09, "BARRIER", PinState.LOW);

    public void showInfo() {
        // display a few of the available system information properties
        System.out.println("----------------------------------------------------");
        System.out.println("PLATFORM INFO");
        System.out.println("----------------------------------------------------");
        try {
            System.out.println("Platform Name     :  " + PlatformManager.getPlatform().getLabel());
        } catch (Exception ex) {
        }
        try {
            System.out.println("Platform ID       :  " + PlatformManager.getPlatform().getId());
        } catch (Exception ex) {
        }
        System.out.println("----------------------------------------------------");
        System.out.println("HARDWARE INFO");
        System.out.println("----------------------------------------------------");
        try {
            System.out.println("Serial Number     :  " + SystemInfo.getSerial());
        } catch (Exception ex) {
        }
        try {
            System.out.println("CPU Revision      :  " + SystemInfo.getCpuRevision());
        } catch (Exception ex) {
        }
        try {
            System.out.println("CPU Architecture  :  " + SystemInfo.getCpuArchitecture());
        } catch (Exception ex) {
        }
        try {
            System.out.println("CPU Part          :  " + SystemInfo.getCpuPart());
        } catch (Exception ex) {
        }
        try {
            System.out.println("CPU Temperature   :  " + SystemInfo.getCpuTemperature());
        } catch (Exception ex) {
        }
        try {
            System.out.println("CPU Core Voltage  :  " + SystemInfo.getCpuVoltage());
        } catch (Exception ex) {
        }
        try {
            System.out.println("CPU Model Name    :  " + SystemInfo.getModelName());
        } catch (Exception ex) {
        }
        try {
            System.out.println("Processor         :  " + SystemInfo.getProcessor());
        } catch (Exception ex) {
        }
        try {
            System.out.println("Hardware          :  " + SystemInfo.getHardware());
        } catch (Exception ex) {
        }
        try {
            System.out.println("Hardware Revision :  " + SystemInfo.getRevision());
        } catch (Exception ex) {
        }
        try {
            System.out.println("Is Hard Float ABI :  " + SystemInfo.isHardFloatAbi());
        } catch (Exception ex) {
        }
        try {
            System.out.println("Board Type        :  " + SystemInfo.getBoardType().name());
        } catch (Exception ex) {
        }

        System.out.println("----------------------------------------------------");
        System.out.println("MEMORY INFO");
        System.out.println("----------------------------------------------------");
        try {
            System.out.println("Total Memory      :  " + SystemInfo.getMemoryTotal());
        } catch (Exception ex) {
        }
        try {
            System.out.println("Used Memory       :  " + SystemInfo.getMemoryUsed());
        } catch (Exception ex) {
        }
        try {
            System.out.println("Free Memory       :  " + SystemInfo.getMemoryFree());
        } catch (Exception ex) {
        }
        try {
            System.out.println("Shared Memory     :  " + SystemInfo.getMemoryShared());
        } catch (Exception ex) {
        }
        try {
            System.out.println("Memory Buffers    :  " + SystemInfo.getMemoryBuffers());
        } catch (Exception ex) {
        }
        try {
            System.out.println("Cached Memory     :  " + SystemInfo.getMemoryCached());
        } catch (Exception ex) {
        }
        try {
            System.out.println("SDRAM_C Voltage   :  " + SystemInfo.getMemoryVoltageSDRam_C());
        } catch (Exception ex) {
        }
        try {
            System.out.println("SDRAM_I Voltage   :  " + SystemInfo.getMemoryVoltageSDRam_I());
        } catch (Exception ex) {
        }
        try {
            System.out.println("SDRAM_P Voltage   :  " + SystemInfo.getMemoryVoltageSDRam_P());
        } catch (Exception ex) {
        }

        System.out.println("----------------------------------------------------");
        System.out.println("OPERATING SYSTEM INFO");
        System.out.println("----------------------------------------------------");
        try {
            System.out.println("OS Name           :  " + SystemInfo.getOsName());
        } catch (Exception ex) {
        }
        try {
            System.out.println("OS Version        :  " + SystemInfo.getOsVersion());
        } catch (Exception ex) {
        }
        try {
            System.out.println("OS Architecture   :  " + SystemInfo.getOsArch());
        } catch (Exception ex) {
        }
        try {
            System.out.println("OS Firmware Build :  " + SystemInfo.getOsFirmwareBuild());
        } catch (Exception ex) {
        }
        try {
            System.out.println("OS Firmware Date  :  " + SystemInfo.getOsFirmwareDate());
        } catch (Exception ex) {
        }

        System.out.println("----------------------------------------------------");
        System.out.println("JAVA ENVIRONMENT INFO");
        System.out.println("----------------------------------------------------");
        System.out.println("Java Vendor       :  " + SystemInfo.getJavaVendor());
        System.out.println("Java Vendor URL   :  " + SystemInfo.getJavaVendorUrl());
        System.out.println("Java Version      :  " + SystemInfo.getJavaVersion());
        System.out.println("Java VM           :  " + SystemInfo.getJavaVirtualMachine());
        System.out.println("Java Runtime      :  " + SystemInfo.getJavaRuntime());

        System.out.println("----------------------------------------------------");
        System.out.println("NETWORK INFO");
        System.out.println("----------------------------------------------------");

        try {
            // display some of the network information
            System.out.println("Hostname          :  " + NetworkInfo.getHostname());
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(MainStart.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            java.util.logging.Logger.getLogger(MainStart.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            for (String ipAddress : NetworkInfo.getIPAddresses()) {
                System.out.println("IP Addresses      :  " + ipAddress);
            }
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(MainStart.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            java.util.logging.Logger.getLogger(MainStart.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            for (String fqdn : NetworkInfo.getFQDNs()) {
                System.out.println("FQDN              :  " + fqdn);
            }
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(MainStart.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            java.util.logging.Logger.getLogger(MainStart.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            for (String nameserver : NetworkInfo.getNameservers()) {
                System.out.println("Nameserver        :  " + nameserver);
            }
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(MainStart.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            java.util.logging.Logger.getLogger(MainStart.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println("----------------------------------------------------");
        System.out.println("CODEC INFO");
        System.out.println("----------------------------------------------------");
        try {
            System.out.println("H264 Codec Enabled:  " + SystemInfo.getCodecH264Enabled());
        } catch (Exception ex) {
        }
        try {
            System.out.println("MPG2 Codec Enabled:  " + SystemInfo.getCodecMPG2Enabled());
        } catch (Exception ex) {
        }
        try {
            System.out.println("WVC1 Codec Enabled:  " + SystemInfo.getCodecWVC1Enabled());
        } catch (Exception ex) {
        }

        System.out.println("----------------------------------------------------");
        System.out.println("CLOCK INFO");
        System.out.println("----------------------------------------------------");
        try {
            System.out.println("ARM Frequency     :  " + SystemInfo.getClockFrequencyArm());
        } catch (Exception ex) {
        }
        try {
            System.out.println("CORE Frequency    :  " + SystemInfo.getClockFrequencyCore());
        } catch (Exception ex) {
        }
        try {
            System.out.println("H264 Frequency    :  " + SystemInfo.getClockFrequencyH264());
        } catch (Exception ex) {
        }
        try {
            System.out.println("ISP Frequency     :  " + SystemInfo.getClockFrequencyISP());
        } catch (Exception ex) {
        }
        try {
            System.out.println("V3D Frequency     :  " + SystemInfo.getClockFrequencyV3D());
        } catch (Exception ex) {
        }
        try {
            System.out.println("UART Frequency    :  " + SystemInfo.getClockFrequencyUART());
        } catch (Exception ex) {
        }
        try {
            System.out.println("PWM Frequency     :  " + SystemInfo.getClockFrequencyPWM());
        } catch (Exception ex) {
        }
        try {
            System.out.println("EMMC Frequency    :  " + SystemInfo.getClockFrequencyEMMC());
        } catch (Exception ex) {
        }
        try {
            System.out.println("Pixel Frequency   :  " + SystemInfo.getClockFrequencyPixel());
        } catch (Exception ex) {
        }
        try {
            System.out.println("VEC Frequency     :  " + SystemInfo.getClockFrequencyVEC());
        } catch (Exception ex) {
        }
        try {
            System.out.println("HDMI Frequency    :  " + SystemInfo.getClockFrequencyHDMI());
        } catch (Exception ex) {
        }
        try {
            System.out.println("DPI Frequency     :  " + SystemInfo.getClockFrequencyDPI());
        } catch (Exception ex) {
        }

        System.out.println();
        System.out.println();
        System.out.println("Exiting SystemInfoExample");
    }

    public void startProgram() {
        System.out.println(entranceID + " Tap Card Listener " + version);
//        System.out.println(entranceID + " Tap Card Listener " + version);
        try {
            welcomeAudioIn = AudioSystem.getAudioInputStream(MainStart.class.getResource("/sounds/welcome.wav"));
            welcomeClip = AudioSystem.getClip();
            welcomeClip.open(welcomeAudioIn);
        } catch (Exception ex) {
            notifyError(ex);
        }
        try {
            pleasewaitAudioIn = AudioSystem.getAudioInputStream(MainStart.class.getResource("/sounds/plswait.wav"));
            pleaseWaitClip = AudioSystem.getClip();
            pleaseWaitClip.open(pleasewaitAudioIn);
        } catch (Exception ex) {
            notifyError(ex);
        }
        try {
            thankyouAudioIn = AudioSystem.getAudioInputStream(MainStart.class.getResource("/sounds/thankyou.wav"));
            thankyouClip = AudioSystem.getClip();
            thankyouClip.open(thankyouAudioIn);
        } catch (Exception ex) {
            notifyError(ex);
        }
        try {
            beepAudioIn = AudioSystem.getAudioInputStream(MainStart.class.getResource("/sounds/beep.wav"));
            beepClip = AudioSystem.getClip();
            beepClip.open(beepAudioIn);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        try {
            cartoonCardAudioIn = AudioSystem.getAudioInputStream(MainStart.class.getResource("/sounds/cartoon.wav"));
            cartoonCardClip = AudioSystem.getClip();
            cartoonCardClip.open(cartoonCardAudioIn);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        try {
            errorAudioIn = AudioSystem.getAudioInputStream(MainStart.class.getResource("/sounds/beep.wav"));
            errorClip = AudioSystem.getClip();
            errorClip.open(errorAudioIn);
        } catch (Exception ex) {
            notifyError(ex);
        }

        try {
            bgAudioIn = AudioSystem.getAudioInputStream(MainStart.class.getResource("/sounds/bgmusic.wav"));
            bgClip = AudioSystem.getClip();
            bgClip.open(bgAudioIn);
        } catch (Exception ex) {
            notifyError(ex);
        }

        try {
            if (welcomeClip.isActive() == false) {
                welcomeClip.setFramePosition(0);
                welcomeClip.start();
                System.out.println("Welcome Message OK");
            }
        } catch (Exception ex) {
            notifyError(ex);
        }

        this.cards = new ArrayList<String>();
        Scanner scan = new Scanner(System.in);

        String text = null;
        String cardUID = null;
        System.out.println("Reader Ready!");
//        transistorDispense.pulse(1000, true);
//        Gpio.delay(2000);
//        transistorReject.pulse(1000, true);
        //Testing Remotely
//        cards.add("ABC1234");
        while (true) {
//            
            Date now = new Date();
            //System.out.println("Hour :  " + now.getHours());
            if (now.getHours() >= 18) {
                //relayLights.low();
            }
            try {
                if (SystemInfo.getCpuTemperature() >= 45) {
                    System.out.println("CPU Temperature   :  " + SystemInfo.getCpuTemperature());
                    //relayFan.low();
                } else {
                    //relayFan.high();
                }
            } catch (Exception ex) {
            }
            //System.out.print("!");
//                relayBarrier.low(); //RELAY ON
//                        System.out.println("Barrier Open!");
//                        try {
//                            Thread.sleep(500);
//                            relayBarrier.high();
//                            Thread.sleep(1500);
//                        } catch (InterruptedException ex) {
//                            Logger.getLogger(MainStart.class.getName()).log(Level.SEVERE, null, ex);
//                        }
            strUID = "";
            text = scan.nextLine();
            if (null != text) {
                try {
                    System.out.println("RAW: " + text);
                    cardUID = Long.toHexString(Long.parseLong(text));
                    if (text.startsWith("0")) {
                        cardUID = "0" + cardUID;
                    } else if (text.startsWith("00")) {
                        cardUID = "00" + cardUID;
                    } else if (text.startsWith("000")) {
                        cardUID = "000" + cardUID;
                    } else if (text.startsWith("0000")) {
                        cardUID = "0000" + cardUID;
                    }
                    //cardUID = Integer.toHexString(Integer.parseInt(text));
                    cardUID = cardUID.toUpperCase();
                    System.out.println("UID: " + cardUID.substring(6, 8) + cardUID.substring(4, 6) + cardUID.substring(2, 4) + cardUID.substring(0, 2));
                } catch (Exception ex) {
                    System.err.println("Card Conversion: " + ex);
                }
                strUID = cardUID.substring(6, 8) + cardUID.substring(4, 6) + cardUID.substring(2, 4) + cardUID.substring(0, 2);
                //System.out.println("" + stats);
                if (prevUID.compareToIgnoreCase(strUID) != 0) {
                    //Uncomment Below to disable Read same Card
                    //prevUID = strUID;

                    System.out.println("Card Read UID:" + strUID.substring(0, 8));
                    cardFromReader = strUID.substring(0, 8).toUpperCase();
                    DataBaseHandler dbh = new DataBaseHandler();
                    String cardHolder = dbh.findVIPcard(cardFromReader);
                    if (cardHolder.compareTo("") != 0) {
                        
                        relayBarrier.low(); //RELAY ON
                        System.out.println("Barrier Open!");
                        try{
                            dbh.deleteVIP(cardFromReader);
                            dbh.exitVIP(cardFromReader);
                        } catch (Exception ex) {
                            Logger.getLogger(MainStart.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        try {
                            Thread.sleep(500);
                            relayBarrier.high();
                            Thread.sleep(1500);
                            dbh.sendMessage(0, " Thank you " + cardHolder + "!", "VIP Exit", "EX01");
                        } catch (Exception ex) {
                            Logger.getLogger(MainStart.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        if (dbh.exitVIP(cardFromReader)) {
                            dbh.sendMessage(0, "VIP Card" + cardFromReader + " successful Exit", "VIP Exit", "EX01");                            
                        } else {
                            if (dbh.exitVIP(cardFromReader)) {
                                dbh.sendMessage(0, "VIP Card" + cardFromReader + " successful Exit", "VIP Exit", "EX01");
                            } else {
                                dbh.sendMessage(0, " Please try to exit " + cardFromReader + " again", "VIP Exit", "EX01");
                            }
                        }
                        
                    } else if (cardFromReader.compareToIgnoreCase("3B40CB73") == 0) {
                        relayBarrier.low(); //RELAY ON
                        System.out.println("MASTER KEY == Barrier Open!");
                        try {
                            Thread.sleep(500);
                            relayBarrier.high();
                            Thread.sleep(1500);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(MainStart.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
//
//                    if (cardFromReader.compareToIgnoreCase("") != 0) {
//                        cards.add(cardFromReader);
//                        
//                        //byte[] buffer2 = {0x2E};
//                        //comPort.writeBytes(buffer2, 1);
//                    }

                    // turn on gpio pin1 #01 for 1 second and then off
                    //System.out.println("--> GPIO state should be: ON for only 3 second");
                    // set second argument to 'true' use a blocking call
//                    c.showWelcome(700, false);
                }
            }
//            try {
//                Thread.sleep(500);                
//            } catch (InterruptedException ex) {
//                Logger.getLogger(MainStart.class.getName()).log(Level.SEVERE, null, ex);
//            }
        }

    }

    private void notifyError(Exception ex) {
        System.out.println(ex.getMessage());
        try {
            if (errorClip.isActive() == false) {
                //haltButton = false;
                errorClip.setFramePosition(0);
                errorClip.start();
            }
        } catch (Exception ex2) {
            System.out.println(ex2.getMessage());
        }
    }

    public void testCard() {
        //读卡，得到序列号
//        if(rc522.Request(RaspRC522.PICC_REQIDL, back_bits) == rc522.MI_OK)
//            System.out.println("Detected:"+back_bits[0]);
//        if(rc522.AntiColl(tagid) != RaspRC522.MI_OK)
//        {
//            System.out.println("anticoll error");
//            return;
//        }
//
//        //Select the scanned tag，选中指定序列号的卡
//        int size=rc522.Select_Tag(tagid);
//        System.out.println("Size="+size);
//有两块(8*8)的屏幕
//		Led c = new Led((short)4);
//		c.brightness((byte)10);
        //打开设备
//		c.open();
        //旋转270度，缺省两个屏幕是上下排列，我需要的是左右排
//		c.orientation(270);
        //DEMO1: 输出两个字母
        //c.letter((short)0, (short)'Y',false);
        //c.letter((short)1, (short)'C',false);
//		c.flush();
        //c.showWelcome(700, false);
//		c.flush();
        //DEMO3: 输出一串字母
//		c.showMessage("Hello 0123456789$");
        //try {
        //	System.in.read();
        //	c.close();
        //} catch (IOException e) {
        // TODO Auto-generated catch block
        //	e.printStackTrace();
        //}

        //        System.out.println("Card Read UID:" + strUID.substring(0,2) + "," +
//                strUID.substring(2,4) + "," +
//                strUID.substring(4,6) + "," +
//                strUID.substring(6,8));
/*
        //default key
        byte []keyA=new byte[]{(byte)0x03,(byte)0x03,(byte)0x00,(byte)0x01,(byte)0x02,(byte)0x03};
        byte[] keyB=new byte[]{(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF};


        //Authenticate,A密钥验证卡,可以读数据块2
        byte data[]=new byte[16];
        status = rc522.Auth_Card(RaspRC522.PICC_AUTHENT1A, sector,block, keyA, tagid);
        if(status != RaspRC522.MI_OK)
        {
            System.out.println("Authenticate A error");
            return;
        }

        status=rc522.Read(sector,block,data);
        //rc522.Stop_Crypto();
        System.out.println("Successfully authenticated,Read data="+Convert.bytesToHex(data));
        status=rc522.Read(sector,(byte)3,data);
        System.out.println("Read control block data="+Convert.bytesToHex(data));


        for (i = 0; i < 16; i++)
        {
            data[i]=(byte)0x00;
        }

        //Authenticate,B密钥验证卡,可以写数据块2
        status = rc522.Auth_Card(RaspRC522.PICC_AUTHENT1B, sector,block, keyB, tagid);
        if(status != RaspRC522.MI_OK)
        {
            System.out.println("Authenticate B error");
            return;
        }

        status=rc522.Write(sector,block,data);
        if( status== RaspRC522.MI_OK)
            System.out.println("Write data finished");
        else
        {
            System.out.println("Write data error,status="+status);
            return;
        }
         */
//        byte buff[]=new byte[16];
//
//        for (i = 0; i < 16; i++)
//        {
//            buff[i]=(byte)0;
//        }
//        status=rc522.Read(sector,block,buff);
//        if(status == RaspRC522.MI_OK)
//            System.out.println("Read Data finished");
//        else
//        {
//            System.out.println("Read data error,status="+status);
//            return;
//        }
//
//        System.out.print("sector"+sector+",block="+block+" :");
//        String strData= Convert.bytesToHex(buff);
//        for (i=0;i<16;i++)
//        {
//            System.out.print(strData.substring(i*2,i*2+2));
//            if(i < 15) System.out.print(",");
//            else System.out.println("");
//        }
    }

    public void setupLED() {
        System.out.println("Testing Relay!");
        relayBarrier.high();
        relayBarrier.setShutdownOptions(true, PinState.LOW);
    }

    public static void rfidReaderLoop(int sleeptime) throws InterruptedException {
        int count = 0;
        while (count++ < 3) {

            int packetlength = 5;

            byte packet[] = new byte[packetlength];
            packet[0] = (byte) 0x80; // FIRST PACKET GETS IGNORED BUT HAS
            // TO BE SET TO READ
            packet[1] = (byte) 0x80; // ADDRESS 0 Gives data of Address 0
            packet[2] = (byte) 0x82; // ADDRESS 1 Gives data of Address 1
            packet[3] = (byte) 0x84; // ADDRESS 2 Gives data of Address 2
            packet[4] = (byte) 0x86; // ADDRESS 3 Gives data of Address 3

            System.out.println("-----------------------------------------------");
            System.out.println("Data to be transmitted:");
            System.out.println("[TX] " + bytesToHex(packet));
            System.out.println("[TX1] " + packet[1]);
            System.out.println("[TX2] " + packet[2]);
            System.out.println("[TX3] " + packet[3]);
            System.out.println("[TX4] " + packet[4]);
            System.out.println("Transmitting data...");

            // Send data to Reader and receive answerpacket.
            packet = readFromRFID(0, packet, packetlength);

            System.out.println("Data transmitted, packets received.");
            System.out.println("Received Packets (First packet to be ignored!)");
            System.out.println("[RX] " + bytesToHex(packet));
            System.out.println("[RX1] " + packet[1]);
            System.out.println("[RX2] " + packet[2]);
            System.out.println("[RX3] " + packet[3]);
            System.out.println("[RX4] " + packet[4]);
            System.out.println("-----------------------------------------------");

            if (packet.length == 0) {
                //Reset when no packet received
                //ResetPin.high();
                Thread.sleep(50);
                //ResetPin.low();
            }

            // Wait 1/2 second before trying to read again
            Thread.sleep(sleeptime);
        }

    }

    public static byte[] readFromRFID(int channel, byte[] packet, int length) {
        Spi.wiringPiSPIDataRW(channel, packet, length);

        return packet;
    }

    public static boolean writeToRFID(int channel, byte fullAddress, byte data) {

        byte[] packet = new byte[2];
        packet[0] = fullAddress;
        packet[1] = data;

        if (Spi.wiringPiSPIDataRW(channel, packet, 1) >= 0) {
            return true;
        } else {
            return false;
        }
    }

    public static String bytesToHex(byte[] bytes) {
        final char[] hexArray = {'0', '1', '2', '3', '4', '5', '6', '7', '8',
            '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        char[] hexChars = new char[bytes.length * 2];
        int v;
        for (int j = 0; j < bytes.length; j++) {
            v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static void main(String[] args) throws InterruptedException {
        MainStart m = new MainStart();
        m.setupLED();
        m.showInfo();
        m.startProgram();
//        while (true) {            
//            Thread.sleep(5000L);
//        }
//        
    }

}
