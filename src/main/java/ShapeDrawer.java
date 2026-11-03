import swiftbot.*;

public class ShapeDrawer {
	
	public static SwiftBotAPI swiftbot = SwiftBotAPI.INSTANCE;

    // You MUST calibrate these on your robot
    private static final int FORWARD_SPEED = 100;
    private static final int TURN_SPEED = 100;

    // Example calibration values - replace after testing
    private static final double MS_PER_CM = 120.0;
    private static final double MS_PER_DEGREE = 4;

    public static void draw(ShapeCommand cmd) {

        if (cmd.type.equalsIgnoreCase("S")) {
            drawSquare(cmd.params[0]);
        }

        if (cmd.type.equalsIgnoreCase("T")) {
            drawTriangle(cmd.params[0], cmd.params[1], cmd.params[2]);
        }
    }

    private static void drawSquare(int side) {

        System.out.println("Drawing Square with side " + side + " cm");

        for (int i = 0; i < 4; i++) {
            moveForward(side);
            turn(90);
        }
    }

    private static void drawTriangle(int a, int b, int c) {

        System.out.println("Drawing Triangle");

        double A = calculateAngle(a, b, c);
        double B = calculateAngle(b, a, c);
        double C = calculateAngle(c, a, b);

        System.out.println("Triangle angles: "
                + round(A) + "°, "
                + round(B) + "°, "
                + round(C) + "°");

        moveForward(a);
        turn(180 - A);

        moveForward(b);
        turn(180 - B);

        moveForward(c);
        turn(180 - C);
    }

    public static double[] getTriangleAngles(int a, int b, int c) {
        double A = calculateAngle(a, b, c);
        double B = calculateAngle(b, a, c);
        double C = calculateAngle(c, a, b);

        return new double[]{round(A), round(B), round(C)};
    }

    private static double calculateAngle(double a, double b, double c) {
        return Math.toDegrees(
            Math.acos((b * b + c * c - a * a) / (2 * b * c))
        );
    }

    private static double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    private static void moveForward(int distanceCm) {
        int movementTime = (int) Math.round(distanceCm * MS_PER_CM);

        System.out.println("Move forward " + distanceCm + " cm");
        System.out.println("Movement time: " + movementTime + " ms");

        swiftbot.move(FORWARD_SPEED, FORWARD_SPEED, movementTime);
    }

    private static void turn(double angle) {
        int turnTime = (int) Math.round(angle * MS_PER_DEGREE);

        System.out.println("Turn " + round(angle) + " degrees");
        System.out.println("Turn time: " + turnTime + " ms");

        swiftbot.move(TURN_SPEED, -TURN_SPEED, turnTime);
    }
}