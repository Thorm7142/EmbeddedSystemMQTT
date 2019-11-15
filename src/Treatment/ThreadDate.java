package Treatment;

import Windows.MainWindow;
import static java.lang.Thread.sleep;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Locale;

public class ThreadDate extends Thread
{
    private boolean activation;
    private MainWindow mw; 
    
    public int formatdate;
    public int formatheure;
    public Locale formatpays;
    
    public void setActivation(boolean tmp){activation = tmp;}
    /*public boolean getActivation(){ return activation;}
    public void setFenetreSallePresse(MainWindow tmp){mw = tmp;}
    public MainWindow getFenetreSallePresse(){ return mw;}*/
    
    public void setFormatDate(int tmp){formatdate = tmp;}
    public void setFormatHeure(int tmp){formatheure = tmp;}   
    public void setFormatLocale(Locale tmp){formatpays = tmp;}   

    public ThreadDate(boolean acti, MainWindow m)
    {
        setActivation(acti);
        mw = m; 
        
        setFormatDate(DateFormat.SHORT);
        setFormatHeure(DateFormat.MEDIUM);
        setFormatLocale(Locale.FRANCE);
        
    }
    
    /*  Thread qui va s'exécuter en même temps que la fenêtre principale et écrire sur celle-ci
        toutes les secondes afin de rafraichir l'heure. Ceci tout en respectant le format qui
        sera défini dans la fenêtre prévue à cet effet */
    @Override
    public void run()
    {
        while(activation == true)
        {
            Date date = new Date();
            
            int jour = date.getDate();
            
            date.setDate((jour+mw.getJour()));

            String maDate = DateFormat.getDateTimeInstance(formatdate,formatheure,formatpays).format(date);  // On donne le format désiré à la date

            mw.setDate(maDate); 

            try{sleep(1000);} // On rafraichit toutes les secondes
            catch (InterruptedException ex){Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);}
        }
    }   
}
