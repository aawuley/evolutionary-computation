/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package individuals.representation.hybrid;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Properties;
import java.util.Random;

import individuals.Chromosome;
import individuals.Gene;
import individuals.Representation;
import util.random.MersenneTwisterFast;
import util.random.RandomGenerator;



/**
 *
 * @author anthony
 * note chromosome is started from index 2
 * this was because index one is used as depot
 */
public class TSP  extends Representation implements Gene{
    
	   //private ArrayList<Integer> defaultChromeContainer = new ArrayList<>();
	

	   @Override
	   public void generateChromosome(Chromosome ch,Properties prop) 
	   {
		  //limit repeated call to function, since its unecessary
		  if(this.chromosome.isEmpty())
		  {
			  setInitChrome(ch.getChromosomeSize());
		  }
		  //shuffle content of chromosome
		  this.rng = new MersenneTwisterFast();
		  Collections.shuffle(this.chromosome, new Random(this.rng.nextLong()));
	      ch.setChromosome(this.chromosome);
	    }
	
	   /**
	    * NOTE: the start of chromosome from index 2
	    */
	   public void setInitChrome(int chromeSize)
	   {
		   this.chromosome.clear();
		   //begin count from 1, since 0 is used as depot
		   for(int i=2;i<=chromeSize;i++)
	       {  
	    	  this.chromosome.add(i); 
	       }
	   }
	
	   /**
	    * 
	    * @param min
	    * @param max
		* @return
		*/
		public static int geneRepresentation(int min, int max)
		{
		    return RandomGenerator.getMultiThreadedRandNumber(min,max);
		}

		@Override
		public void sort() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void addAll(ArrayList<Gene> immigrants) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void clear() {
			// TODO Auto-generated method stub
			
		}
		
		 /**
	     * @throws CloneNotSupportedException 
	     * 
	     */
	    public Gene clone() throws CloneNotSupportedException
	    {
	    	return (Gene) super.clone();
	    }

	    
}
