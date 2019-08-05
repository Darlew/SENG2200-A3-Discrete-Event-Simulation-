///-----------------------------------------------------------------
///   Class:          LastStage.java
///   Description:    Extends the functionality of Stage to cater specifically
///                   for the last stage in the production line
///   Author:         Darcy Lewis - C3282869        Date: 03/06/19
///-----------------------------------------------------------------
import java.util.*;

class LastStage extends Stage {

  public LastStage(Queue<Item> newInputQueue, int newMean, double newRange, int newMaxSize)//constructor
  {
    inputQueue = newInputQueue;//Output from previous stage becomes final stage input
    outputQueue = new LinkedList<Item>();
    current = null;
    mean = newMean;
    totalProdTime = 0.0;
    range = newRange;
    stageName = "s5";
    blockTime = 0.0;
    totalBlockTime = 0.0;
    starved = false;
    blocked = false;
    starveTime = 0.0;
    totalStarveTime = 0.0;
    inputTimeAt = new double[newMaxSize+1];
    changeInTimeI = 0.0;
    outputTimeAt = new double[20];
    changeInTimeO = 0.0;
  }

  @Override
  public Event processItem(double currentTime) //Create item, put in output, create event from stats and returns event
  {
    if (!finished(currentTime))//If finished
    {
      return null;
    }
    if (inputQueue.isEmpty())//If not blocked
    {
      starved = true;
      setStarveTime(currentTime);
      return null;
    } 
    else 
    {
      if (starved)//If starved
      {
        starved = false;
        setStarveTimeFin(currentTime);
      }
    }
    updateInputTimeAt(currentTime, inputQueue.size()); //update times for sizes of input queue
    current = inputQueue.remove(); //take item from input queue
    outputQueue.add(current); //put it in outputQueue
    current.setStage(this); //set items current stage
    Event newEvent = new Event();
    newEvent.setStartTime(currentTime); //set production start time
    double prodTime = calcProductionTime(currentTime);//Find production time
    timeDone = currentTime+prodTime;//Find time item finishes in stage 
    current.setTimeInQueue(stageName, prodTime); //set time spent in stage
    newEvent.setEndTime(timeDone); //set time item exits production in an event
    newEvent.setStage(this); //Insert current stage in event
    return newEvent;
  }

  @Override
  public boolean isBlocked()//Return block status
  {
    return false;//Always false cause last stage never blocks
  }

  @Override
  public void updateOutputTimeAt(double currentTime, int size) //for outputQueue, not used in last stage
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setBlockTime(double currentTime)//Not used in final stage
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setBlockTimeFin(double currentTime)//Not used in final stage
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void calcTotalBlockTime(double time)//Not used in final stage
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public double getTotalBlockTime()//Return total block time
  {
    return 0.0;//Always zero as final stage never blocks
  }

  public Queue<Item> getOutputQueue()//Returns output queue
  {
    return outputQueue;
  }

}
