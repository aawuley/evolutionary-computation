/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package operator;

import individuals.Chromosome;
import individuals.Individual;
import individuals.populations.Population;

import java.util.ArrayList;

/**
 *
 * @author anthony
 */
public abstract class CrossoverModule extends Operator {
    
    private String crossoverType;
    private final String crossoverOperation ="crossover operation";
    private double crossoverRate;
    private int childrenAdded;
    
    
    public  CrossoverModule(String type)
    {
       this.crossoverType = type;
    }
    
   
    /**
     * @param p
     * @return 
     */
    public abstract ArrayList<Individual> performCrossoverOperation(
    		Population p, 
    		Chromosome c1, 
    		Chromosome c2,
    		ArrayList<Integer> tournamentIndividuals, 
    		int numberOfChildrenToAdd);
    
   /**
    * OVERLOAD ALPS add age
    * @param p
    * @param c1
    * @param c2
    * @param tournamentIndividuals
    * @param numberOfChildrenToAdd
    * @param age[] age of parents
    * @param replacementType [steadyState:Generational:etc]
    * @return
    */
    public abstract ArrayList<Individual> performCrossoverOperation(
    		Population p, 
    		Chromosome c1, 
    		Chromosome c2,
    		ArrayList<Integer> tournamentIndividuals, 
    		int numberOfChildrenToAdd,
    		ArrayList<Double> ages,
    		String replacementType);
    
    /**
     * Returns the crossover policy.
     * @return crossover policy
     */
    public String getCrossoverPolicy() 
    {
        return crossoverOperation;
    }

    /**
     * Returns the crossover rate.
     * @return crossover rate
     */
    public double getCrossoverRate() 
    {
        return this.crossoverRate;
    }
    /**
     * @deprecated
     * @param number
     */
    public void setChildrenAdded(int number)
    {
       this.childrenAdded = number;
    }
    
    /**
     * @deprecated
     * @return
     */
    public int getChildrenAdded()
    {
      return this.childrenAdded;
    }

    
    /**
     * 
     * @param rate 
     */
    public void setCrossoverRate(double rate) 
    {
        this.crossoverRate = rate;
    }
    
    public String getSpecificModuleType()
    {
       return this.crossoverType;
    }
    
        
    /**
     * 
     * @param child
     * @param value
     * @return 
     */
    public int returnAvailableIndex(Chromosome child, int value)
    {
        for(int i=0; i<child.getChromosome().size(); i++)
        { 
            if(child.getChromosome().get(i).equals(value))
            {
                return i;
            }
        }
        return -1;
    }
   
    /**
     * 
     * @return mutated offspring
     */
    //public abstract Population getOffsprings();
    
    /**
     * 
     * @param individuals
     */
    //public abstract void setOffsprings(ArrayList<Individual> individuals);
 
    /**
     * Get operation that operator performs
     * @return operation
     */
    public String getOperation()
    {
       return this.crossoverOperation;
    }
    
    
}