package com.dbr.game;
import java.util.concurrent.TimeUnit;

public class Timer {

    Timer(){};
    
    public void startTimer(){
        long lStartTime = System.nanoTime();
    };

    public void endTimer(){
       long lEndTime = System.nanoTime();
    };


}
