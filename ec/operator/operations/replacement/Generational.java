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
package ec.operator.operations.replacement;

import ec.fitnessevaluation.FitnessExtension;
import ec.individuals.Chromosome;
import ec.individuals.Gene;
import ec.individuals.Individual;
import ec.individuals.fitnesspackage.PopulationFitness;
import ec.individuals.populations.Population;
import ec.operator.CrossoverModule;
import ec.operator.MutationModule;
import ec.operator.operations.Operations;
import ec.operator.operations.ReplacementStrategy;
import ec.operator.operations.SelectionOperation;
import ec.util.statistics.BasicStatistics;
import ec.util.statistics.StatisticsCollector;

import java.util.ArrayList;

import com.sun.org.apache.xpath.internal.operations.Operation;

import ec.algorithms.alps.ALPSReplacement;
import ec.algorithms.alps.system.ALPSLayers;
import ec.algorithms.alps.system.Engine;
import ec.algorithms.ga.Evolve;

/**
 * Generational replacement strategy
 * 
 * @author  Anthony Awuley
 */
public class Generational extends Operations implements ReplacementStrategy {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int populationCount;
	private double randomNumber;
	private final String replacmentType = "Generational";
	private ArrayList<Double> ages = new ArrayList<>();

	@Override
	public String toString() {
		return this.replacmentType;
	}

	/**
	 * GENERATE next generation First, individuals are restricted to only
	 * breeding with individuals in their own layer or from the layer
	 * immediately before them. Second, the bottom layer is replaced with
	 * randomly generated individuals at regular intervals.
	 * 
	 * Even if an individual is selected to reproduce multiple times in one
	 * generation its age is still only increased by 1 so that good individuals
	 * that reproduce a lot are not penalized for being more fit than similarly
	 * aged individuals.
	 * 
	 * @param f
	 *            - basic fitness
	 * @param crx
	 *            - crossover module
	 * @param mtx
	 *            - mutation module
	 * @param selectionOperation
	 * @param stats
	 * @param p
	 * @param so
	 *            - selection operator
	 * @param current
	 *            - population <previous+current>
	 * @param generation
	 *            - current generation count
	 * @param run
	 *            - current run count
	 * @param crossoverRate
	 *            - crossover rate
	 * @param elitismSize
	 * @param mutationRate
	 *            - mutation rate
	 * @param selectionPressure
	 * @param elitismFlag
	 * @param tournamentSize
	 *            - tournament size
	 * @return
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Population nextGeneration(
			Evolve e, 
			PopulationFitness f,
			CrossoverModule crx, 
			MutationModule mtx,
			SelectionOperation selectionOperation, 
			StatisticsCollector stats,
			final ArrayList<Population> current, 
			int generation, 
			int run) {
		
		Population nextGeneration = new Population();
		Chromosome c1 = new Chromosome();
		Chromosome c2 = new Chromosome();
		ArrayList<Integer> tournamentIndividuals = new ArrayList<>();

		ArrayList<Individual> bestIndividualsOfPreviousGeneration = new ArrayList<>();
		this.populationCount = 0; // initialized from one because of addition of  best individual

		
		((FitnessExtension) f).calculateSimpleFitness(
				current.get(generation - 1), run, generation,
				(BasicStatistics) stats, e.properties);
		/*
		 * old implementation assuming one best individual Individual
		 * bestIndividualOfPreviousGeneration =
		 * current.get(generation-1).get(((FitnessExtension) f).getBestIndividualsOfGeneration().get(0));
		 */
		if (e.elitismSize > 0) // ignore elitism if steady state replacement is  used
		{ // create elite individuals to be added to next generation
			for (int i = 0; i < e.elitismSize; i++) {
				bestIndividualsOfPreviousGeneration.add(current.get(generation - 1).get(
						((FitnessExtension) f).getBestIndividualsOfGeneration().get(i)).clone());
				this.populationCount++; // increment population count
			}
			/*
			 * add best individual of previous generation
			 * nextGeneration.add(bestIndividualOfPreviousGeneration);
			 */
			nextGeneration.addAll(bestIndividualsOfPreviousGeneration);
		}

		while (this.populationCount < current.get(generation - 1).size()) // best for nextGeneration.size()
		{
			this.randomNumber = e.random.nextDouble(); // crossover?
			
			/*
			 * Individuals that are created through variation, such as by
			 * mutation or recombination, take the age value of their oldest
			 * parent plus 1 since their genetic material comes from their
			 * parents and has now been in the population for one more
			 * generation than their parents. Each generation in which an
			 * individual is used as a parent to create an offspring its age is
			 * increases by 1 since its genetic material has been used in
			 * evolution in another generation
			 */
			if ((this.randomNumber < e.crossoverRate)) {
				tournamentIndividuals.clear(); // clear content of tournament individuals

				/*
				 * Perform two tournament selections and pick "best" from each
				 * using selection pressure
				 */
				selectionOperation.performTournamentSelection(e,current.get(generation - 1).size());
				
				tournamentIndividuals.add(((FitnessExtension) f)
						.selectIndividualsBasedOnFitness(e,
								selectionOperation.getTournamentSelection(),
								e.selectionPressure).get(0)); // select best 2 individuals

				selectionOperation.performTournamentSelection(e,
						current.get(generation - 1).size());
				tournamentIndividuals.add(((FitnessExtension) f)
						.selectIndividualsBasedOnFitness(e,
								selectionOperation.getTournamentSelection(),
								e.selectionPressure).get(0)); // select best 2  individuals
				
				// TODO use individuals instead of chromosomes
				c1.setGenes((ArrayList<Gene>) current.get(generation - 1)
						.get(tournamentIndividuals.get(0)).getChromosome()
						.getGenes().clone()); // clone individuals
				c2.setGenes((ArrayList<Gene>) current.get(generation - 1)
						.get(tournamentIndividuals.get(1)).getChromosome()
						.getGenes().clone());

				/*
				 * add new individuals to population pass two individuals for
				 * crossover automatically determines how many individuals to
				 * add based on current population size left
				 * nextGeneration.addAll
				 * (nextGeneration,crx.performCrossoverOperation
				 * (current.get(generation -1), c1, c2,tournamentIndividuals,(current.get(generation-1).size() - this.populationCount)));
				 */

				nextGeneration.addAll(crx.performCrossoverOperation(e,current
						.get(generation - 1), c1.clone(), c2.clone(),
						tournamentIndividuals, (current.get(generation - 1)
								.size() - this.populationCount)));
				
				/* increment population by number of children  added */
				this.populationCount += crx.getOffsprings().size(); 
			}
			// mutation?
			if ((this.randomNumber < e.mutationRate)
					&& (this.populationCount < current.get(generation - 1).size())) { // apply mutation policy to the chromosomes
				tournamentIndividuals.clear(); // clear content of tournament individuals

				// SelectionOperation;
				selectionOperation.performTournamentSelection(e,
						current.get(generation - 1).size());
				
				/* select two best tournament individuals select best individuals from tournament selection*/
				tournamentIndividuals.add(((FitnessExtension) f)
						.selectIndividualsBasedOnFitness(e,
								selectionOperation.getTournamentSelection(),
								e.selectionPressure).get(0));

				c1.setGenes((ArrayList<Gene>) current.get(generation - 1)
						.get(tournamentIndividuals.get(0)).getChromosome()
						.getGenes().clone()); // clone individuals

				/*
				 * add new individual to population
				 * nextGeneration.addAll(nextGeneration
				 * ,mtx.performMutationOperation(current.get(generation -1),
				 * c1,tournamentIndividuals.get(0)));
				 */
				nextGeneration.addAll(mtx.performMutationOperation(
						e,
						current.get(generation - 1), c1.clone(),
						tournamentIndividuals.get(0)));
				this.populationCount++; // increment population
			}
		}

		return nextGeneration;

	}

	
	
	/**
	 * GENERATE next generation First, individuals are restricted to only
	 * breeding with individuals in their own layer or from the layer
	 * immediately before them. Second, the bottom layer is replaced with
	 * randomly generated individuals at regular intervals.
	 * 
	 * Even if an individual is selected to reproduce multiple times in one
	 * generation its age is still only increased by 1 so that good individuals
	 * that reproduce a lot are not penalized for being more fit than similarly
	 * aged individuals.
	 * 
	 * @param f
	 *            - basic fitness
	 * @param crx
	 *            - crossover module
	 * @param mtx
	 *            - mutation module
	 * @param selectionOperation
	 * @param stats
	 * @param p
	 * @param so
	 *            - selection operator
	 * @param current
	 *            - population <previous+current>
	 * @param generation
	 *            - current generation count
	 * @param run
	 *            - current run count
	 * @param crossoverRate
	 *            - crossover rate
	 * @param elitismSize
	 * @param mutationRate
	 *            - mutation rate
	 * @param selectionPressure
	 * @param elitismFlag
	 * @param tournamentSize
	 *            - tournament size
	 * @return
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Population nextGenerationALPS(
			Evolve e, PopulationFitness f,
			CrossoverModule crx, 
			MutationModule mtx,
			SelectionOperation selectionOperation, 
			StatisticsCollector stats,
			ALPSReplacement alpsReplacment,
			final ArrayList<Population> current,
			ALPSLayers alpsLayers,
			int generation,
			int run) {

		Population nextGeneration = new Population();
		Population evolvingPopulation = new Population();
		Chromosome c1;
		Chromosome c2;
		ArrayList<Integer> tournamentIndividuals = new ArrayList<>();

		ArrayList<Individual> bestIndividualsOfPreviousGeneration = new ArrayList<>();
		this.populationCount = 0; // initialized from one because of addition of best individual

		/* unset parent flag on all individuals in current layer */
		alpsLayers.layers.get(alpsLayers.index).getEvolution().getCurrentPopulation().unsetParent();
		

		System.out.println("Layer #" + alpsLayers.layers.get(alpsLayers.index).getId()
				+ " Layer Generation #" + alpsLayers.layers.get(alpsLayers.index).layerEvaluationCount
				+ " Global Generation # " + Engine.completeGenerationalCount);
		/* bottom layer - new individuals will be generated */
		if (alpsLayers.layers.get(alpsLayers.index).getIsBottomLayer()) 
		{
			evolvingPopulation = alpsLayers.layers.get(alpsLayers.index)
					.getEvolution().getCurrentPopulation();
		} else // get population of current layer
		{ 
			/*
			 * TODO Memory leak
			      evolvingPopulation = (Population) alpsLayers.layers
					.get(alpsLayers.index). // current layer
					getEvolution().getCurrentPopulation().clone();
			 */
			evolvingPopulation.addAll(alpsLayers.layers
					.get(alpsLayers.index). // current layer
					getEvolution().getCurrentPopulation().clone().getAll());
			
			evolvingPopulation.addAll(alpsLayers.layers
					.get(alpsLayers.index - 1). // previous layer
					getEvolution().getCurrentPopulation().clone().getAll());
		}

		// calculate fitness
		((FitnessExtension) f).calculateSimpleFitness(
				evolvingPopulation,
				alpsLayers.layers.get(alpsLayers.index),
				Engine.completeEvaluationCount, 
				run,
				(BasicStatistics) stats,
				e.properties, 
				true);
     
		/* ELITISM
		// create elite individuals to be added to next generation
		if (e.elitismSize > 0) { 
			
			for (int i = 0; i < e.elitismSize; i++) {
				try 
				{
					bestIndividualsOfPreviousGeneration.add(
							alpsLayers.layers.get(alpsLayers.index).getEvolution().
							getCurrentPopulation().get(((FitnessExtension) f)
									.getBestIndividualsOfGeneration().get(i)).clone());
					// increment age of best individuals by 1
					bestIndividualsOfPreviousGeneration.get(i)
							.setAge(bestIndividualsOfPreviousGeneration.get(i)
									.getAge() + 1);
					// set generation for which individual was used as elite
					// bestIndividualsOfPreviousGeneration.get(i).generationRecord = alpsLayers.layers.get(alpsLayers.index).layerCompleteGenerationCount;
					bestIndividualsOfPreviousGeneration.get(i).parentFlag = true;
					this.populationCount++; // increment population count
				} 
				catch (IndexOutOfBoundsException err) {
					continue;
				}
			}
			// add best individual of previous generation
			// nextGeneration.add(bestIndividualOfPreviousGeneration);
			nextGeneration.addAll(bestIndividualsOfPreviousGeneration);
		}
		*/
		
		/* 
		 * ELITISM 
		 * add best individual of previous generation
		 * nextGeneration.add(bestIndividualOfPreviousGeneration);
		 */
		bestIndividualsOfPreviousGeneration = performElitism(
				e, alpsLayers.layers.get(alpsLayers.index).getEvolution().getCurrentPopulation(),f).getAll();
		nextGeneration.addAll(bestIndividualsOfPreviousGeneration);
		this.populationCount += bestIndividualsOfPreviousGeneration.size();
		
		/* current.get(generation-1).size()best for nextGeneration.size() */
		while (this.populationCount < alpsLayers.layers.get(alpsLayers.index).getParameters().getPopulationSize() )
		{
			// this.randomNumber = randGen.nextDouble();
			this.randomNumber = e.random.nextDouble();

			// crossover?
			/*
			 * Individuals that are created through variation, such as by
			 * mutation or recombination, take the age value of their oldest
			 * parent plus 1 since their genetic material comes from their
			 * parents and has now been in the population for one more
			 * generation than their parents. Each generation in which an
			 * individual is used as a parent to create an offspring its age is
			 * increases by 1 since its genetic material has been used in
			 * evolution in another generation
			 */
			if ((this.randomNumber < e.crossoverRate)) {
				c1 = new Chromosome();
				c2 = new Chromosome();
				tournamentIndividuals.clear(); // clear content of tournament  individuals
				/*
				 * Perform two tournament selections and pick "best" from each
				 * using selection pressure add best individual to index 0 and 1
				 * of tournamentIndividuals
				 */
				// first tournament: select best individual
				selectionOperation.performTournamentSelection(e, alpsLayers);
				
				tournamentIndividuals.add(((FitnessExtension) f)
						.selectIndividualsBasedOnFitness(e,
								selectionOperation.getTournamentSelection(),
								e.selectionPressure).get(0));
				
				// second tournament: select best individual
				selectionOperation.performTournamentSelection(e, alpsLayers);
				tournamentIndividuals.add(((FitnessExtension) f)
						.selectIndividualsBasedOnFitness(e,
								selectionOperation.getTournamentSelection(),
								e.selectionPressure).get(0));

				/**
				 * TODO clone individuals not chromosome -- get chromosome from
				 * genetic operation c1 =
				 * evolvingPopulation.get(tournamentIndividuals
				 * .get(0)).getChromosome().clone();
				 */
				c1.setGenes((ArrayList<Gene>) evolvingPopulation
						.get(tournamentIndividuals.get(0)).getChromosome()
						.getGenes().clone()); // clone individuals
				c2.setGenes((ArrayList<Gene>) evolvingPopulation
						.get(tournamentIndividuals.get(1)).getChromosome()
						.getGenes().clone());

				/**
				 * ALPS: AGE OF parents and offsprings Offspring: age of oldest
				 * parent + 1 parent : increment by 1
				 */
				ages.clear();
				for (int i = 0; i < 2; i++) { // check if individual has not been used in current generation as a parent
					if (!evolvingPopulation.get(tournamentIndividuals.get(i)).parentFlag) { // increment age of individual 0  &  1
						ages.add(evolvingPopulation.get(
								tournamentIndividuals.get(i)).getAge() + 1);
						// increment age of parent
						evolvingPopulation.get(tournamentIndividuals.get(i))
								.setAge(ages.get(i));
					} else {
						ages.add(evolvingPopulation.get(
								tournamentIndividuals.get(i)).getAge());// dont modify age
					}
					// evolvingPopulation.get(tournamentIndividuals.get(i)).generationRecord = alpsLayers.layers.get(alpsLayers.index).layerCompleteGenerationCount;
					evolvingPopulation.get(tournamentIndividuals.get(i)).parentFlag = true;
				}
				/*
				 * add new individuals to population pass two individuals for
				 * crossover automatically determines how many individuals to
				 * add based on current population size left
				 * nextGeneration.addAll
				 * (nextGeneration,crx.performCrossoverOperation
				 * (current.get(generation -1), c1,
				 * c2,tournamentIndividuals,(current.get(generation-1).size() -
				 * this.populationCount)));
				 */
				nextGeneration
						.addAll(crx.performCrossoverOperation(
								e,
								evolvingPopulation,
								c1.clone(),
								c2.clone(),
								tournamentIndividuals,
								(alpsLayers.layers.get(alpsLayers.index).getParameters().getPopulationSize() - this.populationCount),
								ages));
				
				this.populationCount += crx.getOffsprings().size(); // increment population by number of children added
			}
			// mutation?
			if ((this.randomNumber < e.mutationRate)
					&& (this.populationCount < alpsLayers.layers.get(alpsLayers.index).getParameters().getPopulationSize())) { // apply mutation policy to the chromosomes
				
				c1 = new Chromosome();
				tournamentIndividuals.clear(); // clear content of tournament individuals

				// SelectionOperation;
				selectionOperation.performTournamentSelection(e, alpsLayers);
				// select two best tournament individuals
				/* select best individuals from tournament selection */
				tournamentIndividuals.add(((FitnessExtension) f)
						.selectIndividualsBasedOnFitness(e,selectionOperation.getTournamentSelection(),
								e.selectionPressure).get(0)); 
				
				c1.setGenes((ArrayList<Gene>) evolvingPopulation.get(tournamentIndividuals.get(0)).getChromosome()
						.getGenes().clone()); // clone individuals
				/* ALPS: AGE OF parents and offsprings Offspring: age of oldest
				 * parent + 1 parent : increment by 1
				 */
				ages.clear();
				if (!evolvingPopulation.get(tournamentIndividuals.get(0)).parentFlag) { // increment age
					ages.add(evolvingPopulation.get(
							tournamentIndividuals.get(0)).getAge() + 1);
					// increment age of parent
					evolvingPopulation.get(tournamentIndividuals.get(0)).setAge(ages.get(0));
				} else {
					ages.add(evolvingPopulation.get(
							tournamentIndividuals.get(0)).getAge());
				}
				// evolvingPopulation.get(tournamentIndividuals.get(0)).generationRecord = alpsLayers.layers.get(alpsLayers.index).layerCompleteGenerationCount;
				evolvingPopulation.get(tournamentIndividuals.get(0)).parentFlag = true;
				/* add new individual to population
				 * nextGeneration.addAll(nextGeneration
				 * ,mtx.performMutationOperation(current.get(generation -1),
				 * c1,tournamentIndividuals.get(0)));
				 */
				nextGeneration.addAll(mtx.performMutationOperation(
						e,
						evolvingPopulation, 
						c1.clone(),
						tournamentIndividuals.get(0), 
						ages));

				this.populationCount++; // increment population
			}
		}

		/* 
		 * select only first half of parsed population
		 * alpsReplacment.consolidatePopulation(alpsLayers,evolvingPopulation);
		 * alpsLayers.layers.get(alpsLayers.index).getEvolution().setCurrentPopulation
		 * (evolvingPopulation);
		 */
        
		return alpsReplacment.performAgeLayerMovements(e, alpsLayers,
				nextGeneration,selectionOperation);

		// return nextGeneration;

	}

}
