///-----------------------------------------------------------------
///   Class:          Event.java
///   Description:    Stores the time an object enters and exits a stage of production 
///   Author:         Darcy Lewis - C3282869        Date: 03/06/19
///-----------------------------------------------------------------
public class Event implements Comparable<Event>{
  private double startTime;//Start time of production
  private double endTime;//End time of production
  private Stage stage;//Stage event occurs at

  public Event()//Constructor
  {
    startTime = 0.0;
    endTime = 0.0;
    stage = null;
  }

  public void setStartTime(double newStartTime)//Set start time 
  {
    startTime = newStartTime;
  }

  public void setEndTime(double newEndTime)//Set end time
  {
    endTime = newEndTime;
  }

  public void setStage(Stage newStage)//Set stage
  {
    stage = newStage;
  }

  public Stage getStage()//Return stage
  {
    return stage;
  }

  public double getStartTime()//Return startTime
  {
    return startTime;
  }

  public double getEndTime()//Return endTime
  {
    return endTime;
  }

  @Override
  public int compareTo(Event event2)//For comparing events
  {
    if (endTime > event2.getEndTime()) 
    {
      return 1;
    } 
    else if (endTime < event2.getEndTime()) 
    {
      return -1;
    } 
    else 
    {
      return 0;
    }
  }
}
