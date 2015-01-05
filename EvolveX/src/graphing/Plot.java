package graphing;

import individuals.Chromosome;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Line2D;
import java.util.Properties;

import parameter.Parameters;
import util.Constants;


@SuppressWarnings("serial")
public class Plot extends javax.swing.JFrame{

	private static Chromosome chrom;
	private static Properties prop;
	protected static String propertiesFilePath;
    protected Parameters initialiser;
    protected static Properties properties;
	
	public Plot()
	{
		// TODO Auto-generated constructor stub
		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setSize(600, 600);
		setLocationRelativeTo(null);
		 
		/*
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		dataset.setValue(10,"1", "x");
		dataset.setValue(20,"1", "x");
		dataset.setValue(30,"1", "x");
		dataset.setValue(40,"1", "x");
		
		JFreeChart chart = ChartFactory.createLineChart("first jchart", "categoryAxisLabel", "valueAxisLabel", dataset,PlotOrientation.HORIZONTAL,true, true,true);
		CategoryPlot p = chart.getCategoryPlot();
		p.setRangeGridlinePaint(Color.black);
		ChartFrame frame = new ChartFrame("whatever line",chart);
		frame.setVisible(true);
		frame.setSize(450,350);
		*/
		
	}
	
	public Plot(Chromosome c,Properties p) 
	{
		setChromosome(c);
		setProperties(p);
		
		new Plot().setVisible(true);
		//TODO code application logic here
        //new Run(args);
	}
	
	
	public void paint(Graphics g)
	{
		Graphics2D g2 = (Graphics2D) g;
		
		//convertToChromosome();
		drawRoute(getChromosome(),getProperties(),g2);
		 
	}
	
	/*
	public void convertToChromosome()
	{
		Chromosome chrom = new Chromosome();
	
		int [] ch = {56, 55, 25, 30, 80, 34, 51, 77, 78, 4, 69, 81, 13, 29, 27, 41, 59, 14, 96, 95, 7, 90, 54, 1, 28, 2, 52, 10, 82, 79, 35, 36, 72, 66, 67, 21, 33, 31, 71, 70, 53, 89, 32, 11, 91, 64, 65, 50, 37, 47, 48, 49, 20, 12, 63, 8, 83, 9, 46, 18, 85, 6, 84, 19, 61, 97, 100, 60, 98, 93, 38, 99, 94, 86, 62, 17, 87, 39, 45, 92, 101, 15, 43, 44, 16, 58, 88, 3, 42, 23, 75, 74, 22, 73, 76, 57, 24, 68, 40, 5, 26};
		
		for(int i=0;i< ch.length;i++)
		{
			chrom.add(ch[i]);
		}
		setChromosome(chrom);
		
	}
	*/
	
	 private void drawRoute(Chromosome c,Properties p,Graphics2D g2)
     { 
		 
      	/*
      	 * node.1.x         = 40.00
         * node.1.y         = 50.00
      	 */
      	String[] cord0 = new String[6];
      	String[] cord1 = new String[6];
      	String[] cord2 = new String[6];
      	
      	String[] params = new String[6];
      	int j;
      	double sum = 0;
      	int incx = 1;
      	int incy = 1;
      	int addx = 0;
      	int addy = 0;
      	
      	
		int v = this.getHeight()/2;
	    int h = this.getWidth()/2;
	   
      	
     	/*
     	 * for each customer indexes cord are distributed as below
     	 * CUST NO.   XCOORD.    YCOORD.    DEMAND   READY TIME   DUE DATE   SERVICE TIME
     	 *              0          1           2         3            4            5
     	 */
  	   
  	   int vCapacity = Integer.parseInt(p.getProperty(Constants.VEHICLE_CAPACITY));
        
  	    for(int i=0; i<c.getChromosome().size();i++)
        {
  	    	
  	    	cord0    = p.getProperty(Constants.CO_ORDINATES+".1").split("\\s{1,}"); //get starting point
  	    	Point p0 = new Point();
  	    	Point p1 = new Point();
  	    	Point p2 = new Point();
  	    	
  	    	
  	    	p0.setLocation((h-Double.parseDouble(cord0[0])), (v-Double.parseDouble(cord0[1])));
  	    	//p0.setLocation(300,300);
  	    	
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
              	p1.setLocation((h-Double.parseDouble(cord1[0])) * incx + addx,(v-Double.parseDouble(cord1[1]))*incy + addy);
  	    		cord2    = p.getProperty(Constants.CO_ORDINATES+"."+c.getChromosome().get(j)).split("\\s{1,}");
      	    	p2.setLocation((h-Double.parseDouble(cord2[0])) * incx + addx,(v-Double.parseDouble(cord2[1]))*incy + addy);
      	    	
  	    		sum+=demand;
  	    		Line2D lin1 = new Line2D.Float(p1,p2);
  	    		g2.draw(lin1);
  	    	}
  	    	else
  	    	{ 
  	    		cord1    = p.getProperty(Constants.CO_ORDINATES+"."+c.getChromosome().get(j)).split("\\s{1,}"); //new beginning point
              	p1.setLocation((h-Double.parseDouble(cord1[0]))*incx + addx,(v-Double.parseDouble(cord1[1]))*incy + addy);
  	    		cord2    = (j==0)?p.getProperty(Constants.CO_ORDINATES+"."+c.getChromosome().get(0)).split("\\s{1,}"):
  	    			              p.getProperty(Constants.CO_ORDINATES+"."+c.getChromosome().get(j-1)).split("\\s{1,}"); //last point before new route
  	    		
      	    	p2.setLocation((h-Double.parseDouble(cord2[0]))*incx +addx,(v-Double.parseDouble(cord2[1]))*incy + addy);
  	    		sum = demand;
  	    		Line2D lin1 = new Line2D.Float(p0,p2);
  	    		Line2D lin2 = new Line2D.Float(p0,p1);
  	    		g2.draw(lin1);
  	    		g2.draw(lin2);
  	    	}
  	    	
           }
  	   
     }
	

	/**
	 * @return the chrom
	 */
	public static Chromosome getChromosome() {
		return chrom;
	}


	/**
	 * @param chrom the chrom to set
	 */
	public static void setChromosome(Chromosome c) {
		chrom = c;
	}


	/**
	 * @return the prop
	 */
	public static Properties getProperties() {
		return prop;
	}


	/**
	 * @param prop the prop to set
	 */
	public static void setProperties(Properties p) {
		prop = p;
	}
	

}