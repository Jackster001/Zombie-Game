import javax.swing.JOptionPane;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.applet.*;

public class Game
{
  private Grid grid;
  private int userRow;
  private int userCol;
  private int msElapsed;
  private int timesAvoid;
  private int score;
  private int numArrows;
  private boolean Over;
  private AudioClip song;
  public Game()
  {
    grid = new Grid(5, 10);
    score=0;
    userRow = 1;
    userCol=0;
    msElapsed = 0;
    numArrows = 7;
    timesAvoid = 0;
    Over=false;
    song=Applet.newAudioClip(Game.class.getResource("file0.wav"));

    updateTitle();
    grid.setImage(new Location(userRow, userCol), "crysis.gif");
   
  }
    public void reset(boolean a)
  {
      if(a)
      {
          score=0;
          timesAvoid=0;
          numArrows=0;
          
          for(int i=0;i<5;i++)
          {
              for(int j=0;j<10;j++)
              {
                  String image=grid.getImage(new Location(i,j));
                  if(image!=null)
                  {
                      if(image.equals("crysis.gif")==false)
                      {
                          grid.setImage(new Location(i,j),null);
                      }
                  }
              }
          }
          play();
      }
  }
  public void play()
  {
    song.loop();
    JOptionPane.showMessageDialog(null,"Welcome Hunter,\nThe world is infected with a virus that brings the dead\nBasically they are zombies. "
            + "\nAnd your objective is to kill as many as you can"
            + "\nInstructions: use your arrow keys to move up, down, left and right"
            + "\nUse the space bar to shoot an arrow"
            + "\nBe careful, if you get bittien 3 time you become one of them"
            + "\nPlease note that you only have 7 arrows \nand you need to collect them as you run through the game"
            + "\n*Objective:Kills as many zombies as you can!"
            + "\nGood Luck....because you need it....");
    boolean GameOver=false;
    while (GameOver!=true)
    {
       
      grid.pause(100);
      handleKeyPress();
      if (msElapsed % 300 == 0)
      {scrollRight();
        scrollLeft();
        
        populateRightEdge();
      }
      updateTitle();
      msElapsed += 100;
          if(timesAvoid==3)
        {
            GameOver=true;
            JOptionPane.showMessageDialog (null, "Game Over! You turned..... and your score is "+score);
            int n=JOptionPane.showConfirmDialog(null,"Wanna Play Again?\nclick yes to inject a vaccine\nThe zombies can't kill themselves!", "Press yes or no",JOptionPane.YES_NO_OPTION);
            if(n==0)
            {
                Over=true;
                reset(Over);
                
            }
            else
            {
                JOptionPane.showMessageDialog(null, "Have a nice day you coward!!");
            }
        }
    }

  }
  
  public void handleKeyPress()
  {
      int key = grid.checkLastKeyPressed();
      if(userRow==0&&key==38||userRow==grid.getNumRows()-1&&key==40)
      {
      }
      else
      {
          if(key==38)
          {
              handleCollision(new Location(userRow-1,userCol));
              grid.setImage(new Location(userRow, userCol), null);
              userRow--;
              grid.setImage(new Location(userRow, userCol), "crysis.gif");
              
          }
          if(key==40)
          {
              handleCollision(new Location(userRow+1,userCol));
              grid.setImage(new Location(userRow, userCol), null);
              userRow++;
              grid.setImage(new Location(userRow, userCol), "crysis.gif");
          }
          if(key==37&&userCol>0)
          {
              String image=grid.getImage(new Location(userRow,userCol-1));
              if(image!=null)
              {
                  if(image.equals("zombie.gif"))
                  {
                      timesAvoid++;
                  }
                  if(image.equals("Arrow.gif"))
                  {
                      numArrows++;
                  }
              }
              grid.setImage(new Location(userRow,userCol),null);
              userCol=userCol-1;
              grid.setImage(new Location(userRow,userCol),"crysis.gif");
          }
          if(key==39&&userCol<9)
          {
              handleCollision(new Location(userRow,userCol+1));
              grid.setImage(new Location(userRow,userCol),null);
              userCol=userCol+1;
              grid.setImage(new Location(userRow,userCol),"Crysis.gif");
          }
          if(numArrows>0)
          {
            if(key==32)
            {
                handleCollision(new Location(userRow,userCol));
                numArrows--;
                grid.setImage(new Location(userRow, userCol+1),"arrowFired.gif");
            }
          }
      }
      
  }
  
  public void populateRightEdge()
  {
      double a=Math.random()*15;
      double b=Math.random()*5;
      double a2=Math.random()*5;
      double b2=Math.random()*5;
      double c=Math.random()*5;
      int ranArr=(int)a;
      int ranRow=(int)b;
      int ranRow2=(int)a2;
      int ranRow3=(int)b2;
      int ranRow4=(int)c;
      if(ranArr>12)
      {
          grid.setImage(new Location(ranRow2,grid.getNumCols()-1),"Arrow.gif");
      }
      if(score<10)
      {
          if(ranArr>5)
          {
            grid.setImage(new Location(ranRow,grid.getNumCols()-1),"zombie.gif");
          }
      }
      else if(score>20)
      {
          grid.setImage(new Location(ranRow,grid.getNumCols()-1),"zombie.gif");
          grid.setImage(new Location(ranRow3,grid.getNumCols()-1),"zombie.gif");
          if(ranArr>7)
          grid.setImage(new Location(ranRow4, grid.getNumCols()-1),"Arrow.gif");
      }
      else
      {
          grid.setImage(new Location(ranRow,grid.getNumCols()-1),"zombie.gif");
      }
      
  }
  
  public void scrollLeft()
  {
      for(int i=0;i<5;i++)
      {
          for(int j=0;j<10;j++)
          {
              Location loc=new Location(i,j);
              if((j>=1))
                handleShot(loc);
                String image=grid.getImage(loc);
                Location newLoc=new Location(i,j-1);
                if(image!=null)
                {
                    if(i==userRow&&j==userCol+1)
                        handleCollision(new Location(userRow,userCol+1));
                 
                    else if(image.equals("zombie.gif"))
                    {
                        if(j!=0)
                        {
                            
                            grid.setImage(newLoc,image);
                        }
                        grid.setImage(loc, null);
                    }
                    else if(image.equals("Arrow.gif"))
                    {
                        if(j!=0)
                        {
                            grid.setImage(newLoc,image);
                        }
                        grid.setImage(loc, null);
                    }
                }  
          }
      }
  }  
  public void handleCollision(Location loc)
  {
      String image=grid.getImage(loc);
      Location newLoc=new Location(loc.getRow(),loc.getCol()-1);
      if(image!=null)
      {

        if("zombie.gif".equals(image))
        {
            timesAvoid++;
            grid.setImage(loc,null);
            if(loc.getCol()-1>=0&&grid.getImage(newLoc)!=null&&grid.getImage(newLoc).equals("arrowFired.gif"))
                            {
                                
                                grid.setImage(newLoc, "Arrow.gif");
                                grid.setImage(loc,null);
                            }
        }
        if("Arrow.gif".equals(image))
        {
            numArrows++;
            grid.setImage(loc,null);
        }
        
      }
  }
  public boolean handleShot(Location loc)
  {
      boolean result=false;
      String image=grid.getImage(loc);
      String image2=grid.getImage(new Location(loc.getRow(),loc.getCol()-1));
      if(image!=null&&image2!=null)
      {
          if("zombie.gif".equals(image)&&"arrowFired.gif".equals(image2))
          {
              score++;
              grid.setImage(loc,null);
              grid.setImage(new Location(loc.getRow(),loc.getCol()-1),null);
              result=true;
          }
          if("Arrow.gif".equals(image)&&"arrowFired.gif".equals(image2))
          {
              numArrows++;
              grid.setImage(loc,null);
              grid.setImage(new Location(loc.getRow(),loc.getCol()-1),null);
              result=true;
          }
      }
      return result;
  }
  public void scrollRight()
  {
      for(int i=4;i>=0;i--)
      {
          for(int j=9;j>0;j--)
          {
              Location loc=new Location(i,j);
              String image=grid.getImage(loc);
              
              if(image!=null)
              {
                  if(image.equals("zombie.gif"))
                  {
                      handleShot(loc);
                  }
                  if(image.equals("arrowFired.gif"))
                  {
                      
                      if(j+1!=10)
                      {
                        Location newLoc=new Location(i,j+1);
                        String image2=grid.getImage(newLoc);
                        if(image2!=null&&image2.equals("zombie.gif"))
                        {
                            score++;
                            grid.setImage(newLoc,null);
                            grid.setImage(loc, null);
                        }
                        else
                        {
                            grid.setImage(newLoc,image);
                            grid.setImage(loc, null);
                        }
                      }
                      else
                          grid.setImage(loc, null);
                  }
                
              }
          }
      }
  }
  public int getScore()
  {
      
      return score;
  }
  
  public void updateTitle()
  {
      grid.setTitle("Zombie Hunter:  " +score+"                   Arrows: "+numArrows+"             "+"         Bitten:"+timesAvoid);
      
  } 
  public static void test()
  {
    Game game = new Game();
    game.play();
  }
  public static void main(String[] args)
  {
      Game.test();

  }
}
