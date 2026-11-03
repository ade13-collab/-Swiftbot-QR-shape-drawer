# SwiftBot QR Shape Drawer

A Java program for a SwiftBot robot that reads a QR code and physically draws the squares and triangles it describes. Built for CS1814 Software Implementation (Task 6: Draw Shape) at Brunel University London.

## What it does

1. Scans a QR code using the SwiftBot API
2. Parses the text into shape commands
3. Validates each shape before anything is drawn
4. Drives the robot to draw each valid shape
5. Logs every shape and saves a summary when you exit

## QR command format

Shapes are separated by `&` and values by `:`.

- Square: `S:<side>` for example `S:30`
- Triangle: `T:<a>:<b>:<c>` for example `T:30:40:50`
- Several shapes in one code: `S:30&T:30:40:50`

Rules enforced by the validator:

- 1 to 5 shapes per QR code
- Every side must be between 15 cm and 85 cm
- Triangles must satisfy the triangle inequality (each side shorter than the sum of the other two)

Invalid shapes are reported with a clear error message and skipped.

## Project structure

- `Main.java`: console menu loop, QR scanning and overall flow
- `QRParser.java`: turns the QR text into `ShapeCommand` objects
- `Validator.java`: range checks and the triangle inequality check
- `ShapeCommand.java`: simple data class for a shape type and its values
- `ShapeDrawer.java`: converts shapes into robot movement using calibrated timing and calculated triangle angles
- `Logger.java`: records each shape and its draw time, then writes a summary (squares and triangles drawn, largest shape by area, most frequent shape, average draw time) to a log file

## Requirements

- A SwiftBot robot
- Java and Maven
- SwiftBot API 6.0.0, pulled from Brunel's Maven repository (configured in `pom.xml`)

Movement timing constants (`MS_PER_CM`, `MS_PER_DEGREE`) in `ShapeDrawer.java` need calibrating for each robot.

## How to run

1. Build with `mvn package`
2. Run `Main` on the SwiftBot
3. Press `Y` to scan a QR code, or `X` to save the log and exit

## Possible improvements

- Unit tests for `QRParser` and `Validator`, which don't need the robot hardware
- More shape types

## Author

Adetunji Dada
