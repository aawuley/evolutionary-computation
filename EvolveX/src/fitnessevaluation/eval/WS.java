package fitnessevaluation.eval;

import fitnessevaluation.singleobjective.WeightedSum;
import individuals.Chromosome;
import individuals.Individual;
import individuals.populations.Population;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Properties;
import util.Constants;
import util.Point;
import util.random.RandomGenerator;

public class WS extends WeightedSum{

	public WS() 
	{
		// TODO Auto-generated constructor stub
	}
	
	
    
	
    /**
     * prepare data(population) in format accepted for SOR processing e.g.
     * 2,1000 
     * 4,600  
     * 7,800 
     * 8,400  
     * 9,500  
     * @param pop
     * @param swap : specify order of reordering fitness (true-car,route, false route,car)
     * @return
     */
    protected ArrayList<String> changePopFormatForMOP(Population pop,Properties p,String seperator)
    {
 	   ArrayList<String> sorData = new ArrayList<>();
 	   for( int i=0; i< pop.size(); i++)
	   {
 		   ArrayList<Double> vrp = timeWindowEvaluations(pop.get(i),p);
 		   sorData.add(vrp.get(0)+seperator+vrp.get(1)+"");
 		  //sorData.add(this.sumDistanceVRP(pop.get(i),p)+","+this.sumCars(pop.get(i),p)+"");
 		  //sorData.add(this.sumCars(pop.get(i),p)+seperator+this.sumDistanceVRP(pop.get(i),p)+"");
	   }
 	   
	  return sorData;
 	   
    }
    
    
    protected ArrayList<Integer> changePopFormatForMOPInt(Population pop,Properties p)
    {
 	   ArrayList<Integer> sorData = new ArrayList<>();
 	   
 	   for( int i=0; i< pop.size(); i++)
	   {
 		  //sorData.add(this.sumDistanceVRP(pop.get(i),p)+","+this.sumCars(pop.get(i),p)+"");
 		  sorData.add(Integer.parseInt(this.sumCars(pop.get(i),p)+""+this.sumDistanceVRP(pop.get(i),p)));
	   }
 	   
	  return sorData;
 	   
    }

	
    /**
     * mock fitness - sum chromosomes
     * 
     * @param c
     * @return 
     * @deprecated designed for VRP Dataset. see TSP class for newer version
     */
    public double sumDistanceTSP(Chromosome c)
    {
    	/*
    	 * node.1.x         = 40.00
         * node.1.y         = 50.00
    	 */
    	String[] cord1 = new String[6];
    	String[] cord2 = new String[6];
    	
    	
    	/*
    	 * for each customer indexes cord are distributed as below
    	 * CUST NO.   XCOORD.    YCOORD.    DEMAND   READY TIME   DUE DATE   SERVICE TIME
    	 *              0          1           2         3            4            5
    	 */
    	
    	int j = 0;
        int sum = 0;
        //System.out.println("*********************************************");
        for(int i=0; i<c.getChromosome().size();i++)
        {
        	
        	cord1  = this.getProperties().getProperty(Constants.CO_ORDINATES+"."+c.getChromosome().get(i)).split("\\s{1,}");
        	Point p1 = new Point(Double.parseDouble(cord1[0]),Double.parseDouble(cord1[1]));
        	//when genes are exhausted, loop back j to one
        	//so cycle completes e.g. 2-4-5--1-3-2, 4-2-5-3-1-4 ... etc
        	j = (i==c.getChromosome().size()-1)?0:i+1;
        	cord2  = this.getProperties().getProperty(Constants.CO_ORDINATES+"."+c.getChromosome().get(j)).split("\\s{1,}");
        	Point p2 = new Point(Double.parseDouble(cord2[0]),Double.parseDouble(cord2[1]));
        	
        	//System.out.println(c.getChromosome() + "\n#sum "+p1.cartesianDistance(p2));
           sum += p1.calculateEuclidianDistance(p2);
         }
        
         return sum;
         
   }
    
    
   /**
    * 
    * @param c chromosome
    * @param p propertties file
    * @return
    */
    @SuppressWarnings({ "unchecked", "unused" })
	public ArrayList<Double> timeWindowEvaluations(Chromosome c,Properties p)
    {

    	long startTime = getCurrentTime();
    	
    	/*
    	 * node.1.x         = 40.00
         * node.1.y         = 50.00
    	 */
    	String[] cord0,cordz,corda,cordi0,cordi1= new String[6];
    	String[] params = new String[6];
    	ArrayList<Double>  vrptw = new ArrayList<>();
    	ArrayList<Integer> chrom = (ArrayList<Integer>) c.getChromosome().clone();
    	//int lastLocation = 0;
    	double totalDistance = 0;
     	int vCount = 1;
     	
    	/*
    	 * for each customer indexes cord are distributed as below
    	 * CUST NO.   XCOORD.    YCOORD.    DEMAND   READY TIME   DUE DATE   SERVICE TIME
    	 *              0          1           2         3            4            5
    	 */
     	
 	    int vCapacity = Integer.parseInt(p.getProperty(Constants.VEHICLE_CAPACITY));
 	    cord0    = p.getProperty(Constants.CO_ORDINATES+".1").split("\\s{1,}"); //get depo cordinates
   	    Point p0 = new Point(Double.parseDouble(cord0[0]),Double.parseDouble(cord0[1]));
   	    //Point pz = p0; //initialize end point to start point
   	    Point pi1 = p0;  //initialize end pi1 to start point
   	    
 	    while(chrom.size()>0) //enter loop while there exists customers
 	    {
 	    	double sumOfDemand   = 0;
 	    	double distanceTime  = 0;
 	    	int    skippedCount  = 0;
 	 	    
 	 	    /**
 	 	     * sort chromosome by
 	 	     * chrom = sortChromosomeByDistance(chrom,p);
 	 	     */
 	 	     //System.out.println("#Caught"+ chrom.size());
 	    	corda    = p.getProperty(Constants.CO_ORDINATES+"."+(chrom.get(0)+1)).split("\\s{1,}"); //get first cordinate in chromosome
 	   	    Point pa = new Point(Double.parseDouble(corda[0]),Double.parseDouble(corda[1]));
 	   	    
 	     	totalDistance += p0.calculateEuclidianDistance(pa); //distance from depot to first coordinate
            //distanceTime  = p0.cartesianDistance(pa); //time from depot to first coordinate 
 	     	
 	     	//sort chromosome
 	     	//chrom = sortChromosomeByDistance(chrom,p);
 	     	//chrom = swapRandomIndex(chrom);
 	     	
 	 	    for(int i=0; i<chrom.size();i++)
 	        {  
 	        	params        = p.getProperty(Constants.CO_ORDINATES+"."+(chrom.get(i)+1)).split("\\s{1,}");
 		    	double demand = Double.parseDouble(params[2]); 
 		    	
 		    	cordi0     = p.getProperty(Constants.CO_ORDINATES+"."+(chrom.get(i)+1)).split("\\s{1,}"); //get current cordinate
 	            Point pi0 = new Point(Double.parseDouble(cordi0[0]),Double.parseDouble(cordi0[1]));
 	            
 	            /**
 	             * customers are added only when they rightfully fall within the given time window. 
 	             * (current time + service time is less than closing time)
 	             * (distanceTime+Double.parseDouble(cordi0[5]))
 	             */
 	            if( ((distanceTime) < Double.parseDouble(cordi0[4])) && ((sumOfDemand+demand) < vCapacity) ) //compare current distance-time to due date
 	    	    { 	
 	                int j = (i-skippedCount-1)>0?i-skippedCount-1:0; //estimate the last point that was added
 	 		    	cordi1      = p.getProperty(Constants.CO_ORDINATES+"."+(chrom.get(j)+1)).split("\\s{1,}"); //get previously visited cordinate
 	 	            pi1 = new Point(Double.parseDouble(cordi1[0]),Double.parseDouble(cordi1[1]));
 	 	            
 	 	            totalDistance += pi0.calculateEuclidianDistance(pi1);
 	 	            distanceTime  += pi0.calculateEuclidianDistance(pi1); //estimate time traveled so far by adding cartesian distance from visited nodes
 	 	            
 	 	            if(distanceTime <= Double.parseDouble(cordi0[3]))
 	 	            {
 	 	            	distanceTime = Double.parseDouble(cordi0[3]) + Double.parseDouble(cordi0[5]); //add ready time to service time
 	 	            }
 	 	            else
 	 	            {
 	 	            	distanceTime += ( Double.parseDouble(cordi0[5])); //add service time to acumulated distance
 	 	            }
 	            	
 	            	//distanceTime = (Double.parseDouble(cordi0[3]) + pi0.cartesianDistance(pi1)) + Double.parseDouble(cordi0[5]); //ready time + service time
 	            	sumOfDemand += demand;
 	                skippedCount = 0; //reset skipped counter anytime an assignable customer is found
 	            	chrom.remove(i);  //remove assigned customer from array
 		        }
 	            else
 	            {
 	            	skippedCount++;
 	            }
 	             
 	          } 
 	 	    
 	 	    /**
        	 * get last coordinate in previous route
        	 * This will be used to complete total distance traveled by previous route
        	 * by adding distance from this point to depot
        	 */
 	 	     totalDistance += p0.calculateEuclidianDistance(pi1); // depot to last node in previous route
            /*
             * 
 	 	     if(isTooLongEvalTime(startTime))
 	 	     {
 	 	    	vCount = Constants.DEFAULT_WORSE_FITNESS_CARS;
 	 	    	totalDistance = Constants.DEFAULT_WORSE_FITNESS;
 	 	    	chrom.clear();
 	 	     }
 	 	     */
 	 	    		
 	 	  vCount++; //counting assigned cars
 	 	  
 	    }
 	   
 	    
 	    vrptw.add(0,(double) vCount);
 	    vrptw.add(1,(double) totalDistance);
 	   
 	  return vrptw;
 	  
    }
    
    
    @SuppressWarnings("unused")
	private ArrayList<Integer> swapRandomIndex(ArrayList<Integer> c)
    {
    	int val0 = c.get(0);
    	int index = RandomGenerator.getRandomNumberBetween(1,c.size()-1);
    	
    	c.set(0,c.get(index));
    	c.set(index, val0);
    	
    	return c;
    }
    
    
    @SuppressWarnings("unused")
	private ArrayList<Integer> sortChromosomeByDistance(ArrayList<Integer> c,Properties p)
    {
    	String[] cord0,cordi1= new String[6];
    	String[] cord1 = new String[6];
    	ArrayList<Integer> results = new ArrayList<>();
    	ArrayList<Double>  distance = new ArrayList<>();
    	
    	cord0    = p.getProperty(Constants.CO_ORDINATES+"."+(c.get(0)+1)).split("\\s{1,}"); //get depo cordinates
    	Point p0 = new Point(Double.parseDouble(cord0[0]),Double.parseDouble(cord0[1]));
    	
    	results.add(0, c.get(0)); //add first element
    	c.remove(0);// remove first element from chrom
    	
    	//System.out.println("#came "+ chrom);
    	
 	    for(int i=0;i<c.size();i++)
 	    {
 	    	cord1    = p.getProperty(Constants.CO_ORDINATES+"."+(c.get(i)+1)).split("\\s{1,}"); //get depo cordinates
 	 	    Point p1 = new Point(Double.parseDouble(cord1[0]),Double.parseDouble(cord1[1]));
 	    	distance.add(p0.calculateEuclidianDistance(p1));
 	    }
 	    
 	    Collections.sort(distance);
 	    
 	    for(int i=0;i<distance.size();i++)
 	    {
 	    	for(int j=0;j<c.size();j++)
 	 	    {
 	 	    	cord1    = p.getProperty(Constants.CO_ORDINATES+"."+(c.get(j)+1)).split("\\s{1,}"); //get depo cordinates
 	 	 	    Point p1 = new Point(Double.parseDouble(cord1[0]),Double.parseDouble(cord1[1]));
 	 	 	    
 	 	 	    if(p0.calculateEuclidianDistance(p1)==distance.get(i))
 	 	 	    {
 	 	 	    	results.add(c.get(j));
 	 	 	    	c.remove((Object) c.get(j));
 	 	 	    	break;
 	 	 	    }
 	 	    }
 	    }
 	   //System.out.println("#leave "+ results);
    	return results;
    	
    }
    
    
    
    /**
     * 
     * @param c
     * @return
     * @deprecated
     */
    public ArrayList<Double> timeWindowEvaluationsDeprecated(Chromosome c,Properties p)
    {

    	/*
    	 * node.1.x         = 40.00
         * node.1.y         = 50.00
    	 */
    	String[] cord0,cordi0,cordi1= new String[6];
    	@SuppressWarnings("unused")
		String[] cord1 = new String[6];
    	String[] params = new String[6];
    	ArrayList<Double>  vrptw = new ArrayList<>();
    	int j;
    	
    	double sumOfDemand = 0;
    	double distanceTime  = 0;
    	double totalDistance = 0;
    	
     	int vCount = 1;
     	
    	/*
    	 * for each customer indexes cord are distributed as below
    	 * CUST NO.   XCOORD.    YCOORD.    DEMAND   READY TIME   DUE DATE   SERVICE TIME
    	 *              0          1           2         3            4            5
    	 */
     	//System.out.println("#capacity: "+p.getProperty(Constants.VEHICLE_CAPACITY));
 	    int vCapacity = Integer.parseInt(p.getProperty(Constants.VEHICLE_CAPACITY));
 	    
 	    cord0    = p.getProperty(Constants.CO_ORDINATES+".1").split("\\s{1,}"); //get depo cordinates
   	    Point p0 = new Point(Double.parseDouble(cord0[0]),Double.parseDouble(cord0[1]));
   	  
   	    cord1    = p.getProperty(Constants.CO_ORDINATES+"."+c.getChromosome().get(0)).split("\\s{1,}"); //get depo cordinates
	    Point p1 = new Point(Double.parseDouble(cord0[0]),Double.parseDouble(cord0[1]));
	    
 	    distanceTime = -p0.calculateEuclidianDistance(p1); //initialise distance to p0-p1
 	    totalDistance = p0.calculateEuclidianDistance(p1); //set total 
 	    
 	    for(int i=0; i<c.getChromosome().size();i++)
        {
 	    	
        	/*
        	 * when genes are exhausted, loop back j to one
        	 * so cycle completes e.g. 2-4-5--1-3-2, 4-2-5-3-1-4 ... etc
        	 */
        	j = (i==c.getChromosome().size()-1)?0:i+1;
        	//note use of i+1 & j+1 to reference cordinates, because of cordinate numbering in param file
	    	params        = p.getProperty(Constants.CO_ORDINATES+"."+(c.getChromosome().get(i)+1)).split("\\s{1,}");
	    	double demand = Double.parseDouble(params[2]);
 	    	/*
 	    	 * keep adding demand until vehicle capacity is full
 	    	 * it its full, start loading an empty truck
 	    	 */
	    
	    	cordi0     = p.getProperty(Constants.CO_ORDINATES+"."+(c.getChromosome().get(i)+1)).split("\\s{1,}");
            Point pi0 = new Point(Double.parseDouble(cordi0[0]),Double.parseDouble(cordi0[1]));
            //System.out.println("hahahaha" + i+ " : "+ (c.getChromosome().get(j)+1));
	    	cordi1      = p.getProperty(Constants.CO_ORDINATES+"."+(c.getChromosome().get(j)+1)).split("\\s{1,}");
            Point pi1 = new Point(Double.parseDouble(cordi1[0]),Double.parseDouble(cordi1[1]));
    	    
            //distanceTime += Double.parseDouble(cordi0[3]) + Double.parseDouble(cordi0[5]); //ready time + service time
            	
            if( (distanceTime < Double.parseDouble(cordi0[4])) && (sumOfDemand < vCapacity) ) //compare current distance-time to due date
    	    { 	
            	distanceTime += (Double.parseDouble(cordi0[3]) - pi0.calculateEuclidianDistance(pi1));//  + Double.parseDouble(cordi0[5]); //ready time + service time
            	sumOfDemand+=demand;
	        }
	        else
	    	{ 
	        	distanceTime   = (Double.parseDouble(cordi0[3]) - p0.calculateEuclidianDistance(pi0));//  + Double.parseDouble(cordi0[5]); //initialise distance to p0-p1
	        	totalDistance += (distanceTime+ p0.calculateEuclidianDistance(p1)); //;
	        	sumOfDemand    = demand;
	        	vCount++;
	    	}
          }
 	    
 	    
 	    
 	    
 	    vrptw.add(0,(double) vCount);
 	    vrptw.add(1,(double) totalDistance);
 	    //System.out.println("Cars" + vCount + " distance "+ totalDistance);
 	  return vrptw;
 	  
    }
    
       
       /**
        * 
        * @param c
        * @return
        */
       public double sumDistanceVRP(Chromosome c, Properties p)
       {
        	/*
        	 * node.1.x         = 40.00
             * node.1.y         = 50.00
        	 */
        	String[] cord0 = new String[6];
        	String[] cord1 = new String[6];
        	String[] cord2 = new String[6];
        	String[] params = new String[6];
        	DecimalFormat df = new DecimalFormat("#.#####");
        	
        	int j;
        	
        	double sum = 0;
        	double distance = 0;
       	
       	/*
       	 * for each customer indexes cord are distributed as below
       	 * CUST NO.   XCOORD.    YCOORD.    DEMAND   READY TIME   DUE DATE   SERVICE TIME
       	 *              0          1           2         3            4            5
       	 */
    	   
    	    int vCapacity = Integer.parseInt(p.getProperty(Constants.VEHICLE_CAPACITY));
    	    for(int i=0; i<c.getChromosome().size();i++)
            {
    	    	
    	    	cord0    = p.getProperty(Constants.CO_ORDINATES+".1").split("\\s{1,}"); //get starting point
    	    	Point p0 = new Point(Double.parseDouble(cord0[0]),Double.parseDouble(cord0[1]));
            	/*
            	 * when genes are exhausted, loop back j to one
            	 * so cycle completes e.g. 2-4-5--1-3-2, 4-2-5-3-1-4 ... etc
            	 */
            	j = (i==c.getChromosome().size()-1)?0:i+1;
    	    	params        = p.getProperty(Constants.CO_ORDINATES+"."+c.getChromosome().get(i)).split("\\s{1,}");
    	    	double demand = Double.parseDouble(params[2]);
    	    	/*
    	    	 * keep adding demand until vehicle capacity is full
    	    	 * it its full, start loading an empty truck
    	    	 */
    	    	if( (sum+demand) <= vCapacity)
    	    	{
    	    		cord1    = p.getProperty(Constants.CO_ORDINATES+"."+c.getChromosome().get(i)).split("\\s{1,}");
                	Point p1 = new Point(Double.parseDouble(cord1[0]),Double.parseDouble(cord1[1]));
    	    		cord2    = p.getProperty(Constants.CO_ORDINATES+"."+c.getChromosome().get(j)).split("\\s{1,}");
                	Point p2 = new Point(Double.parseDouble(cord2[0]),Double.parseDouble(cord2[1]));
        	    	
    	    		sum+=demand;
    	    		distance += p1.calculateEuclidianDistance(p2);
    	    		//System.out.print(" "+cord1[0]+"-"+cord2[0]+" ");
    	    	}
    	    	else
    	    	{ 
    	    		cord1    = p.getProperty(Constants.CO_ORDINATES+"."+c.getChromosome().get(j)).split("\\s{1,}");          //new beginning point
                	Point p1 = new Point(Double.parseDouble(cord1[0]),Double.parseDouble(cord1[1]));
    	    		cord2    = (j==0)?p.getProperty(Constants.CO_ORDINATES+"."+c.getChromosome().get(0)).split("\\s{1,}"):
    	    			              p.getProperty(Constants.CO_ORDINATES+"."+c.getChromosome().get(j-1)).split("\\s{1,}"); //last point before new route
    	    		Point p2 = new Point(Double.parseDouble(cord2[0]),Double.parseDouble(cord2[1]));
        	    	 
    	    		sum = demand;
    	    		distance += (p0.calculateEuclidianDistance(p2)+p0.calculateEuclidianDistance(p1)); //end previous route & begin new route from point0
    	    		//distance += p0.cartesianDistance(p1); //
    	    		
    	    		//System.out.print(cord2[0]+"-"+cord0[0]+" #end#begin "+cord0[0]+"-"+cord1[0]+" ");
    	    	}
             }
    	    
    	  return Double.parseDouble(df.format(distance));
    	  
       }
       
    
       
       /**
        * 
        * @param c
        * @return
        */
       public int sumCars(Chromosome c,Properties p)
       {
    	  
        	String[] params = new String[6];
        	double sum = 0;
        	int count = 1;
        	
       	/*
       	 * for each customer indexes cord are distributed as below
       	 * CUST NO.   XCOORD.    YCOORD.    DEMAND   READY TIME   DUE DATE   SERVICE TIME
       	 *              0          1           2         3            4            5
       	 */
        	//System.out.println("#capacity: "+p.getProperty(Constants.VEHICLE_CAPACITY));
    	    int vCapacity = Integer.parseInt(p.getProperty(Constants.VEHICLE_CAPACITY));
    	    
    	    for(int i=0; i<c.getChromosome().size();i++)
            {
    	    	params  = p.getProperty(Constants.CO_ORDINATES+"."+c.getChromosome().get(i)).split("\\s{1,}");
    	    	double demand = Double.parseDouble(params[2]);
    	    	/*
    	    	 * keep adding demand until vehicle capacity is full
    	    	 * it its full, start loading an empty truck
    	    	 */
    	    	if( (sum+demand) <= vCapacity)
    	    	{
    	    		sum+=demand;
    	    	}
    	    	else
    	    	{
    	    		count++;
    	    		sum = demand;
    	    	}
             }
    	  return count;
       }
    
       
       
       
       /**
        * 
        * @param pop
        * @return 
        * @deprecated
        */
       public double setAverageFitness(Population pop)
       {
          double averageFitness = 0;
          for( int i=0; i< pop.size(); i++)
          {
             averageFitness += sumDistanceVRP(pop.get(i),this.getProperties());
          }
          return averageFitness/pop.size();
       }

        
    
       /**
        * 
        * @return
        */
       public ArrayList<Double> calcGenerationalFitness(Population pop,Properties p)
      	{
    	      //define weight
    	      double vehicleWeight = 1; 
    	      double routeWeight   = 0.001;
    	      
      		  double sum = 0;
      		  this.getGenerationFitness().clear(); //added to control population increment
      	      
      	       for( int i=0; i< pop.size(); i++)
      	       {
      	          /* this.setIndividual(pop.get(i));
      	           * this.setDouble(sumDistance(this.getIndividual()));
      	           * specify weight from parameter file
      	           * this.setDouble(weightedFitness(vehicleWeight,routeWeight,p));
                   * pop.get(i).setFitness(new BasicFitness());
                   */
      	          pop.get(i).getFitness().setDouble(weightedFitness(pop.get(i),vehicleWeight,routeWeight,p));
    	         
      	          //this.generationFitness.add(i, this.getDouble());
      	          this.getGenerationFitness().add(i, pop.get(i).getFitness().getDouble());
    	          
      	          sum += pop.get(i).getFitness().getDouble();
      	          //System.out.println("Individual#"+i+" "+this.getDouble());
      	       }
      	     this.setTotalFitness(sum); //total fitness
      	     
      	    return this.getGenerationFitness();
      	}


       public double weightedFitness(Individual i,double alpha, double beta,Properties p)
       {
    	   
    	   ArrayList<Double> vrp = timeWindowEvaluations(i,p);
    	   return (alpha*vrp.get(0) + beta*vrp.get(1));
    	   
    	   //return (alpha*sumCars(this.getIndividual(),this.getProperties()) + beta*sumDistanceVRP(this.getIndividual(),this.getProperties()));
    	   
       }
    
    
	

}