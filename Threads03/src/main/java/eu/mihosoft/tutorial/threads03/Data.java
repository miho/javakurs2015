/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.mihosoft.tutorial.threads03;

/**
 * Simple data class that is not threadsafe.
 * 
 * @author Michael Hoffer &lt;info@michaelhoffer.de&gt;
 */
public class Data {

    private int value;

    public void reset() {
        value = 0;
    }

    public  void setValue(int value) {
        this.value = value;
    }

    /**
     * @return the counter
     */
    public  int getValue() {
        return value;
    }
}
