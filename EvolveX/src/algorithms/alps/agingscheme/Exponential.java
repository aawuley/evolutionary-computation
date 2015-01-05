package algorithms.alps.agingscheme;

import java.util.ArrayList;

import util.Constants;
import algorithms.alps.AgingScheme;
import algorithms.alps.layers.Layer;

public class Exponential implements AgingScheme {

	public Exponential() {
		// TODO Auto-generated constructor stub
	}
	
	public String toString(String str)
	{
		return "polynomial aging scheme selected " + str;
	}
	
	/**
	 * 
	 * @return 2^n
	 */
	public ArrayList<Layer> agingScheme(int ageGap,int ageLayers)
	{
		ArrayList<Layer> layers = new ArrayList<>();
		this.toString(" with an age gap of "+ ageGap +" and "+ ageLayers +" layers");
		
		for(int i=0; i<ageLayers;i++)
		{
			Layer layer = new Layer();
			layer.setMaxAgeLayer((int) Math.pow(2,i)*ageGap);
			layer.setIsActive(Boolean.FALSE);
			layer.setGenerationalCount(0); //initialize generational count
			layer.setId(i);
			
			
			if(i==0)
			{
				layer.setIsBottomLayer(Boolean.TRUE);
				layer.setGenerations(layer.getMaxAge());
			}
			else
			{
				layer.setIsBottomLayer(Boolean.FALSE);
				layer.setGenerations( //get the difference between maximum of this layer and previous layer
				           layer.getMaxAge() - layers.get(i-1).getMaxAge());
			}
			
			layers.set(i, layer);
		}
		/*
		 * there is no age-limit to the last layer so as to be able to keep the 
		 * best individuals from the most promising (longest running) search
		 */
		layers.get(layers.size()-1).setMaxAgeLayer(Constants.ALPS_MAX_AGE_LAST_LAYER);
		
		return layers;
		
	}

}