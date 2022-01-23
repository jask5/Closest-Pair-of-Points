
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 *  ClosestPair Class to calculate minimum distance between two points
 * @author Jaskirat kaur
 *
 */
public class ClosestPair {

	/*
	 * It will read data from file and make a list
	 */
	public static void main(String[] args) throws Exception {
		FileReader reader = new FileReader("program2data.txt");
		BufferedReader br = new BufferedReader(reader);

		int total =Integer.parseInt(br.readLine());
		List<Point> pointsListX = new ArrayList<>();
		List<Point> pointsListY = new ArrayList<>();
		for (int i = 0; i < total; i++) {
			String inputLine = br.readLine().trim().replaceAll("  ", " ");
			String line[] = inputLine.split(" ");
			Point p1 = new Point(Double.parseDouble(line[0]), Double.parseDouble(line[1]), i);
			Point p2 = new Point(Double.parseDouble(line[0]), Double.parseDouble(line[1]), i);
			pointsListX.add(p1);
			pointsListY.add(p2);
		}
		//Sorting points of x-co-ordinates and y-co-ordinates
		Collections.sort(pointsListX, (p1,p2)-> ((p1.x-p2.x)<0)?-1:((p1.x-p2.x)>0)?1:0);
		Collections.sort(pointsListY, (p1,p2)-> ((p1.y-p2.y)<0)?-1:((p1.y-p2.y)>0)?1:0);

		int i=0;
		for (Point point : pointsListX) {
			point.pointIndex = i;
			i++;
		}
		/*
		 * i=0; for (Point point : pointsListY) { point.pointIndex = i; i++; }
		 */
		///System.out.println(pointsListX);
		//System.out.println(pointsListY);

		Result minResult = closest(pointsListX, pointsListY, total);
		double distance4Digit = Double.parseDouble(String.format("%.4f", minResult.getDistance()));
		//System.out.println("D["+minResult.getP1().pointIndex+","+minResult.getP2().pointIndex+"]: "+distance4Digit);
	}

	// Will call the function getDistance to calculate the distance between two points using euclidean distance
	private static double getDistance(Point p1, Point p2) {

		return Math.sqrt((p1.x-p2.x)*(p1.x-p2.x) + (p1.y-p2.y)*(p1.y-p2.y));

	}

	/**
	 * It will call recursively till the base case is reached then apply Brute Force
	 * @param listX sorted on the basis of x-co-ordinates
	 * @param listY sorted on the basis of y co-ordinates
	 * @param count length of the list
	 * @return
	 */
	private static Result closest(List<Point> listX, List<Point> listY, int count) {
		if(count<=3) {
			// When division reaches to minimum base case points
			// calculation using Brute Force Algorithm
			return byBruteForce(listX, listX.size());
		}
		int mid = count/2;

		Point midPoint = listX.get(mid);

		List<Point> listLeft = new ArrayList<>();
		List<Point> listRight = new ArrayList<>();

		for (int i = 0; i <mid; i++) {
			listLeft.add(listX.get(i));
		}

		for (int i = mid; i < listX.size(); i++) {
			listRight.add(listX.get(i));
		}
		//Calculating distance on left half
		Result distanceLeft = closest(listLeft, listY, mid);
		double distance4Digit = Double.parseDouble(String.format("%.4f", distanceLeft.getDistance()));
		//System.out.println("D["+distanceLeft.getP1().pointIndex+","+distanceLeft.getP2().pointIndex+"]: "+distance4Digit);
		Result distanceRight = closest(listRight, listY, count-mid);
		Result minResult = new Result();
		if(distanceLeft.getDistance()<distanceRight.getDistance()) {
			minResult = distanceLeft;
		}else {
			minResult = distanceRight;
		}



		distance4Digit = Double.parseDouble(String.format("%.4f", distanceRight.getDistance()));
		//System.out.println("D["+distanceRight.getP1().pointIndex+","+distanceRight.getP2().pointIndex+"]: "+distance4Digit);

		for (Point point1 : listLeft) {
			for (Point point2 : listRight) {
				double distance = getDistance(point1, point2);
				if(distance < minResult.getDistance()) {
					minResult.setDistance(distance);
					minResult.setP1(point1);
					minResult.setP2(point2);
				}
			}
		}
		double minResult4Digit = Double.parseDouble(String.format("%.4f", minResult.getDistance()));
		System.out.println("D["+listLeft.get(0).pointIndex+","+listRight.get(listRight.size()-1).pointIndex+"]: "+minResult4Digit);
		return minResult;


	}

	// Processing the case when closest point lies in two halves
	public static double calculateCaseY(List<Point> points, int size, double distance) {

		double minValue = distance;
		double changed = distance;
		Point p1 = null, p2 = null;
		for (int i = 0; i < size; i++) {
			for (int j = i+1; j < Math.min(size, i+1+7); j++) {

				minValue = Math.min(minValue, getDistance(points.get(i), points.get(j)));
				if(minValue!=changed) {
					changed = minValue;
					p1 = points.get(i);
					p2 = points.get(j);
				}
			}
		}

		double distance4Digit = Double.parseDouble(String.format("%.4f", minValue));
		//System.out.println("D["+p1.pointIndex+","+p2.pointIndex+"]: "+distance4Digit);
		return minValue;
	}

	/**
	 *
	 * @param listX
	 * @param count
	 * @return rerun the min distance by brute force
	 */
	public static Result byBruteForce(List<Point> listX, int count) {
		Result result = new Result();
		double r=Double.MAX_VALUE;
		for (int i = 0; i < count; i++) {
			for (int j = i+1; j < count; j++) {
				if(getDistance(listX.get(i), listX.get(j))<r){
					r= getDistance(listX.get(i), listX.get(j));
					result.setDistance(getDistance(listX.get(i), listX.get(j)));
					result.setP1(listX.get(i));
					result.setP2(listX.get(j));
				}
			}
		}
		double distance4Digit = Double.parseDouble(String.format("%.4f", result.getDistance()));
		System.out.println("D["+result.getP1().pointIndex+","+result.getP2().pointIndex+"]: "+distance4Digit);
		return result;
	}

}
