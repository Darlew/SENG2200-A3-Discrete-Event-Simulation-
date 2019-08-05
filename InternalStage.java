///-----------------------------------------------------------------
///   Class:          InternalStage.java
///   Description:    Extends the functionality of Stage to cater specifically
///                   for the middle stages in the production line
///   Author:         Darcy Lewis - C3282869        Date: 03/06/19
///-----------------------------------------------------------------
import java.util.*;

public class InternalStage extends Stage {

  public InternalStage(Queue<Item> newInputQueue, String newStageName, int newMean, double newRange, int newMaxSize)//Constructor
  {
    inputQueue = newInputQueue;
    outputQueue = new LinkedList<Item>();
    stageName = newStageName;
    mean = newMean;
    totalProdTime = 0.0;
    range = newRange;
    current = null;
    maxSize = newMaxSize;
    blockTime = 0.0;
    totalBlockTime = 0.0;
    starved = false;
    blocked = false;
    starveTime = 0.0;
    totalStarveTime = 0.0;
    inputTimeAt = new double[maxSize+1];
    changeInTimeI = 0.0;
    outputTimeAt = new double[maxSize+1];
    changeInTimeO = 0.0;
  }

  public Queue<Item> getOutputQueue()//Return outputQueue
  {
    return outputQueue;
  }

  public Queue<Item> getInputQueue()//Return inputQueue
  {
    return inputQueue;
  }

  public void setOutputQueue(Queue<Item> newQueue)//Set outputQueue
  {
    outputQueue = newQueue;
  }

}
