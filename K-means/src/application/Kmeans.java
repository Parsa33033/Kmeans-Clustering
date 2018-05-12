package application;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Kmeans 
{
  //field
	private List<Double[]> points;
	private List<Double[]> clusterPoints;
	private Map<Double[], List<Double[]>> clustersMap = new HashMap<>();
	private List<Double[]> newClusterPoints;
  //methods
	public Kmeans(List<Double[]> points, List<Double[]> clusterPoints)
	{
		this.points = points;
		this.clusterPoints = clusterPoints;
		this.newClusterPoints = new ArrayList<Double[]>();
		for(int i = 0 ; i<this.clusterPoints.size() ; i++)
		{
			clustersMap.put(this.clusterPoints.get(i), new ArrayList<Double[]>());
		}
		clusteringAlgorithm();
	}//constructor
	
	
	public void clusteringAlgorithm()
	{
		
		System.out.println();
		for(Double[] i : points)
		{
			System.out.println("point " + i[0] + "  " + i[1]);
			groupClosestToClusters(i);
		}
		Iterator iter = clustersMap.keySet().iterator();
		while(iter.hasNext())
		{
			Double[] temp = (Double[]) iter.next();
			System.out.println("x: " + temp[0] + " y: "+ temp[1] +  "==>");
			List<Double[]> tempList = clustersMap.get(temp);
			double tempX = 0.0;
			double tempY = 0.0;
			
			for(int i = 0 ; i< clustersMap.get(temp).size() ; i++)
			{
				tempX += tempList.get(i)[0];
				tempY += tempList.get(i)[1];
				System.out.println("\t x: " + tempList.get(i)[0] + "   y: " + tempList.get(i)[1]);
			}
			tempX /= tempList.size();
			tempY /= tempList.size();
			
			this.newClusterPoints.add(new Double[] {tempX, tempY});
		}
		
		for(int i = 0 ; i< this.newClusterPoints.size() ; i++)
		{
			System.out.println(this.newClusterPoints.get(i)[0]+ " " + this.newClusterPoints.get(i)[1]);
		}
		
	}//kmeans
	
	
	public void groupClosestToClusters(Double[] point)
	{
		List<Double> dist = new ArrayList<>();
		Double distTemp = 0.0;
		for(int i = 0 ; i < clusterPoints.size() ; i++)
		{
			distTemp = Math.pow( clusterPoints.get(i)[0] - point[0], 2) + Math.pow(clusterPoints.get(i)[1] - point[1], 2);
			dist.add(distTemp);
		}//for
		System.out.println("is closest to : "+ clusterPoints.get(dist.indexOf(Collections.min(dist)))[0] + " " +
				clusterPoints.get(dist.indexOf(Collections.min(dist)))[1]+" " +  Collections.min(dist));
		Double[] key = clusterPoints.get(dist.indexOf(Collections.min(dist)));
		clustersMap.get(key).add(point);
		clustersMap.put(key, clustersMap.get(key));
	}//groupClosestToClusters
	
	
	public List<Double[]> getNewClusterPoints()
	{
		return this.newClusterPoints;
	}
	
}//Kmeans class
