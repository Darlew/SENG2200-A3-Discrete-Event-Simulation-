///-----------------------------------------------------------------
///   Class:          PA3.java
///   Description:    Initialises program, instatiates stages, runs program and prints results.
///   Author:         Darcy Lewis - C3282869        Date: 03/06/19
///-----------------------------------------------------------------
import java.util.*;
import java.io.*;

public class PA3{
  private int stageNo;//Identifier of what index is to be accessed in stageList
  private Queue<Event> eventQueue;//Stores events
  private Stage[] stageList;//Stores stages
  private Stage s0, s1, s2a, s2b, s3, s4a, s4b, s5;//Initialise stages

  public void run(int mean, int size, double range)
  {
    if (size<0)
    {
      System.out.println("Negative size not permitted!");
      return;
    }
    stageList = new Stage[8]; //Stores stages
    eventQueue = new PriorityQueue<Event>();
    double currentTime = 0.0;//Set current time to 0
    double maxTime = 10000000.0;//Set max time to 10,000,000
    Event newEvent = null;//initialise event object to store current event while being processed
    Stage stage = null;//Stores stage current event occured at

    s0 = new FirstStage(mean, range, size);//Instantiate stage 0
    Queue<Item> newQueue = ((FirstStage)s0).getQueue();//Create output queue for stae 0
    s1 = new InternalStage(newQueue, "s1", mean, range, size); //Instantiate stage 1 with stage 0 output as input
    newQueue = ((InternalStage)s1).getOutputQueue();//Create output queue for stage 1
    s2a = new InternalStage(newQueue, "s2a", (2*mean), (2*range), size);//Instantiate stage 2a with stage 1 output as input
    s2b = new InternalStage(newQueue, "s2b", mean, (range*0.5), size);//Instantiate stage 2b with stage 1 output as input
    newQueue = ((InternalStage)s2a).getOutputQueue(); //Create shared output for stage 2a and 2b
    ((InternalStage)s2b).setOutputQueue(newQueue);
    s3 = new InternalStage(newQueue, "s3", mean, range, size);////Instantiate stage 3 with stage 2a/b output as input
    newQueue = ((InternalStage)s3).getOutputQueue();//Create output queue for stage 3
    s4a = new InternalStage(newQueue, "s4a", mean, (range*0.5), size);//Instantiate stage 4a with stage 3 output as input
    s4b = new InternalStage(newQueue, "s4b", (2*mean), (2*range), size);//Instantiate stage 4b with stage 3 output as input
    newQueue = ((InternalStage)s4a).getOutputQueue(); //Create shared output for stage 4a and 4b
    ((InternalStage)s4b).setOutputQueue(newQueue);
    s5 = new LastStage(newQueue, mean, range, size);//Instantiate stage 5 with stage 4a/b output as input

    //fill stageList with created stages
    stageList[0] = s0;
    stageList[1] = s1;
    stageList[2] = s2a;
    stageList[3] = s2b;
    stageList[4] = s3;
    stageList[5] = s4a;
    stageList[6] = s4b;
    stageList[7] = s5;

    stageNo = 0; //index to get stages from array
    newEvent = stageList[stageNo].processItem(currentTime); //set newEvent to process first item
    eventQueue.add(newEvent); //add event just created to the event queue

    //Send items through production line until time limit reached
    while(currentTime<=maxTime)
    {
      newEvent = eventQueue.remove();
      currentTime = newEvent.getEndTime(); //update currentTime to end time of even in eventQueue
      stage = newEvent.getStage();

      stageNo = getStageNo(stage);
      newEvent = stage.processItem(currentTime);
      if (newEvent != null)
      {
        eventQueue.add(newEvent);
      }
      checkAdjacent(newEvent, stage, currentTime);
    }
    //Max time reached, stop producing items

    //////////////////////////Outputs/////////////////////////////////
    //Q1
    System.out.println("Production Stages:");
    System.out.println("-----------------------------------------------");
    System.out.format("%-10s%5s%15s%15s\n", "Stage:", "Work[%]", "Starve[t]", "Block[t]"); //Labels of columns for Q1
    for(Stage stage2: stageList)//Print all production %, starve time, block time for each stage
    {
      System.out.format("%-10s%5s%15s%15s\n", stage2.getStageName(), String.format("%.2f", (stage2.getProdTime()/maxTime)*100), String.format("%.2f", stage2.getTotalStarveTime()), String.format("%.2f", stage2.getTotalBlockTime()));
    }
    System.out.println();
    //Q2
    System.out.println("Storage Queues:");
    System.out.println("------------------------------ ");
    System.out.format("%-10s%10s%10s\n", "Queue", "AvgTime[t]", "AvgItems");//Labels of columns for Q2
    //Output average time an item was in each queue and average items in each queue 
    System.out.format("%-10s%10s%10s\n", "Q1", String.format("%.2f", getAvgQueueTime(s0)), String.format("%.2f", s0.getAverageSize()));
    System.out.format("%-10s%10s%10s\n", "Q2", String.format("%.2f", getAvgQueueTime(s1)), String.format("%.2f", s1.getAverageSize()));
    System.out.format("%-10s%10s%10s\n", "Q3", String.format("%.2f", getAvgQueueTime(s2a)), String.format("%.2f", s2a.getAverageSize()));
    System.out.format("%-10s%10s%10s\n", "Q4", String.format("%.2f", getAvgQueueTime(s3)), String.format("%.2f", s3.getAverageSize()));
    System.out.format("%-10s%10s%10s\n", "Q5", String.format("%.2f", getAvgQueueTime(s4a)), String.format("%.2f", s4a.getAverageSize()));
    System.out.println();
    //Q3
    //Output each possible path an item can take and the number of items that have taken that path
    System.out.println("Production Paths:");
    System.out.println("------------------");
    System.out.println("S2A -> S4A: " + getPathUsage(1));
    System.out.println("S2B -> S4A: " + getPathUsage(2));
    System.out.println("S2A -> S4B: " + getPathUsage(3));
    System.out.println("S2B -> S4B: " + getPathUsage(4));
  }

  public static void main(String args[])
  {
    int mean, size;
    double range;
    mean = Integer.parseInt(args[0]);
    range = Double.parseDouble(args[1]);
    size = Integer.parseInt(args[2]);
    PA3 pa3 = new PA3();
    pa3.run(mean, size, range);
  }

  public int getStageNo(Stage stage)//Return index of stageList a stage is at
  {
    String stageName = stage.getStageName();
    switch(stageName)
    {
      case "s0":
        return 0;
      case "s1":
        return 1;
      case "s2a":
        return 2;
      case "s2b":
        return 3;
      case "s3":
        return 4;
      case "s4a":
        return 5;
      case "s4b":
        return 6;
      case "s5":
        return 7;
      default:
        return -1;
    }
  }

 
  public void checkAdjacent(Event newEvent, Stage stage, double currentTime)//Check input and output of stage for starvation or blockage
  {
    if (stage==s2a || stage==s4a)//For stage 2a or 4a
    {
      newEvent = stageList[stageNo-1].processItem(currentTime); //Check output of Stage 1 or stage 3
      if (newEvent!=null)//If not starved
      {
        eventQueue.add(newEvent);//add event to event queue
      }
      newEvent = stageList[stageNo+2].processItem(currentTime); //Check input of stage 3 or 5
      if (newEvent!=null)//if not blocked
      {
        eventQueue.add(newEvent);
      }
    } else if (stage==s2b || stage==s4b)//For stage 2b or 4b
    {
      newEvent = stageList[stageNo-2].processItem(currentTime); //1 or 3
      if (newEvent!=null)
      {
        eventQueue.add(newEvent);
      }
      newEvent = stageList[stageNo+1].processItem(currentTime); //3 or 5
      if (newEvent!=null)
      {
        eventQueue.add(newEvent);
      }
    } else if (stage==s1 || stage==s3)//for stage 1 or 3
    {
      newEvent = stageList[stageNo-1].processItem(currentTime); //0 or 2b
      if (newEvent!=null)
      {
        eventQueue.add(newEvent);
      }
      newEvent = stageList[stageNo+1].processItem(currentTime); //2a or 4a
      if (newEvent!=null)
      {
        eventQueue.add(newEvent);
      }
      newEvent = stageList[stageNo+2].processItem(currentTime); //2b or 4b
      if (newEvent!=null)
      {
        eventQueue.add(newEvent);
      }
      if(stage==s3) {
        newEvent = stageList[stageNo-2].processItem(currentTime); //2a
        if (newEvent!=null)
        {
          eventQueue.add(newEvent);
        }
      }
    } else if (stage==s5)//for stage 5
    {
      newEvent = stageList[stageNo-1].processItem(currentTime); //4b
      if (newEvent!=null)
      {
        eventQueue.add(newEvent);
      }
      newEvent = stageList[stageNo-2].processItem(currentTime); //4a
      if (newEvent!=null)
      {
        eventQueue.add(newEvent);
      }
    } else { //s0
      newEvent = stageList[stageNo+1].processItem(currentTime); //1
      if (newEvent!=null)
      {
        eventQueue.add(newEvent);
      }
    }
  }

  public double getAvgQueueTime(Stage stage)//Calculate and return average time spent in queue
  {
    Queue<Item> items = ((LastStage)s5).getOutputQueue();//Get items from output of Stage 5
    double queueTime = 0;
    for(Item it: items)//For each item in items Array
    {
      queueTime = queueTime + it.getTimeInQueue(stage.getStageName());//Add time spent in a queue of item to queueTime
    }
    return (queueTime/items.size());//return average
  }

  public int getNoOfItems(String pathway, Queue<Item> items)//Returns number of items taking a particular stage path
  {
    int noOfItems = 0;
    for(Item it: items)//For each item
    {
      if (it.getStages().equals(pathway))//If item takes specified pathway
      {
        noOfItems++;
      }
    }
    return noOfItems;
  }

  public int getPathUsage(int path)//Returns number of items that have taken the specified producion path
  {
    Queue<Item> items = ((LastStage)s5).getOutputQueue();
    String pathway = "";
    switch(path)
    {
      case 1:
        pathway = "s0 s1 s2a s3 s4a ";
        return getNoOfItems(pathway, items);
      case 2:
        pathway = "s0 s1 s2b s3 s4a ";
        return getNoOfItems(pathway, items);
      case 3:
        pathway = "s0 s1 s2a s3 s4b ";
        return getNoOfItems(pathway, items);
      case 4:
        pathway = "s0 s1 s2b s3 s4b ";
        return getNoOfItems(pathway, items);
      default:
        return -1;
    }
  }
}
