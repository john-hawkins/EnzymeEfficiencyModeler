package org.enzymes;

import java.util.Calendar;

/**
 * A class that acts as a store for the request
 * details for administration.
 */
public class WebRequest {

    private String address;
    private Calendar date;
    private int seqs;

    public WebRequest(String a, Calendar d, int s) {
        address = a;
        date = d;
        seqs = s;
    }
    
    public String getAddress() {
       return address;
    }
    
    public Calendar getDate() {
        return date;
     }
    
    public int getSeqs(){
        return seqs;
    }
}
