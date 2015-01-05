package algorithms.alps.replacement.sfs;

import operator.operations.SelectionOperation;
import operator.operations.selection.TournamentSelection;
import util.random.MersenneTwisterFast;
import util.random.RandomGenerator;
import individuals.populations.Population;
import algorithms.alps.ALPSReplacement;
import algorithms.alps.system.ALPSLayers;

/**
 * 
 * @author anthony
 * 
 * makes tournament selectiion, use a percentage selection for worse individual replacement
 * else a random individual is selected out of the tournament individulas to be replaced
 *
 */
public class ReverseTournamentWorst  extends ALPSReplacement{

	private int individualID;
	
	public ReverseTournamentWorst() 
	{
	}

	public String toString()
	{
		return "Reverse Tournament Replacement";
	}
	
	@Override
	public Population performAgeLayerMovements(ALPSLayers alpsLayers,
			Population current) {
		
		Population higherPop = null;
		Population deleteList = new Population();
		SelectionOperation selectionOperation = new TournamentSelection();
		
		if(alpsLayers.index < (alpsLayers.layers.size()-1) ){
	    //get population of next higher layer
		higherPop = (Population) alpsLayers.layers.get(alpsLayers.index+1).
				getEvolution().getCurrentPopulation();
		
		for(int i=0;i<current.size();i++)
		{
			if(current.get(i).getAge() > alpsLayers.layers.get(alpsLayers.index).getMaxAge() )
			{ 
				//fill higher layer with individuals that fall withing its age limit
				if(higherPop.size() < alpsLayers.layers.get(alpsLayers.index+1).getParameters().getPopulationSize())
				{
					alpsLayers.layers.get(alpsLayers.index+1).getEvolution().
					getCurrentPopulation().add(current.get(i));
				}
				else if(higherPop.size() > 0) //once higher layer is filled, do selective replacement based on new individuals that have higher age than in the individual in the  higher layer
				{
					@SuppressWarnings("unused")
					RandomGenerator randGen = new RandomGenerator(); 
			        MersenneTwisterFast mtf = new MersenneTwisterFast();
			        mtf.setSeed(alpsLayers.layers.get(alpsLayers.index).getParameters().getSeed()); //set seed
			        
			        //perform tournament selection on higher layer
					selectionOperation.performTournamentSelection(alpsLayers,higherPop.size(),
						 alpsLayers.layers.get(alpsLayers.index+1).getParameters().getTournamentSize());
			        
					this.individualID = worseTournamentIndividual(higherPop,selectionOperation.getTournamentSelection());
					
					alpsLayers.layers.get(alpsLayers.index+1).getEvolution().getCurrentPopulation().
					   set(this.individualID,current.get(i));
					deleteList.add(current.get(i));
				}
			}
		}
		//remove all individuals older than current layer
		for(int id=0;id<deleteList.size();id++)
		{
			current.remove(deleteList.get(id));
		}
		/*
		System.out.println("DeleteList "+ deleteList.size()+ " -- Current!! "+current.size()+
				" Next "+alpsLayers.layers.get(alpsLayers.index+1).getEvolution().
				getCurrentPopulation().size()+" maxi age layer "+alpsLayers.layers.get(alpsLayers.index).getMaxAge()); //System.exit(0);
		*/
		}
		
		return current;
	}
	
	
	

}