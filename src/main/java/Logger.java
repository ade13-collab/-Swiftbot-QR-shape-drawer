
import java.util.*;
import java.io.*;

public class Logger {

    private int squareCount = 0;
    private int triangleCount = 0;

    private double totalTime = 0;
    private int drawCount = 0;

    private String largestShape = "";
    private double largestArea = 0;

    private List<String> logEntries = new ArrayList<>();

    public void log(ShapeCommand cmd, long time) {

        drawCount++;
        totalTime += time;

        if (cmd.type.equalsIgnoreCase("S")) {

            squareCount++;

            int side = cmd.params[0];
            double area = side * side;

            if (area > largestArea) {
                largestArea = area;
                largestShape = "Square: " + side;
            }

            logEntries.add("Square: " + side + " cm (time: " + time + " ms)");
        }

        if (cmd.type.equalsIgnoreCase("T")) {

            triangleCount++;

            int a = cmd.params[0];
            int b = cmd.params[1];
            int c = cmd.params[2];

            double[] angles = ShapeDrawer.getTriangleAngles(a, b, c);

            double s = (a + b + c) / 2.0;
            double area = Math.sqrt(s * (s - a) * (s - b) * (s - c));

            if (area > largestArea) {
                largestArea = area;
                largestShape = "Triangle: " + a + ", " + b + ", " + c;
            }

            logEntries.add(
                "Triangle: " + a + ", " + b + ", " + c +
                " cm (angles: " + angles[0] + ", " + angles[1] + ", " + angles[2] +
                "; time: " + time + " ms)"
            );
        }
    }

    public void writeLog() {

        double averageTime = (drawCount == 0) ? 0 : totalTime / drawCount;

        String mostFrequentShape;
        int mostFrequentCount;

        if (squareCount > triangleCount) {
            mostFrequentShape = "Square";
            mostFrequentCount = squareCount;
        } else if (triangleCount > squareCount) {
            mostFrequentShape = "Triangle";
            mostFrequentCount = triangleCount;
        } else if (squareCount == 0 && triangleCount == 0) {
            mostFrequentShape = "None";
            mostFrequentCount = 0;
        } else {
            mostFrequentShape = "Square and Triangle (tie)";
            mostFrequentCount = squareCount;
        }

        System.out.println("\n===== DRAWING SUMMARY =====");
        System.out.println("Squares drawn: " + squareCount);
        System.out.println("Triangles drawn: " + triangleCount);
        System.out.println("Largest shape by area: " + largestShape);
        System.out.println("Most frequently drawn shape: " + mostFrequentShape + " (" + mostFrequentCount + " times)");
        System.out.println("Average draw time: " + round(averageTime) + " ms");

        try {
            File dir = new File("/data/home/pi/");
            if (!dir.exists()) {
                dir.mkdirs();
            }

            File file = new File("/data/home/pi/shapelog1.txt");
            FileWriter writer = new FileWriter(file);

            writer.write("===== Shape Log =====\n");

            for (String entry : logEntries) {
                writer.write(entry + "\n");
            }

            writer.write("\nSquares drawn: " + squareCount + "\n");
            writer.write("Triangles drawn: " + triangleCount + "\n");
            writer.write("Largest shape by area: " + largestShape + "\n");
            writer.write("Most frequently drawn shape: " + mostFrequentShape + " (" + mostFrequentCount + " times)\n");
            writer.write("Average draw time: " + round(averageTime) + " ms\n");

            writer.close();

            System.out.println("Log saved to: " + file.getAbsolutePath());

        } catch (Exception e) {
            System.out.println("Error writing log file.");
        }
    }

    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
