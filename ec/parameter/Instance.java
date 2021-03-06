/*******************************************************************************
 * Copyright (c) 2014, 2015 
 * Anthony Awuley - Brock University Computer Science Department
 * All rights reserved. This program and the accompanying materials
 * are made available under the Academic Free License version 3.0
 * which accompanies this distribution, and is available at
 * https://aawuley@bitbucket.org/aawuley/evolvex.git
 *
 * Contributors:
 *     ECJ                     MersenneTwister & MersenneTwisterFast (https://cs.gmu.edu/~eclab/projects/ecj)
 *     voidException      Tabu Search (http://voidException.weebly.com)
 *     Lucia Blondel       Simulated Anealing 
 *     
 *
 *        
 *******************************************************************************/
package ec.parameter;

import ec.individuals.Gene;
import ec.individuals.fitnesspackage.PopulationFitness;

import java.util.Properties;

import ec.main.EC;
import ec.main.Run;
import ec.algorithms.alps.ALPSReplacement;
import ec.algorithms.alps.AgingScheme;
import ec.operator.CrossoverModule;
import ec.operator.InitialisationModule;
import ec.operator.MutationModule;
import ec.operator.operations.ReplacementStrategy;
import ec.operator.operations.SelectionOperation;
import ec.operator.operations.replacement.AbstractSSReplacement;
import ec.util.Constants;
import ec.util.statistics.StatisticsCollector;

/**
 * 
 * @author Anthony Awuley
 *
 */
public class Instance {

	public Instance() 
	{
	}

	 /**
	  * @param p
	  * @return selected alps replacement object
	  */
	 public ALPSReplacement replacementStrategyALPS(Properties p) 
	 {
		 ALPSReplacement as = null;
	        String className;
	        try {
	            className = p.getProperty(Constants.ALPS_REPLACMENT_STRATEGY);
	            Class<?> obj = Class.forName(className);
		    as = (ALPSReplacement)obj.newInstance();
		    
	        } catch(IllegalAccessException e) {
	            System.err.println(this.getClass().getName()+". exception: "+e);
	        }  catch (ClassNotFoundException e) {
	            System.err.println(this.getClass().getName()+". exception: "+e);
		    e.printStackTrace();    
	        }  catch (InstantiationException e) {
	            System.err.println(this.getClass().getName()+". exception: "+e);
		    e.printStackTrace();
	        }
	        return as;
	    }
	
	/**
	 * 
	 * @param p
	 * @return selected replacement strategy used withing steady state replacement
	 */
	public AbstractSSReplacement  ssReplacement(Properties p) 
	 {
		    AbstractSSReplacement  as = null;
	        String className;
	        try {
	            className = p.getProperty(Constants.STEADY_STATE_REPLACEMENT);
	            Class<?> obj = Class.forName(className);
		    as = (AbstractSSReplacement )obj.newInstance();
		    
	        } catch(IllegalAccessException e) {
	            System.err.println(this.getClass().getName()+". exception: "+e);
	        }  catch (ClassNotFoundException e) {
	            System.err.println(this.getClass().getName()+". exception: "+e);
		    e.printStackTrace();    
	        }  catch (InstantiationException e) {
	            System.err.println(this.getClass().getName()+". exception: "+e);
		    e.printStackTrace();
	        }
	        return as;
	    }
	
	
	
	/**
	 * @param p
	 * @return aging scheme object
	 */
	 public AgingScheme agingLayers(Properties p) 
	 {
		    AgingScheme as = null;
	        String className;
	        try {
	            className = p.getProperty(Constants.ALPS_AGING_SCHEME);
	            Class<?> obj = Class.forName(className);
		    as = (AgingScheme)obj.newInstance();
		    
	        } catch(IllegalAccessException e) {
	            System.err.println(this.getClass().getName()+". exception: "+e);
	        }  catch (ClassNotFoundException e) {
	            System.err.println(this.getClass().getName()+". exception: "+e);
		    e.printStackTrace();    
	        }  catch (InstantiationException e) {
	            System.err.println(this.getClass().getName()+". exception: "+e);
		    e.printStackTrace();
	        }
	        return as;
	    }
	
	 public ReplacementStrategy replacementOperation(Properties p) 
	 {
	    	ReplacementStrategy rs = null;
	        String className;
	        try {
	            className = p.getProperty(Constants.REPLACEMENT_OPERATION);
	            Class<?> obj = Class.forName(className);
		    rs = (ReplacementStrategy)obj.newInstance();
		    
	        } catch(IllegalAccessException e) {
	            System.err.println(this.getClass().getName()+". exception: "+e);
	        }  catch (ClassNotFoundException e) {
	            System.err.println(this.getClass().getName()+". exception: "+e);
		    e.printStackTrace();    
	        }  catch (InstantiationException e) {
	            System.err.println(this.getClass().getName()+". exception: "+e);
		    e.printStackTrace();
	        }
	        return rs;
	    }
	    
	    protected CrossoverModule crossoverOperation(Properties p) 
	    {
	    	CrossoverModule cm = null;
	        String className;
	        try {
	            className = p.getProperty(Constants.CROSSOVER_OPERATION);
	            Class<?> obj = Class.forName(className);
		    cm = (CrossoverModule)obj.newInstance();
		    
	        } catch(IllegalAccessException e) {
	            System.err.println(this.getClass().getName()+". exception: "+e);
	        }  catch (ClassNotFoundException e) {
	            System.err.println(this.getClass().getName()+". exception: "+e);
		    e.printStackTrace();    
	        }  catch (InstantiationException e) {
	            System.err.println(this.getClass().getName()+". exception: "+e);
		    e.printStackTrace();
	        }
	        return cm;
	    }
	    
	    
	    protected EC mainClass(Properties p) 
	    {
	    	EC ec = null;
	        String className;
	        try {
	            className = p.getProperty(Constants.MAIN_CLASS);
	            Class<?> obj = Class.forName(className);
		    ec = (EC) obj.newInstance();
		    
	        } catch(IllegalAccessException e) {
	            System.err.println(this.getClass().getName()+". exception: "+e);
	        }  catch (ClassNotFoundException e) {
	            System.err.println(this.getClass().getName()+". exception: "+e);
		    e.printStackTrace();    
	        }  catch (InstantiationException e) {
	            System.err.println(this.getClass().getName()+". exception: "+e);
		    e.printStackTrace();
	        }
	        return ec;
	    }
	    
	    
	    protected MutationModule mutationOperation(Properties p) 
	    {
	    	MutationModule mm = null;
	        String className;
	        try {
	            className = p.getProperty(Constants.MUTATION_OPERATION);
	            Class<?> obj = Class.forName(className);
		    mm = (MutationModule)obj.newInstance();
		    
	        } catch(IllegalAccessException e) {
	            System.err.println(this.getClass().getName()+". exception: "+e);
	        }  catch (ClassNotFoundException e) {
	            System.err.println(this.getClass().getName()+". exception: "+e);
		    e.printStackTrace();    
	        }  catch (InstantiationException e) {
	            System.err.println(this.getClass().getName()+". exception: "+e);
		    e.printStackTrace();
	        }
	        return mm;
	    }
	    
	    public PopulationFitness fitnessEvaluator(Properties p) 
	    {
	    	PopulationFitness f = null;
	        String className;
	        try {
	            className = p.getProperty(Constants.FITNESS_FUNCTION);
	            Class<?> obj = Class.forName(className);
		    f = (PopulationFitness)obj.newInstance();
		    
	        } catch(IllegalAccessException e) {
	            System.err.println(this.getClass().getName()+". exception: "+e);
	        }  catch (ClassNotFoundException e) {
	            System.err.println(this.getClass().getName()+". exception: "+e);
		    e.printStackTrace();    
	        }  catch (InstantiationException e) {
	            System.err.println(this.getClass().getName()+". exception: "+e);
		    e.printStackTrace();
	        }
	        return f;
	    }
	    
	    
	    protected Run abstractRun(Properties p) 
	    {
	    	Run so = null;
	        String className;
	        try {
	            className = p.getProperty(Constants.ALGORITHM_SELECTOR);
	            Class<?> obj = Class.forName(className);
		    so = (Run)obj.newInstance();
		    
	        } catch(IllegalAccessException e) {
	            System.err.println(this.getClass().getName()+". exception: "+e);
	        }  catch (ClassNotFoundException e) {
	            System.err.println(this.getClass().getName()+". exception: "+e);
		    e.printStackTrace();    
	        }  catch (InstantiationException e) {
	            System.err.println(this.getClass().getName()+". exception: "+e);
		    e.printStackTrace();
	        }
	        return so;
	    }
	    
	    
	    public SelectionOperation selectionOperator(Properties p) 
	    {
	    	SelectionOperation so = null;
	        String className;
	        try {
	            className = p.getProperty(Constants.SELECTION_OPERATION);
	            Class<?> obj = Class.forName(className);
		    so = (SelectionOperation)obj.newInstance();
		    
	        } catch(IllegalAccessException e) {
	            System.err.println(this.getClass().getName()+". exception: "+e);
	        }  catch (ClassNotFoundException e) {
	            System.err.println(this.getClass().getName()+". exception: "+e);
		    e.printStackTrace();    
	        }  catch (InstantiationException e) {
	            System.err.println(this.getClass().getName()+". exception: "+e);
		    e.printStackTrace();
	        }
	        return so;
	    }
	    
	    
	    protected InitialisationModule initialiserModule(Properties p) 
	    {
	    	InitialisationModule init = null;
	        String className;
	        try {
	            className = p.getProperty(Constants.INITIALISER);
	            Class<?> obj = Class.forName(className);
		    init = (InitialisationModule)obj.newInstance();
		    
	        } catch(IllegalAccessException e) {
	            System.err.println(this.getClass().getName()+". exception: "+e);
	        }  catch (ClassNotFoundException e) {
	            System.err.println(this.getClass().getName()+". exception: "+e);
		    e.printStackTrace();    
	        }  catch (InstantiationException e) {
	            System.err.println(this.getClass().getName()+". exception: "+e);
		    e.printStackTrace();
	        }
	        return init;
	    }
	
	    protected Gene geneRepresentation(Properties p) 
	    {
	    	Gene g = null;
	        String className;
	        try {
	            className = p.getProperty(Constants.GENE_REPRESENTATION);
	            Class<?> obj = Class.forName(className);
		    g = (Gene) obj.newInstance();
		    
	        } catch(IllegalAccessException e) {
	            System.err.println(this.getClass().getName()+". exception: "+e);
	        }  catch (ClassNotFoundException e) {
	            System.err.println(this.getClass().getName()+". exception: "+e);
		    e.printStackTrace();    
	        }  catch (InstantiationException e) {
	            System.err.println(this.getClass().getName()+". exception: "+e);
		    e.printStackTrace();
	        }
	        return g;
	    }
	    
	    
	    protected StatisticsCollector statisticsOperation(Properties p) 
	    {
	    	StatisticsCollector stats = null;
	    	
	        String className;
	        try 
	        {
	        	
	        	if(p.getProperty(Constants.STATS_OPERATION) != null)
	        	{
	        		className = p.getProperty(Constants.STATS_OPERATION);
	        	}
	        	else
	        	{   
	        		className = Constants.DEFAULT_STATS_OPERATION;
	        	}
	            Class<?> obj = Class.forName(className);
		        stats = (StatisticsCollector) obj.newInstance();
		    
	        }
	        catch (ClassNotFoundException e) {
	            System.err.println(this.getClass().getName()+". exception: "+e);
		    e.printStackTrace();    
	        }
	        catch(IllegalAccessException e) 
	        {
	            System.err.println(this.getClass().getName()+". exception: "+e);
	        }  
	        catch (InstantiationException e) 
	        {
	            System.err.println(this.getClass().getName()+". exception: "+e);
		    e.printStackTrace();
	        }
	        
	        return stats;
	    }
	
}
