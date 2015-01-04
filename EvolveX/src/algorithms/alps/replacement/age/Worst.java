package algorithms.alps.replacement.age;

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
public class Worst  extends ALPSReplacement{

	private int individualID;
	
	public Worst() 
	{
	}

	public String toString()
	{
		return "Reverse Tournament Replacement";
	}
	
	@Override
	public Population performAgeLayerMovements(ALPSLayers alpsLayers,
			Population current) {
		
		Population higherPop  = new Population();
		Population deleteList = new Population();
		
		if(alpsLayers.index < (alpsLayers.layers.size()-1) )
		{
	      //get population of next higher layer
		  higherPop = (Population) alpsLayers.layers.get(alpsLayers.index+1).
				 getEvolution().getCurrentPopulation();
		
		  for(int i=0;i<current.size();i++)
		  { 
			current.get(i).parentFlag = false; //unset parameter so that age will be incremented when individual is used as a parent
			
			if(current.get(i).getAge() >= alpsLayers.layers.get(alpsLayers.index).getMaxAge() )
			{ 
				//fill higher layer with individuals that fall within its age limit
				if(higherPop.size() < alpsLayers.layers.get(alpsLayers.index+1).getParameters().getPopulationSize())
				{ 
					alpsLayers.layers.get(alpsLayers.index+1).getEvolution().
					getCurrentPopulation().add(current.get(i));
				}
				else //if(higherPop.size() > 0) //once higher layer is filled, do selective replacement based on new individuals that have higher age than in the individual in the  higher layer
				{
					this.individualID = worseFitIndividual(higherPop,current.get(i));
					if(this.individualID < higherPop.size())
					{
						alpsLayers.layers.get(alpsLayers.index+1).getEvolution().
						getCurrentPopulation().set(this.individualID,current.get(i));
					}
				}
				//delete individuals that are older than their age and cant replace others in higher layers
				deleteList.add(current.get(i)); 
			}
		  }
		  //remove all individuals older than current layer
		  for(int id=0;id<deleteList.size();id++)
		  {
			current.remove(deleteList.get(id));
		  }
		/*
		System.out.println("DeleteList "+ deleteList.size()+ " Current1: "+current.size()+ " Current2: "+ alpsLayers.layers.get(alpsLayers.index).getEvolution().
				getCurrentPopulation().size()+
				" Next: "+alpsLayers.layers.get(alpsLayers.index+1).getEvolution().
				getCurrentPopulation().size()+" max age layer:  "+alpsLayers.layers.get(alpsLayers.index).getMaxAge()); //System.exit(0);
		*/
		}
		return current;
	}
	
	
	

}
