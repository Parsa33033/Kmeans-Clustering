package application;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

public class LayoutController 
{
  //field
	private List<Double[]> points = new ArrayList<>();
	private List<Double[]> clusterPoints = new ArrayList<>();
	@FXML
	private ScatterChart<Double, Double> chart;
	@FXML
	private Label lable;
	@FXML
	private NumberAxis xAxis;
	@FXML
	private NumberAxis yAxis;
	@FXML
	private Button AddPoints;
	@FXML
	private Button chooseClusters;
	@FXML
	private Button runKmeansAlgorithm;
	XYChart.Series<Double, Double> series;
	XYChart.Series<Double, Double> clusters;
	XYChart.Series newCluster ;
	List<XYChart.Series<Double, Double>> pointsOnChart;
	private Map<Double[], List<Double[]>> pointsMap = new HashMap<>();
	
  //methods
	
	
	@FXML
	public void initialize()
	{
		chooseClusters.setDisable(true);
		runKmeansAlgorithm.setDisable(true);
	}//initailitze method
	
	
	public LayoutController()
	{
		
	}//constructor
	
	
	
	@FXML
	public void button()
	{ 
		try
		{
			chart.setTitle("chart");
			series = new XYChart.Series<>();
			series.setName("Instances");
			final Node background = chart.lookup(".chart-plot-background");
			background.setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent e)
				{
					Double[] i = new Double[2];
					i[0] = (Double)xAxis.getValueForDisplay(e.getX());
					i[1] = (Double)yAxis.getValueForDisplay(e.getY());
					points.add(i);
					series.getData().add(new XYChart.Data(xAxis.getValueForDisplay(e.getX()), yAxis.getValueForDisplay(e.getY())));
				}
			});
			
			chart.getData().addAll(series);
		}//try
		catch(Exception e)
		{
			e.printStackTrace();
		}//catch
		finally
		{
			chooseClusters.setDisable(false);
			AddPoints.setDisable(true);
		}
	}//button
	
	

	@FXML
	public void clustering()
	{
		clusters = new XYChart.Series<>();
		clusters.setName("clusters");
		final Node background1 = chart.lookup(".chart-plot-background");
		background1.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e)
			{
				Double[] i = new Double[2];
				i[0] = (Double)xAxis.getValueForDisplay(e.getX());
				i[1] = (Double)yAxis.getValueForDisplay(e.getY());
				clusterPoints.add(i);
				clusters.getData().add(new XYChart.Data(xAxis.getValueForDisplay(e.getX()), yAxis.getValueForDisplay(e.getY())));
			}
		});
		chart.getData().addAll(clusters);
		runKmeansAlgorithm.setDisable(false);
		chooseClusters.setDisable(true);
	}//clustering
	
	
	
	@FXML
	public void kmeans()
	{
		
		pointsMap = new HashMap<>();
		chart.getData().removeAll(clusters);
		chart.getData().removeAll(series);
		chart.getData().removeAll(newCluster);
		for(Double[] i : points)
		{
			System.out.println("point: "+ i[0] + " " + i[1]);
		}
		for(Double[] i : clusterPoints)
		{
			System.out.println("Cluster point: "+ i[0] + " " + i[1]);
		}
		Kmeans kmeans = new Kmeans(points, clusterPoints);
		List<Double[]> newClusterPoints = kmeans.getNewClusterPoints();
		findClosestPointsToClusters(newClusterPoints);
		Iterator iter = pointsMap.keySet().iterator();
		System.out.println();
		newCluster = new XYChart.Series<>();
		newCluster.getData().removeAll();
		newCluster.setName("Clusters");
		pointsOnChart = new ArrayList<>();
		while(iter.hasNext())
		{
			Double[] next =(Double[]) iter.next();
			newCluster.getData().add(new XYChart.Data(next[0], next[1]));
			System.out.print("cluster " + next[0] +"  "+ next[1]);
			System.out.println("===>--->");
			XYChart.Series newPointsForCluster = new XYChart.Series();
			for(int i = 0 ; i<pointsMap.get(next).size() ; i++)
			{
				newPointsForCluster.getData().add(new XYChart.Data(pointsMap.get(next).get(i)[0], pointsMap.get(next).get(i)[1]));
				System.out.println(pointsMap.get(next).get(i)[0]+ " " + pointsMap.get(next).get(i)[1]);
			}
			pointsOnChart.add(newPointsForCluster);
		}
		System.out.println(pointsOnChart.size()+" is size");
		chart.getData().add(newCluster);
		chart.getData().addAll(pointsOnChart);
		clusterPoints = newClusterPoints;
	}//kmeans method
	
	
	
	public void findClosestPointsToClusters(List<Double[]> newClusterPoints)
	{
		for(int i = 0 ; i<newClusterPoints.size() ; i++)
		{
			pointsMap.put(newClusterPoints.get(i), new ArrayList<Double[]>());
		}
		List<Double> dist = new ArrayList<Double>();
		double temp = 0;
		for(int i = 0 ; i<points.size() ; i++)
		{
			dist = new ArrayList<Double>();
			for(int j = 0 ; j<newClusterPoints.size() ; j++)
			{
				temp = Math.pow(newClusterPoints.get(j)[0] - points.get(i)[0], 2) + 
						Math.pow(newClusterPoints.get(j)[1] - points.get(i)[1], 2);
				dist.add(temp);
			}
			Double[] clusterTemp = newClusterPoints.get(dist.indexOf(Collections.min(dist)));
			
			pointsMap.get(clusterTemp).add(points.get(i));
			pointsMap.put(clusterTemp, pointsMap.get(clusterTemp));
		}
	}//findClosestPointsToClusters

}//class LayoutController
