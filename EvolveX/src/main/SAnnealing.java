package main;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import util.Constants;
import algorithms.simulatedannealing.RunSA;
import exceptions.InitializationException;

public class SAnnealing extends Start{

	public SAnnealing() 
	{
		
	}

	@Override
	public String toString() 
	{
		 return "Simulated Annealing";
	}
   
 /** 
  * Creates a new instance of Run 
  * @throws IOException 
  * @throws ExecutionException 
  * @throws InterruptedException 
  */
   public SAnnealing(String[] arguments) throws IOException, InterruptedException, ExecutionException 
   {
       
       try 
       {
           for(int f=1;f<arguments.length;f++)
           {  
        	  /* Start Evolve time */
              start = System.currentTimeMillis();
             	
           	  propertiesFilePath = arguments[f].toString().length()>1?arguments[f]:Constants.DEFAULT_PROPERTIES;
           	  //AntColonyOptimisation antColonyOptimization = new AntColonyOptimisation(arguments[1],this.setup());
           	  new RunSA(this.setup());
        	   
           	 /* Start Evolve time */
          	  end = System.currentTimeMillis();
           }
           /* Start Evolve time */
       	   sysEndTime = System.currentTimeMillis();
           
		} 
       catch (InitializationException e) 
       {
			e.printStackTrace();
		}
       
   }
  
   
   
   public static void main(String[] args) throws IOException, InterruptedException, ExecutionException 
   {
       new SAnnealing(args);
   }
   
   
}
