package algorithms.alps.layers;

import individuals.populations.Population;
import algorithms.alps.LayerInterface;
import algorithms.alps.system.EvolveGA;

public class Layer implements LayerInterface {

	private int maxAgeLayer;
	private boolean isBottomLayer = Boolean.FALSE;
	private boolean isActive = Boolean.FALSE;
	private InitializeParams params;
	private EvolveGA evolve;
	private int generationalCount, generations;
	private int layerId;
	public int layerEvaluationCount   = 0;
	public int layerCompleteGenerationCount = 0;
	public boolean initializerFlag = true;
	
	public Layer() 
	{
		
	}
	
	

	@Override
	public boolean getIsActive() 
	{
		return this.isActive;
	}

	@Override
	public void setIsActive(boolean status) 
	{
		this.isActive = status;
	}

	@Override
	public boolean getIsBottomLayer() 
	{
		return this.isBottomLayer;
	}

	@Override
	public void setIsBottomLayer(boolean status) 
	{
		this.isBottomLayer = status;
	}

	@Override
	public void tryMoveUp(int layer, Population pop) 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getMaxAge() 
	{
		return this.maxAgeLayer;
	}

	@Override
	public void setMaxAgeLayer(int age) 
	{
		this.maxAgeLayer = age;
	}
   
	@Override
	public void setParameters(InitializeParams i) 
	{
		this.params = i;
	}



	@Override
	public InitializeParams getParameters() 
	{
		return this.params;
	}



	@Override
	public void setEvolution(EvolveGA e) 
	{
		this.evolve = e;
	}



	@Override
	public EvolveGA getEvolution() 
	{
		return this.evolve;
	}



	@Override
	public int getGenerationalCount() 
	{
		return this.generationalCount;
	}



	@Override
	public void setGenerationalCount(int count) 
	{
		this.generationalCount = count;
	}



	@Override
	public int getGenerations() 
	{
		return this.generations;
	}



	@Override
	public void setGenerations(int count) 
	{
		this.generations = count;
	}



	@Override
	public int getId() 
	{
		return this.layerId;
	}



	@Override
	public void setId(int id) {
		this.layerId = id;
	}


   /*
	@Override
	public double getEvaluationCounter() 
	{
		return this.evaluationCounter;
	}



	@Override
	public void setEvaluationCounter(double evaluationCounter) 
	{
		this.evaluationCounter = evaluationCounter;
	}
	*/

}