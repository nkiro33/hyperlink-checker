/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BackEnd;

import java.io.IOException;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 *
 * @author 20122
 */
public class LinkValidator {

    public static ExecutorService executorService = Executors.newFixedThreadPool(Data.numOfThreads);

    public static boolean validateMainLink(String x){  //checks the validity of url 
        
        try {
            Document y = Jsoup.connect(x).get();
        }catch (ConnectException ex){ // connection error
            return false;
        } catch (HttpStatusException ex) {  // 404 page not found
            return false;
        }  catch (IOException ex) { // host name not found
           return false;
        }
       return true;
    }
    public static void validateURL(String link, int currentDepth, int depth) {
        ValidationThread validator;
        if (validateMainLink(link)) {
           // System.out.println("Valid : " + link);
            Data.validLinks.add(link);
            if (currentDepth == depth) {
                return;
            }
            Document doc = null;
            try {
                doc = Jsoup.connect(link).get();
            } catch (IOException ex) {
                //Logger.getLogger(LinkValidator.class.getName()).log(Level.SEVERE, null, ex);
            }
            Elements links = doc.select("a[href]"); //to get lines of links only
          //  System.err.println("Number of Links : " + e.size());

            URL u = null;
            try {
                u = new URL(link);
            } catch (MalformedURLException ex) {
                Logger.getLogger(LinkValidator.class.getName()).log(Level.SEVERE, null, ex);
            }
            String base = u.getProtocol() + "://" + u.getHost(); // the base name for the host link
            for (int i = 0; i < links.size(); i++) {
                String x = links.get(i).attr("href"); // to have the the url only not the full line
                if (!x.startsWith("http")) {
                    x = base + x; // add the base to the url which is in the same site
                }
                validator = new ValidationThread(x, currentDepth + 1, depth); // call the method again with the new depth & new link
                executorService.execute(validator);

            }
        } else {
         //   System.out.println("Invalid : " + link);
            Data.inValidLinks.add(link);
        }
    }

}
