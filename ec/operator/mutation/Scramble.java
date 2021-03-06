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
package ec.operator.mutation;

import ec.algorithms.ga.Evolve;
import ec.individuals.Chromosome;
import ec.individuals.Individual;
import ec.individuals.populations.Population;

import java.util.ArrayList;

import ec.operator.MutationModule;

/**
 *
 * @author  Anthony Awuley
 */
public class Scramble extends MutationModule {

	public Scramble(String type) 
	{
		super(type);
	}

	@Override
	public ArrayList<Individual> performMutationOperation(
			Evolve e,Population p,
			Chromosome c1, int parentId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Individual> performMutationOperation(
			Evolve e,Population p,
			Chromosome c1, int parentId, ArrayList<Double> ages) {
		// TODO Auto-generated method stub
		return null;
	}

    
}
