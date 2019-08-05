///-----------------------------------------------------------------
///   Class:          Item.java
///   Description:    The items being processed by the production line. 
///                   Stores time spent in queues and the stages visited.
///   Author:         Darcy Lewis - C3282869        Date: 03/06/19
///-----------------------------------------------------------------
public class Item {
  private double[] timeInQueue; //time spent in queues
  private Stage[] stages;//Stages visited

  public Item()
  {
    timeInQueue = new double[5];
    stages = new Stage[5];
    for(int i = 0; i < stages.length; i++) //initialise array of stages visited
    {
      stages[i] = null;
    }
    for(int i = 0; i < timeInQueue.length; i++) //initialise array of time spent in queues
    {
      timeInQueue[i] = 0.0;
    }
  }

  
  public void setTimeInQueue(String stage, double time) //Takes stage name and time spent in a stage and inserts time into appropriate index
  {
    switch(stage)
    {
      case "s0":
        timeInQueue[0] = time;
        return;
      case "s1":
        timeInQueue[1] = time;
        return;
      case "s2a":
        timeInQueue[2] = time;
        return;
      case "s2b":
        timeInQueue[2] = time;
        return;
      case "s3":
        timeInQueue[3] = time;
        return;
      case "s4a":
        timeInQueue[4] = time;
        return;
      case "s4b":
        timeInQueue[4] = time;
        return;
      default:
        return;
    }
  }

  public void setStage(Stage stage)
  {
    for (int i =0; i<5; i++)
    {
      if (stages[i] == null)
      {
        stages[i] = stage;
        return;
      }
    }
  }

  public String getStages()//Convert stages array into a string and returns it
  {
    String stageNames = "";
    for (int i =0; i<stages.length; i++)
    {
      stageNames = stageNames + stages[i].getStageName() + " ";
    }
    return stageNames;
  }

  public double getTimeInQueue(String stage)//Returns time spent in the queue specified by input stage
  {
    switch(stage)
    {
      case "s0":
        return timeInQueue[0];
      case "s1":
        return timeInQueue[1];
      case "s2a":
        return timeInQueue[2];
      case "s2b":
        return timeInQueue[2];
      case "s3":
        return timeInQueue[3];
      case "s4a":
        return timeInQueue[4];
      case "s4b":
        return timeInQueue[4];
      default:
        return -1;
    }
  }

}
