/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Treatment;

import Windows.MainWindow;
import java.awt.Color;

/**
 *
 * @author alexa
 */
public class ThreadIntruder  extends Thread{
    
    private boolean activation;
    private MainWindow mw = null;

    public void setActivation(boolean activation) {
        this.activation = activation;
    }
    
    public ThreadIntruder(boolean act, MainWindow m)
    {
        mw = m;
        activation = act;
    }
    
    @Override
    public void run()
    {
        while(true)
        {
            while(activation == true)
            { 
                mw.setIntrusion(new Color(153,0,0), true);

                try{sleep(1000);} // On rafraichit toutes les secondes
                catch (InterruptedException ex)
                {System.out.println("Interrupt errror during Thread Intruder");}

                mw.setIntrusion(new Color(0,0,153), false);

                try{sleep(1000);}
                catch (InterruptedException ex)
                {System.out.println("Interrupt errror during Thread Intruder");}
            }
        }

    }
    

    
}
