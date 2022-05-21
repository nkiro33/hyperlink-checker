/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BackEnd;


/**
 *
 * @author 20122
 */
public class ValidationThread extends Thread {
    private String link;
    private int currentDepth;
    private int depth;


    public ValidationThread(String link, int currentDepth, int depth) {
        this.link = link;
        this.currentDepth = currentDepth;
        this.depth = depth;
    }
    
    @Override
    public void run() {
        LinkValidator.validateURL(link, currentDepth, depth);
        
    }
    
}
