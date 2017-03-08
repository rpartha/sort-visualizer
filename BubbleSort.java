import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import javax.swing.JComponent;
/**
   This class sorts an array, using the bubble sort
   algorithm.
*/
public class BubbleSort
{
   /**
      Constructs a bubble sorter.
      @param anArray the array to sort
      @param aComponent the component to be repainted when the animation
      pauses
   */
   public BubbleSort(int[] anArray, JComponent aComponent)
   {
      a = anArray;
      sortStateLock = new ReentrantLock();
      component = aComponent;
   }

   /**
      Sorts the array managed by this bubble sorter.
   */
   public void sort()
         throws InterruptedException
   {
      for (int i = 0; i < a.length - 1; i++)
      {

    	  for(int j = 0; j < a.length-1-i; j++)
    	  {
    		  sortStateLock.lock();
    		  try
    		  {

    				if(a[j] > a[j+1])
    				{
    					swap(j, j+1);
       				 	// For animation
       					alreadySorted = i;
       				}
    				 markedPosition = j;


    		  }

    		  finally
    		  {
    			  sortStateLock.unlock();
    		  }
    		  pause(2);

    	  	}
    	}
   }

   /**
      Swaps two entries of the array.
      @param i the first position to swap
      @param j the second position to swap
   */
   private void swap(int i, int j)
   {
      int temp = a[i];
      a[i] = a[j];
      a[j] = temp;
   }

   /**
      Draws the current state of the sorting algorithm.
      @param g2 the graphics context
   */
   public void draw(Graphics2D g2)
   {
      sortStateLock.lock();
      try
      {
         int deltaX = component.getWidth() / a.length;
         for (int i = 0; i < a.length; i++)
         {
            if (i == markedPosition)
               g2.setColor(Color.RED);
            else if (i <= alreadySorted)
               g2.setColor(Color.BLUE);
            else
               g2.setColor(Color.BLACK);
            g2.draw(new Line2D.Double(i * deltaX, 0,
                  i * deltaX, a[i]));
         }
      }
      finally
      {
         sortStateLock.unlock();
      }
   }

   /**
      Pauses the animation.
      @param steps the number of steps to pause
   */
   public void pause(int steps)
         throws InterruptedException
   {
      component.repaint();
      Thread.sleep(steps * DELAY);
   }

   private int[] a;
   private Lock sortStateLock;

   // The component is repainted when the animation is paused
   private JComponent component;

   // These fields are needed for drawing
   private int markedPosition = -1;
   private int alreadySorted = -1;

   private static final int DELAY = 100;
}
