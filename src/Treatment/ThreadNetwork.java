
  
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Treatment;

import Windows.MainWindow;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;

/**
 *
 * @author alexa
 */
public class ThreadNetwork extends Thread implements MqttCallback{
    
    MqttClient myClient;
    MqttConnectOptions connOpt;

    static final String BROKER_URL = "tcp://192.168.0.22:1883" ;
    static final String M2MIO_USERNAME = "<m2m.io username>";
    static final String M2MIO_PASSWORD_MD5 = "<m2m.io password (MD5 sum of password)>";
    
    MainWindow mw;

    // the following two flags control whether this example is a publisher, a subscriber or both
    static final Boolean subscriber = true;
    static final Boolean publisher = true;
    
    public ThreadNetwork (MainWindow m)
    {
        mw = m;
    }

    public int Connexion()
    {
        
        String clientID = MqttClient.generateClientId();
        connOpt = new MqttConnectOptions();

        connOpt.setCleanSession(true);

        connOpt.setUserName("root"); // A CHANGER
        connOpt.setPassword("root".toCharArray()); // A CHANGER
        connOpt.setAutomaticReconnect(true);

        // Connect to Broker
        try {
                myClient = new MqttClient(BROKER_URL, clientID);
                myClient.setCallback(this);
                myClient.connect(connOpt);
        } 
        catch (MqttException e) 
        {
                e.printStackTrace();
                System.exit(-1);
        }

        System.out.println("Connected to " + BROKER_URL);
                
        try {
            Thread.sleep(100);
        } catch (InterruptedException ex) {
            Logger.getLogger(ThreadNetwork.class.getName()).log(Level.SEVERE, null, ex);
        }

          return 1;
    }
    
    public void init()
    {
        Connexion();
        String myTopic = "topic/Intruder"; // CHANGER
        //MqttTopic topic = myClient.getTopic(myTopic);
        if (subscriber) 
        {
            try 
            {
                    int subQoS = 0;
                    myClient.subscribe(myTopic, subQoS);
                    System.out.println("SUBSCRIBED");
            } 
            catch (Exception e) 
            {
                    e.printStackTrace();
            }
        }

		// publish messages if publisher
		/*if (publisher) {
                    for (int i=1; i<=10; i++) {
                        String pubMsg = "b{\"pubmsg\":" + i + "}";
                        System.out.println("Nouveau msg cree : " + pubMsg);
                        int pubQoS = 0;
                        MqttMessage message = new MqttMessage(pubMsg.getBytes());
                        message.setQos(pubQoS);
                        message.setRetained(false);
                        // Publish the message
                        System.out.println("Publishing to topic \"" + topic + "\" qos " + pubQoS);
                        MqttDeliveryToken token = null;
                        try {
                        // publish message to broker
                           
                            //token = topic.publish(message);
                            myClient.publish("yolo/swag",message);
                            Thread.sleep(1500);
                        // Wait until the message has been delivered to the broker
                                //token.waitForCompletion();
                        } catch (Exception e) {
                                e.printStackTrace();
                        }
                    }			
		}*/
                
        
    }
    
    @Override
    public void run()
    {
        init();
        while(true) {
        } 
        
    }

    @Override
    public void connectionLost(Throwable thrwbl) {
        System.out.println("Connection lost!");
        thrwbl.printStackTrace();
    }

    @Override
    public void messageArrived(String string, MqttMessage mm) throws Exception {
       
        System.out.println("Message recu !");
        
        if(mw.getMode()) // Mode NUIT
        {            
            if(Integer.parseInt(mm.toString()) == 0)
            {
                mw.getTi().setActivation(true);
            }

        }
        else // Mode JOUR
        {
            if(Integer.parseInt(mm.toString()) == 0)
            {
                mw.Update();
            }
            
            //buzz(1000);
        }
        //System.out.println("string = " + string);
        
    }
    
    public void buzz(int milli)
    {
        String myTopic = "topic/Intruder"; // A CHANGER
        MqttTopic topic = myClient.getTopic(myTopic);
        
        String pubMsg = "b" +  Integer.toString(milli);
        System.out.println("Nouveau msg cree : " + pubMsg);
        int pubQoS = 0;
        MqttMessage message = new MqttMessage(pubMsg.getBytes());
        message.setQos(pubQoS);
        message.setRetained(false);

        // Publish the message
        System.out.println("Publishing to topic \"" + topic + "\" qos " + pubQoS);
        MqttDeliveryToken token = null;
        // publish message to broker
        try {
            //token = topic.publish(message);²
            myClient.publish("topic/buzzer",message); // CHANGER
        } catch (MqttException ex) {
            Logger.getLogger(ThreadNetwork.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken imdt) {
        
    }
}