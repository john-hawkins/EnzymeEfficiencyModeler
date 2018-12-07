package org.enzymes;

import java.util.*;

/**
 * A task that executes regularly to remove expired requests.
 * 
 * @author John Hawkins
 */
public class RequestDeleter extends TimerTask{
    // the delay between executions of this task (in milliseconds)
    public static final long runDelay = 1000 * 60 * 60;
    
    // the time of the last execution
    private long lastRun  = System.currentTimeMillis();
    
    public void run(){
        Request.deleteExpired();
        lastRun = System.currentTimeMillis();
        System.gc();
    }
    
    /**
     * Returns the time of the last execution (in System.currentTimeMillis() format),
     * or the startup time if it has never executed
     */
    public long getLastRunTime(){
        return lastRun;
    }
}
