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
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.individuals;

import java.util.ArrayList;

import ec.individuals.fitnesspackage.Fitness;
   

/**
 * This is a prototype class for an individual
 * 
 * @author Anthony Awuley
 */

public interface IndividualInterface{
    
    /**
     * Get fitness of individuals
     * @return individual fitness
     */
    public Fitness getFitness();
    
    /**
     * Set fitness
     * @param f fitness
     */
    public void setFitness(Fitness fitness);
    
    /**
     * Get the genotype
     * @return genotype
     */
    public Chromosome getGenotype();
    
    
    /**
     * @throws CloneNotSupportedException 
     * 
     */
    public Individual clone() throws CloneNotSupportedException;
    
    
    /**
     * Get phenotype
     * @return phenotype
     */
    public Phenotype getPhenotype();
    
    /**
     * Get a String representation of the output(Phenotype)
     * @param map which output to get if there are multiple
     * @return string of output
     */
    public String getPhenotypeString(int map);
    
    /**
     * Set genotype
     * @param g genotype
     */
    public void setGenotype(Chromosome g);
    
    /**
     * Set phenotype
     * @param p phenotype
     */
    public void setPhenotype(Phenotype p);
    
    /**
     * Has the individual been evaluated
     * @return boolean of evaluation status
     */
    public boolean isEvaluated();

    /**
     * Indicate if the individual should be evaluated or not
     * @param b set if individual should be evaluated
     */
    public void setEvaluated(boolean b);

    /**
     * Get the validity of the individual
     * @return validity of the individual
     */
    public boolean isValid();

    /**
     * Set the validity of the individual
     * @param b validity to be set
     */
    public void setValid(boolean b);

    /**
     * Age is how long the individual has existed
     * @param age How long the individual has existed  
     * The age-measure that we define for the ALPS-EA is 
     * a count of how many generations in which the individuals 
     * genotypic material has been evolving inside the population. 
     * 
     * The age-measure that we define for the ALPS-EA is a count 
     * of how many generations in which the individuals genotypic 
     * material has been evolving inside the population. New individuals, 
     * that are randomly generated, start with an initial age of 0 since 
     * their genetic material has just been introduced into the population.
     **/
    public void setAge(double age);

    /**
     * The age of the individual, counted as how many
     * iterations it has survived.
     * @return number of iterations survived
     */
    public double getAge();
    
    
    /**
     * Age is how long the individual has existed
     * @param set the layer to which an individual belongs 
     **/
    public void setLayerId(int layer);

    /**
     * 
     * @return current layer of individual
     */
    public int getLayerId();
    
    /**
     * Get an evaluated string for further evaluation
     * @return string for second evaluator
     */
    public ArrayList<Integer> getHybrid();

    /**
     * Set an evaluated string to be further evaluated
     * @param i individual to which the fitness belongs
     */
    public void setHybrid(ArrayList<Integer> i);
    
    /**
     * Get multiple fitness values for multi-objective
     */
    public ArrayList<Double> getMultipleFitness();
    
    /**
     * set multiple fintess for multi-objective
     * @param mop individual to which the fitness belongs
     */
    public void setMultipleFitness(ArrayList<Double> mop);
    
    /**
     * 
     * @return number of evaluations that have been performed so far in Steady State ALPS
     */
    public double getBirthEvaluations();
    
    /**
     * Randomly generated individuals store the number of evaluations 
     * that have been performed so far in Steady State ALPS
     * @param numberOfEvaluations
     */
    public void setBirthEvaluations(double numberOfEvaluations);

    /**
     * SEt chromosome for an individual
     * @param c
     */
	public void setChromosome(Chromosome c);
    /**
     * 
     * @return get an indiviuals chromosome
     */
	public Chromosome getChromosome();
    
}
