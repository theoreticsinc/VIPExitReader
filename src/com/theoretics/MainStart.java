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
import com.theoretics.NetworkClock;
import com.theoretics.RaspRC522;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.ConnectException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

//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
public class MainStart {

    String version = "v.3.0.2";
    String entranceID = "VIP Exit ";

    String cardFromReader = "";

    ArrayList<String> cards;
    private static Logger log = LogManager.getLogger(MainStart.class.getName());
    DateConversionHandler dch = new DateConversionHandler();
    private Thread ThrNetworkClock;
    private Thread ThrReaderClock;

    AudioInputStream welcomeAudioIn = null;
    AudioInputStream thankyouAudioIn = null;
    AudioInputStream pleasewaitAudioIn = null;
    AudioInputStream errorAudioIn = null;
    AudioInputStream beepAudioIn = null;
    AudioInputStream takeCardAudioIn = null;
    AudioInputStream bgAudioIn = null;
    Clip welcomeClip = null;
    Clip pleaseWaitClip = null;
    Clip thankyouClip = null;
    Clip beepClip = null;
    Clip takeCardClip = null;
    Clip errorClip = null;
    Clip bgClip = null;

    String strUID = "";
    String prevUID = "0";

    final GpioController gpio = GpioFactory.getInstance();

    //NOTE: Do not use GPIO 30-SDA0, 12-MOSI, 13-MISO, 14-DOES NOT WORK
    final GpioPinDigitalOutput led1 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_23, "GREENLED", PinState.HIGH);
    final GpioPinDigitalOutput led2 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_08, "BLUELED", PinState.HIGH);
    final GpioPinDigitalOutput led3 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_25, "REDLED", PinState.HIGH);

    final GpioPinDigitalInput btnPower = gpio.provisionDigitalInputPin(RaspiPin.GPIO_04, PinPullResistance.PULL_UP);
    final GpioPinDigitalInput btnReset = gpio.provisionDigitalInputPin(RaspiPin.GPIO_20, PinPullResistance.PULL_UP);
    final GpioPinDigitalInput btnDispense = gpio.provisionDigitalInputPin(RaspiPin.GPIO_28, PinPullResistance.PULL_UP);

    final GpioPinDigitalInput carDetected = gpio.provisionDigitalInputPin(RaspiPin.GPIO_29, PinPullResistance.PULL_UP);

    final GpioPinDigitalOutput relayBarrier = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_09, "BARRIER", PinState.HIGH);

    public void startProgram() {
        System.out.println(entranceID + " Standalone Listener " + version);
//        System.out.println(entranceID + " Tap Card Listener " + version);

//        Gson gson = new Gson();
//
//        String json = "{\"brand\":\"Jeep\", \"doors\": 3}";
//
//        VIPs car = gson.fromJson(json, VIPs.class);
//        
//        System.out.println(car.brand +"["+ car.doors + "]");
        this.cards = new ArrayList<String>();

        NetworkClock nc = new NetworkClock(this.cards);
        ThrNetworkClock = new Thread(nc);
        ThrNetworkClock.start();
//        ReaderClock rc = new ReaderClock(this.cards);
//        ThrReaderClock = new Thread(rc);
//        ThrReaderClock.start();
        //Old RC522 Reader
        //RaspRC522 rc522 = new RaspRC522();
        //rc522.RC522_Init();
        //New Black READER
        Scanner scan = null;

        String text = null;
        String cardUID = null;

        System.out.println("Reader Ready!");
        //Testing Dispenser
//        transistorDispense.pulse(2000, true);
//        Gpio.delay(2000);
        /*
        transistorDispense.setState(false);
        Gpio.delay(1000);
        transistorDispense.setState(true);
        Gpio.delay(1000);
        transistorDispense.setState(false);
        Gpio.delay(1000);
         */
 /*
        transistorReject.setState(false);
        Gpio.delay(1000);
        transistorReject.setState(true);
        Gpio.delay(1000);
        transistorReject.setState(false);
        Gpio.delay(1000);
         */
//
//        //Testing Barrier Relay
//        relayBarrier.setState(false);
//        Gpio.delay(2000);
//        relayBarrier.setState(true);
//        Gpio.delay(2000);
//        relayLights.setState(false);
//        Gpio.delay(2000);
//        relayLights.setState(true);
//        Gpio.delay(2000);
//        relayFan.setState(false);
//        Gpio.delay(2000);
//        relayFan.setState(true);
        System.out.println("Relays Ready!");
//        

//        Gpio.delay(2000);
//        relayFan.setState(false);
//        Gpio.delay(2000);
//        relayFan.setState(true);
//        Gpio.delay(2000);
//        relayLights.setState(false);
//        Gpio.delay(2000);
//        relayLights.setState(true);        
//        System.out.print("RELAYS Tested!");
        //Testing Remotely
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Date now = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-dd H:mm:ss");
                int minsOffset = 3;
                Calendar calendar = Calendar.getInstance();     //NOW
                calendar.setTime(now);
                calendar.add(Calendar.MINUTE, minsOffset);
                now = calendar.getTime();
                String d2 = sdf.format(now);        
                comms2WatchDog(d2);
        
            }
        }, 5000, 65000);
        //cards.add("72562862");
//        System.out.println("Testing vip masterlist == " + checkVIP_MasterList("0285656A"));
//        System.out.println("Testing vip masterlist == " + checkVIP_MasterList("62445A6A"));
        Date now = new Date();
        try {
            comms2POS("EXITVIP,WAS RESET," + now.toString() + ", ");
        } catch (Exception ex) {
            logWrite("Initial Comm2POS Error: " + ex);
        }
        while (true) {
            System.out.print("!reader is good!");
//            try {
//                comms2POS("EXITVIP,CDF07701, , ");
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }
            strUID = "";
            try {
                scan = new Scanner(System.in);
                if (scan.hasNextLong()) {
                    text = scan.nextLine();
                }
            } catch (Exception ex) {
                logWrite("Scanner Error: " + ex);
            }
            if (null != text) {
                try {
                    System.out.println("RAW: " + text);
                    //cardUID = Long.toHexString(Long.parseLong(text));
                    cardUID = Long.toHexString(Long.parseLong(text));
                    if (cardUID.length() == 7) {
                        cardUID = "0" + cardUID;
                    } else if (cardUID.length() == 6) {
                        cardUID = "00" + cardUID;
                    } else if (cardUID.length() == 5) {
                        cardUID = "000" + cardUID;
                    } else if (cardUID.length() == 4) {
                        cardUID = "0000" + cardUID;
                    } else if (cardUID.length() == 3) {
                        cardUID = "00000" + cardUID;
                    } else if (cardUID.length() == 2) {
                        cardUID = "000000" + cardUID;
                    }
                    //0892609774
                    System.out.println("RAW CARDUID: " + cardUID);
                    /*
                    if (text.startsWith("0")) {
                        cardUID = "0" + cardUID;
                    } else if (text.startsWith("00")) {
                        cardUID = "00" + cardUID;
                    } else if (text.startsWith("000")) {
                        cardUID = "000" + cardUID;
                    } else if (text.startsWith("0000")) {
                        cardUID = "0000" + cardUID;
                    } else if (text.startsWith("00000")) {
                        cardUID = "00000" + cardUID;
                    } else if (text.startsWith("000000")) {
                        cardUID = "000000" + cardUID;
                    }
                     */
                    //cardUID = Integer.toHexString(Integer.parseInt(text));
                    cardUID = cardUID.toUpperCase();

                    strUID = cardUID.substring(6, 8) + cardUID.substring(4, 6) + cardUID.substring(2, 4) + cardUID.substring(0, 2);

                    System.out.println("UID: " + cardUID.substring(6, 8) + cardUID.substring(4, 6) + cardUID.substring(2, 4) + cardUID.substring(0, 2));
                } catch (Exception ex) {
                    logWrite("Card Conversion: " + ex);
                }
                //System.out.println("" + stats);

                try {
                    if (prevUID.compareToIgnoreCase(strUID) != 0) {
                        //Uncomment Below to disable Read same Card
//                        prevUID = strUID;

                        System.out.println("Card Read UID:" + strUID.substring(0, 8));
                        cardFromReader = strUID.substring(0, 8).toUpperCase();
//
                        if (cardFromReader.compareToIgnoreCase("") != 0) {
                            //ThrNetworkClock.sleep(5000);
                            if (checkVIP_MasterList(cardFromReader)) {
                                relayBarrier.setState(false);
                                led1.low();
                                led2.high();
                                led3.high();
                                Gpio.delay(2000);
                                relayBarrier.setState(true);
                                led1.high();
                                led2.high();
                                led3.high();
                                try {
                                    comms2POS("EXITVIP,card number:" + cardFromReader + "," + now.toString() + ", Doctor");
                                } catch (Exception ex) {
                                    logWrite("comms2POS error:" + ex);
                                }
                                try {
                                    deletedVIP_DTR(cardFromReader);
                                } catch (Exception ex) {
                                    logWrite("comms2POS error:" + ex);
                                }
                            } else {
                                led1.high();
                                led2.high();
                                led3.low();
                                Gpio.delay(2000);
                                led1.high();
                                led2.high();
                                led3.high();
                            }
//
//                        //byte[] buffer2 = {0x2E};
//                        //comPort.writeBytes(buffer2, 1);
                        }

                        //led1.pulse(1250, true);
                        System.out.println("LED Open!");
                        //led2.pulse(1250, true);

                        // turn on gpio pin1 #01 for 1 second and then off
                        //System.out.println("--> GPIO state should be: ON for only 3 second");
                        // set second argument to 'true' use a blocking call
//                    c.showWelcome(700, false);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            
//            strUID = null;
//
//            transistorDispense.pulse(500, true);
//        transistorReject.pulse(500, true);
//        System.out.println("Test Dispense");
            //System.out.println("Hour :  " + now.getHours());
            if (now.getHours() >= 18) {
                //relayLights.low();
            }
            try {
                if (SystemInfo.getCpuTemperature() >= 65) {
                    System.out.println("CPU Temperature   :  " + SystemInfo.getCpuTemperature());
//                    relayFan.low();
//                    relayBarrier.low();
//                    transistorDispense.pulse(500, true);
                } else {
//                    relayFan.high();
//                    relayBarrier.high();
                }
            } catch (Exception ex) {
            }

//            if (null != strUID) {
//                if (strUID.compareTo("") == 0) {
//                    transistorDispense.pulse(500, true);
//                }
//            } else {
//                transistorDispense.pulse(500, true);
//            }
//            if (led1.isLow()) {
//                led1.high();
//            }
            System.out.println("EOL");
        }

        /**
         * Old Version RC522 Reader
         */
        /*
        while (true) {            
            rc522.Reader_Init();
            //System.out.print("!");
            int stats = 0;
            stats = rc522.Select_MifareOne(tagid);
            strUID = "";
            if (stats != 2) {
                //System.out.println("" + stats);
                strUID = Convert.bytesToHex(tagid);
                if (prevUID.compareToIgnoreCase(strUID) != 0) {
                    //Uncomment Below to disable Read same Card
                    prevUID = strUID;

                    System.out.println("Card Read UID:" + strUID.substring(0, 8));
                    cardFromReader = strUID.substring(0, 8).toUpperCase();
//
                    if (cardFromReader.compareToIgnoreCase("") != 0) {
                        cards.add(cardFromReader);
//
//                        //byte[] buffer2 = {0x2E};
//                        //comPort.writeBytes(buffer2, 1);
                    }

                    //led1.pulse(1250, true);
                    System.out.println("LED Open!");
                    //led2.pulse(1250, true);

                    // turn on gpio pin1 #01 for 1 second and then off
                    //System.out.println("--> GPIO state should be: ON for only 3 second");
                    // set second argument to 'true' use a blocking call
//                    c.showWelcome(700, false);
                }
            }
            rc522.Stop_Crypto();
            rc522.AntennaOff();
//            strUID = null;
//
            Date now = new Date();
//            transistorDispense.pulse(500, true);
//        transistorReject.pulse(500, true);
//        System.out.println("Test Dispense");
            //System.out.println("Hour :  " + now.getHours());
            if (now.getHours() >= 18) {
                //relayLights.low();
            }
            try {
                if (SystemInfo.getCpuTemperature() >= 65) {
                    System.out.println("CPU Temperature   :  " + SystemInfo.getCpuTemperature());
//                    relayFan.low();
//                    relayBarrier.low();
//                    transistorDispense.pulse(500, true);
                } else {
//                    relayFan.high();
//                    relayBarrier.high();
                }
            } catch (Exception ex) {
            }

//            if (null != strUID) {
//                if (strUID.compareTo("") == 0) {
//                    transistorDispense.pulse(500, true);
//                }
//            } else {
//                transistorDispense.pulse(500, true);
//            }
            if (led1.isLow()) {
                led1.high();
            }
            
            try {
                Thread.sleep(500);
//                rc522 = null;
//                Thread.sleep(3200);
//                Thread.yield();
              } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
         */
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
        System.out.println("Setting Up GPIO!");
        if (Gpio.wiringPiSetup() == -1) {
            System.out.println(" ==>> GPIO SETUP FAILED");
            return;
        }

        led1.setShutdownOptions(true, PinState.HIGH);
        led2.setShutdownOptions(true, PinState.HIGH);
        led3.setShutdownOptions(true, PinState.HIGH);

        relayBarrier.high();

        relayBarrier.setShutdownOptions(true, PinState.LOW);
//        relayFan.setShutdownOptions(true, PinState.LOW);
//        relayLights.setShutdownOptions(true, PinState.LOW);
        btnDispense.setMode(PinMode.DIGITAL_INPUT);
        btnDispense.setPullResistance(PinPullResistance.PULL_UP);
        btnPower.setMode(PinMode.DIGITAL_INPUT);
        btnPower.setPullResistance(PinPullResistance.PULL_UP);
        btnReset.setMode(PinMode.DIGITAL_INPUT);
        btnReset.setPullResistance(PinPullResistance.PULL_UP);

        // set shutdown state for this input pin
        btnDispense.setShutdownOptions(true);
        btnPower.setShutdownOptions(true);
        btnReset.setShutdownOptions(true);
        led1.high();
        led2.high();
        led3.high();

        //GREEN
//        led1.low();
//        Gpio.delay(2000);
//        led1.high();
//        Gpio.delay(2000);
        //BLUE
        //led2.low();
//        Gpio.delay(15000);
        led2.high();
        //Gpio.delay(5000);
        //RED
//        led3.low();
//        Gpio.delay(2000);
//        led3.high();
//        Gpio.delay(2000);

        //Testing Barrier Relay
        relayBarrier.setState(false);
        Gpio.delay(2000);
        relayBarrier.setState(true);
        Gpio.delay(2000);

        // create and register gpio pin listener
        btnDispense.addListener(new GpioPinListenerDigital() {
            @Override
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
                // display pin state on console
                System.out.println("Trying Welcome Message");
                try {
                    if (welcomeClip.isActive() == false) {
                        welcomeClip.setFramePosition(0);
                        welcomeClip.start();
                        System.out.println("Welcome Message OK");
                    }
                } catch (Exception ex) {
                    notifyError(ex);
                }
//                if (cardFromReader.compareToIgnoreCase("") != 0) {
//                    cards.add(cardFromReader);
//                    //byte[] buffer2 = {0x2E};
//                    //comPort.writeBytes(buffer2, 1);
//                }
                System.out.println("Dispense NOW!");
//                transistorDispense.pulse(500, true);
                //led1.blink(2000);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    java.util.logging.Logger.getLogger(MainStart.class.getName()).log(Level.SEVERE, null, ex);
                }
//               transistorDispense.pulse(500, true);
                //transistorDispense.pulse(1250, true);
                System.out.println("TRANSISTOR Open!");

                System.out.println(" --> GPIO PIN STATE CHANGE: " + event.getPin() + " = " + event.getState());
//                if (null != strUID && strUID.compareTo("") == 0) {
//                    transistorDispense.pulse(1250, true);
//                }

                Thread.yield();

                try {
                    if (takeCardClip.isActive() == false) {
                        takeCardClip.setFramePosition(0);
                        takeCardClip.start();
                        System.out.println("Take Card Please");
                    }
                } catch (Exception ex) {
                    notifyError(ex);
                }
//                
//                try {
//                    relayBarrier.setState(PinState.LOW);
//                    Thread.sleep(1000);
//                    relayBarrier.setState(PinState.HIGH);
//                    Thread.sleep(2000);
//
//                } catch (InterruptedException ex) {
//                    java.util.logging.Logger.getLogger(MainStart.class.getName()).log(Level.SEVERE, null, ex);
//                }
//
//                //Gpio.delayMicroseconds(1000);
//                try {                    
//                    relayBarrier.setState(PinState.LOW);
//                    relayLights.setState(PinState.LOW);
//                    relayFan.setState(PinState.LOW);
//                    Thread.sleep(1000);
//                
//                    relayBarrier.setState(PinState.HIGH);
//                    relayLights.setState(PinState.HIGH);
//                    relayFan.setState(PinState.HIGH);
//                    Thread.sleep(2000);
////                relayFan.toggle();
////                relayLights.toggle();
//                } catch (InterruptedException ex) {
//                    java.util.logging.Logger.getLogger(MainStart.class.getName()).log(Level.SEVERE, null, ex);
//                }
            }

        });

        // create and register gpio pin listener
        btnPower.addListener(new GpioPinListenerDigital() {
            @Override
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
                // display pin state on console
                System.out.println("POWER LED Pressed!");
                //led1.pulse(5000);
                System.out.println(" --> GPIO PIN STATE CHANGE: " + event.getPin() + " = " + event.getState());
                try {
                    Runtime r = Runtime.getRuntime();
                    Process p = r.exec("sudo reboot now");//u - update f - force
                    Thread.sleep(30000);

                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }

        });

        // create and register gpio pin listener
        btnReset.addListener(new GpioPinListenerDigital() {
            @Override
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
                // display pin state on console
                System.out.println("RESET LED!");
                //led1.pulse(5000);
                System.out.println(" --> GPIO PIN STATE CHANGE: " + event.getPin() + " = " + event.getState());
                try {
                    Runtime r = Runtime.getRuntime();
                    Process p = r.exec("sudo shutdown now");//u - update f - force
                    Thread.sleep(30000);

                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }

        });

        carDetected.addListener(new GpioPinListenerDigital() {
            @Override
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
                // display pin state on console
                if (event.getState() == PinState.LOW) {
                    System.out.println("CAR is now Present");
                }
                System.out.println(" --> GPIO PIN STATE CHANGE: " + event.getPin() + " = " + event.getState());
            }

        });
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
        } catch (Exception ex) {
            System.out.println("No POS Server available to receive Messages 2");
            //ex.printStackTrace();
            logWrite("Socket Error: " + ex);

        }
    }
    
    private void comms2WatchDog(String messageOut) {

        //System.out.println( "Loading contents of URL: " + POSserver );
        try {
            // Connect to the server
            Socket socket = new Socket("127.0.0.1", 7889);
            socket.setSoTimeout(3000);
            // Create input and output streams to read from and write to the server
            PrintStream out = new PrintStream(socket.getOutputStream());
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Follow the HTTP protocol of GET <path> HTTP/1.0 followed by an empty line
            out.println(messageOut);
            out.println();

            // Read data from the server until we finish reading the document
            /*String line = null;
            if (in != null) {
                line = in.readLine();
            }
            while (line != null && in != null) {
                try {
                    System.out.println(line);
                    line = in.readLine();
                } catch (Exception e) {
                    line = null;
                    in.close();
                    out.close();
                    socket.close();
                    //e.printStackTrace();
                }
            }
            // Close our streams
            in.close();
            out.close();
            socket.close();
             */
        } catch (java.net.ConnectException e) {
            System.out.println("ConnectException No POS Server available to receive Messages 1");
            //e.printStackTrace();
        } catch (Exception ex) {
            System.out.println("No POS Server available to receive Messages 2");
            ex.printStackTrace();
        }

    }

    public static void main(String[] args) {
        MainStart m = new MainStart();
        m.setupLED();
        m.startProgram();
//        while (true) {
//            Thread.sleep(5000L);
//        }
//
    }

    private boolean checkVIP_MasterList(String cardFromReader) {
        DataBaseHandler dbh = new DataBaseHandler(CONSTANTS.serverIP);
        try {
            SystemStatus ss = new SystemStatus();
            boolean online = ss.checkPING(CONSTANTS.serverIP);//LINUX USE ONLY - also check your root password

            if (online == true) {
                System.out.println("ONLINE");
                System.out.print("`/");

                //Find Card to DATABASE
                boolean isValidVIP = dbh.findCGHVIPCard(cardFromReader);
                if (isValidVIP) {
                    //isUpdated = dbh.updateVIPEntryRecordWPix(cardFromReader, CONSTANTS.entranceID);
                    //System.out.println(cardFromReader + "isUpdated" + isUpdated);
                    //System.out.println(cardFromReader + "isUpdated" + isUpdated);
                    //cards.add(cardFromReader);  // For DTR Recording of VIP
                    return true;
                } else {
                    //isInserted = dbh.writeVIPEntryWithPix(CONSTANTS.entranceID, cardFromReader, "R", "");
                    //System.out.println(cardFromReader + " isInserted:" + isInserted);
                    //System.out.println(cardFromReader + " isInserted:" + isInserted);
                    return false;
                }

            } else if (online == false) {
                System.out.println("OFFLINE");
                System.out.print("-");
            }
            Thread.sleep(100);
            //resetAdmin();
            //Thread.sleep(2000);

            System.out.println(".");

        } catch (Exception ex) {
            logWrite("CheckMasterlist Error: " + ex);
        }
        return false;
    }

    private boolean deletedVIP_DTR(String cardFromReader) {
        DataBaseHandler dbh = new DataBaseHandler(CONSTANTS.serverIP);
        try {
            SystemStatus ss = new SystemStatus();
            boolean online = ss.checkPING(CONSTANTS.serverIP);//LINUX USE ONLY - also check your root password

            if (online == true) {
                System.out.println("ONLINE");
                System.out.print("`/");

                boolean isRecordedVIP = dbh.deleteVIP_DTR(cardFromReader);
                //Find Card to DATABASE
                if (isRecordedVIP) {
                    //isUpdated = dbh.updateVIPEntryRecordWPix(cardFromReader, CONSTANTS.entranceID);
                    //System.out.println(cardFromReader + "isUpdated" + isUpdated);
                    //System.out.println(cardFromReader + "isUpdated" + isUpdated);
                    //cards.add(cardFromReader);  // For DTR Recording of VIP
                    return true;
                } else {
                    //isInserted = dbh.writeVIPEntryWithPix(CONSTANTS.entranceID, cardFromReader, "R", "");
                    //System.out.println(cardFromReader + " isInserted:" + isInserted);
                    //System.out.println(cardFromReader + " isInserted:" + isInserted);
                    return false;
                }

            } else if (online == false) {
                System.out.println("OFFLINE");
                System.out.print("-");
            }
            Thread.sleep(100);
            //resetAdmin();
            //Thread.sleep(2000);

            System.out.println(".");

        } catch (Exception ex) {
            logWrite("CheckMasterlist Error: " + ex);
        }
        return false;
    }

    private void logWrite(String error) {
        FileWriter fileWriter = null;
        PrintWriter printWriter = null;
        try {
            java.util.Date nowStamp = new java.util.Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMddHmmss");
            String d2 = sdf.format(nowStamp);
            String fileName = "/home/pi/VIP" + d2;
            fileWriter = new FileWriter(fileName);
            printWriter = new PrintWriter(fileWriter);
            printWriter.print(error);
//            printWriter.printf("Product name is %s and its price is %d $", "iPhone", 1000);            
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(MainStart.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                printWriter.close();
                fileWriter.close();
            } catch (IOException ex) {
                java.util.logging.Logger.getLogger(MainStart.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
