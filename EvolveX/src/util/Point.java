package util;

import java.io.Serializable;

public class Point implements Serializable {
    
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private double x;
	private double y;
	
	public Point(double xCord, double yCord) {
		// TODO Auto-generated constructor stub
		this.x = xCord;
		this.y = yCord;
	}
	
	public double getX()
	{
		return this.x;
	}
	
	public double getY()
	{
		return this.y;
	}
	 
	public Point clone() throws CloneNotSupportedException
	{
	        return (Point) super.clone();
    }
	
	
	public double calculateEuclidianDistance(Point p2)
	{
		return Math.sqrt( Math.pow(this.x-p2.x,2) + Math.pow(this.y-p2.y,2) ); 
	}
	
	public double calculateEuclidianDistance(Point p1, Point p2)
	{
		return Math.sqrt( Math.pow(p1.x-p2.x,2) + Math.pow(p1.y-p2.y,2) ); 
	}

}