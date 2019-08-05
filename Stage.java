///-----------------------------------------------------------------
///   Class:          Stage.java
///   Description:    Removes object from input queue, computes processing time and moves object to output queue
///   Author:         Darcy Lewis - C3282869        Date: 03/06/19
///-----------------------------------------------------------------
import java.util.*;

public class Stage {
  protected Queue<Item> outputQueue;//queue storing items output from stage
  protected Queue<Item> inputQueue;//queue storing items not yet entered stage
  protected Item current;//Current item in stage
  protected double totalProdTime;//Total time spent producing items
  protected int mean;
  protected double range;
  protected String stageName;//Name of stage
  protected int maxSize;//Maximum size of output queue
  protected double blockTime;//time stage is blocked at
  protected double totalBlockTime;//Total time the stage has been blocked
  protected boolean starved;//if true, stage is starved
  protected boolean blocked;//if true, stage is blocked
  protected double inputTimeAt[];//time the input queue spends at a particular size
  protected double changeInTimeI;//Change in time at input
  protected double outputTimeAt[];//time the output queue spends at a particular size
  protected double changeInTimeO;//Change in time at output
  protected double timeDone;//time at end of production
  protected double starveTime;//time stage is starved at
  protected double totalStarveTime;//Total time spent starving

  public Event processItem(double currentTime) //Takes item from input queue, places in output queue and creates event to store details
  {
    if (!finished(currentTime)) //check if finished
    {
      return null;
    }
    if (inputQueue.isEmpty()) //check if starved 
    {
      starved = true;
      setStarveTime(currentTime);
      return null;
    } 
    else 
    {
      if (starved)
      {
        starved = false;
        setStarveTimeFin(currentTime);
      }
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
    updateInputTimeAt(currentTime, inputQueue.size()); //set times at sizes for inputQueue
    current = inputQueue.remove(); //removes from input
    outputQueue.add(current); //puts into output
    updateOutputTimeAt(currentTime, outputQueue.size()); //set times at sizes for outputQueue
    current.setStage(this); //sets stage in item
    Event newEvent = new Event();
    newEvent.setStartTime(currentTime);
    double prodTime = calcProductionTime(currentTime);
    timeDone = currentTime+prodTime;
    current.setTimeInQueue(stageName, prodTime); //sets time spent in prod in item
    newEvent.setEndTime(timeDone); //sets end time of prod in event
    newEvent.setStage(this); //sets stage in event
    return newEvent;
  }

  public void updateOutputTimeAt(double currentTime, int size) //Updates time outputQueue is at a specified size
  {
    double changeInTime = currentTime - changeInTimeO;
    changeInTimeO = currentTime;
    if (size!=0)
    {
      outputTimeAt[size-1] = outputTimeAt[size-1] + changeInTime;
    }
  }

  public void updateInputTimeAt(double currentTime, int size)//same as above but for inputQueue
  {
    double changeInTime = currentTime - changeInTimeI;
    changeInTimeI = currentTime;
    if (size!=0)
    {
      inputTimeAt[size-1] = inputTimeAt[size-1] + changeInTimeI;
    }
  }

  public double getAverageSize()//calculate average size of output queue and return it
  {
    double avgSize = 0;
    for (int i = 0; i < outputTimeAt.length; i++)
    {
      avgSize = avgSize + (i*outputTimeAt[i]);
    }
    avgSize = avgSize/10000000.0;
    return avgSize;
  }

  public String getStageName()//Return name of stage
  {
    return stageName;
  }

  public boolean finished(double currentTime)//Check whether item is finished its production time
  {
    if(currentTime>=timeDone)
    {
      return true;
    } 
    else 
    {
      return false;
    }
  }

  public double calcProductionTime(double currentTime)//Calculates production time and returns it 
  {
    double prodTime = 0.0;
    Random r = new Random();
    Double d = r.nextDouble();
    prodTime = mean + (range*(d-0.5));
    setTotalProdTime(prodTime);
    return prodTime;
  }

  public void setTotalProdTime(double newTime)//Sets production time of stage
  {
    totalProdTime = totalProdTime + newTime;
  }

  public double getProdTime()//returns total production time of stage
  {
    return totalProdTime;
  }

  public boolean isBlocked()//Returns stage block status
  {
    return blocked;
  }

  public boolean isStarved()//Returns stage starve status
  {
    return starved;
  }

  public void setBlockTime(double currentTime)//Set time to block stage
  {
    blockTime = currentTime;
  }

  public void setBlockTimeFin(double currentTime)//Set time stage unblocks
  {
    double timeSpent = currentTime - blockTime;
    calcTotalBlockTime(timeSpent);
  }

  public void calcTotalBlockTime(double time)//Calculate total block time
  {
    totalBlockTime = totalBlockTime + time;
  }

  public void setStarveTime(double currentTime)//Set time stage starved at
  {
    starveTime = currentTime;
  }

  public void setStarveTimeFin(double currentTime)//Set time stage unstarved at
  {
    double timeSpent = currentTime - starveTime;
    calcTotalStarveTime(timeSpent);
  }

  public void calcTotalStarveTime(double time)//Calculate total starve time
  {
    totalStarveTime = totalStarveTime + time;
  }

  public double getTotalStarveTime()//Get total time stage is starved
  {
    return totalStarveTime;
  }

  public double getTotalBlockTime()//Return total time stage blocked for
  {
    return totalBlockTime;
  }
}
