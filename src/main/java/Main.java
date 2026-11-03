import swiftbot.*;
import java.util.*;
import java.io.*;
import java.awt.image.BufferedImage;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        Logger logger = new Logger();

        displayStartUI();

        while (true) {

            System.out.print("Press Y to scan QR or X to exit: ");
            String choice = scanner.nextLine().trim();

            if (choice.equalsIgnoreCase("X")) {
                displayExitUI();
                logger.writeLog();
                System.out.println("Log file saved.");
                break;
            }

            if (!choice.equalsIgnoreCase("Y")) {
                displayErrorUI(
                    "Invalid input.",
                    "Press Y to scan or X to exit."
                );
                continue;
            }

            try {
                System.out.println("Scanning QR code...");

                BufferedImage img = SwiftBotAPI.INSTANCE.getQRImage();
                String input = SwiftBotAPI.INSTANCE.decodeQRImage(img);

                if (input == null || input.trim().isEmpty()) {
                    displayErrorUI(
                        "QR code could not be read.",
                        "Please scan a valid QR code."
                    );
                    continue;
                }

                input = input.trim();

                List<ShapeCommand> commands = QRParser.parse(input);

                if (commands == null || commands.isEmpty()) {
                    displayErrorUI(
                        "No shapes were found in the QR input.",
                        "Please put the amount within the valid range: 1 to 5 shapes."
                    );
                    continue;
                }

                if (commands.size() > 5) {
                    displayErrorUI(
                        "The amount of shapes is too large.",
                        "Please put the amount within the valid range: 1 to 5 shapes."
                    );
                    continue;
                }

                for (int i = 0; i < commands.size(); i++) {
                    ShapeCommand cmd = commands.get(i);

                    if (!Validator.validate(cmd)) {

                        if (cmd.type.equalsIgnoreCase("S") && cmd.params.length == 1) {
                            if (cmd.params[0] < 15 || cmd.params[0] > 85) {
                                displayErrorUI(
                                    "The size of the shape is too large or too small.",
                                    "Valid size range: 15 cm to 85 cm."
                                );
                                continue;
                            }
                        }

                        if (cmd.type.equalsIgnoreCase("T") && cmd.params.length == 3) {
                            if (cmd.params[0] < 15 || cmd.params[0] > 85 ||
                                cmd.params[1] < 15 || cmd.params[1] > 85 ||
                                cmd.params[2] < 15 || cmd.params[2] > 85) {
                                displayErrorUI(
                                    "The size of the shape is too large or too small.",
                                    "Valid size range: 15 cm to 85 cm."
                                );
                                continue;
                            }
                        }

                        displayErrorUI(
                            "Invalid shape parameters detected.",
                            "Check the shape type, number of values, and triangle validity."
                        );
                        continue;
                    }

                    displayDrawingUI(cmd);

                    long start = System.currentTimeMillis();
                    ShapeDrawer.draw(cmd);
                    long end = System.currentTimeMillis();

                    long duration = end - start;

                    logger.log(cmd, duration);

                    displayCompletedUI(duration);

                    if (i < commands.size() - 1) {
                        System.out.println("------------------------------------------------------------");
                        System.out.println("Preparing next shape...");
                        System.out.println("SwiftBot moving 15 cm backwards.");
                        System.out.println("------------------------------------------------------------");
                        System.out.println();
                    }
                }

            } catch (Exception e) {
                displayErrorUI(
                    "Unexpected error: " + e.getMessage(),
                    "Please scan a valid QR command."
                );
            }

            System.out.println();
            System.out.println("Ready for next command.");
            System.out.println();
        }

        scanner.close();
    }

    private static void displayStartUI() {
        System.out.println("============================================================");
        System.out.println("                SWIFTBOT DRAW SHAPE PROGRAM                 ");
        System.out.println("============================================================");
        System.out.println("Press Y to scan a QR code.");
        System.out.println("Press X to save the log and exit.");
        System.out.println("============================================================");
        System.out.println();
    }

    private static void displayDrawingUI(ShapeCommand cmd) {
        System.out.println("\nDrawing shape...");
    }

    private static void displayCompletedUI(long duration) {
        System.out.println("Shape completed. Time: " + duration + " ms\n");
    }

    private static void displayErrorUI(String error, String fix) {
        System.out.println("\nERROR: " + error);
        System.out.println(fix);
        System.out.println("Please try again.\n");
    }

    private static void displayExitUI() {
        System.out.println("\nEnding program...");
    }
}