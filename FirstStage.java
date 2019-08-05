///-----------------------------------------------------------------
///   Class:          FirstStage.java
///   Description:    Extends the functionality of Stage to cater specifically
///                   for the first stage in the production line
///   Author:         Darcy Lewis - C3282869        Date: 03/06/19
///-----------------------------------------------------------------
import java.util.*;

public class FirstStage extends Stage {

  public FirstStage(int newMean, double newRange, int newMaxSize)//Constructor 
  {
    outputQueue = new LinkedList<Item>();//Stores items that have finished in current stage
    current = null;
    mean = newMean;
    totalProdTime = 0.0;
    range = newRange;
    stageName = "s0";
    maxSize = newMaxSize;
    blockTime = 0.0;
    totalBlockTime = 0.0;
    starved = false;
    blocked = false;
    starveTime = 0.0;
    totalStarveTime = 0.0;
    outputTimeAt = new double[maxSize+1];
    changeInTimeO = 0.0;
  }

  @Override//From Stage
  public boolean isStarved()
  {
    return false;//First stage never starves
  }

  @Override//From Stage
  public void setStarveTime(double currentTime)//Not used in first stage
  {
    throw new UnsupportedOperationException();
  }

  @Override//From Stage
  public void setStarveTimeFin(double currentTime)//Not used in first stage
  {
    throw new UnsupportedOperationException();
  }

  @Override//From Stage
  public void calcTotalStarveTime(double time)//Not used in first stage
  {
    throw new UnsupportedOperationException();
  }

  @Override//From Stage
  public double getTotalStarveTime()
  {
    return 0.0;//Always zero since first stage never starves
  }

  @Override//From Stage
  public Event processItem(double currentTime) //Create item, put in output, create event from stats and returns event
  {
    if (!finished(currentTime)) //check if finished processing 
    {
      return null;
    }
    if (outputQueue.size()>=maxSize) //check if blocked
    {
      blocked = true;
      setBlockTime(currentTime);
      return null;
    } 
    else
    {
      if (blocked)
      {
        blocked = false;
        setBlockTimeFin(currentTime);
      }
    }
    current = new Item(); //creates item
    outputQueue.add(current); //process item
    updateOutputTimeAt(currentTime, outputQueue.size());//update time a queue is at a particular size
    current.setStage(this);//Set current items current stage to this stage=
    Event newEvent = new Event();
    newEvent.setStartTime(currentTime);//Set production start time
    double prodTime = calcProductionTime(currentTime);//Find production time
    timeDone = currentTime+prodTime;//Calc time item finishes prod in this stage
    current.setTimeInQueue(stageName, prodTime);
    newEvent.setEndTime(timeDone);
    newEvent.setStage(this);
    return newEvent;
  }

  public Queue<Item> getQueue()//Return queue storing output of stage
  {
    return outputQueue;
  }

}
