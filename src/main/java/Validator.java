public class Validator {

    public static boolean validate(ShapeCommand cmd) {

        if (cmd == null || cmd.type == null || cmd.params == null) {
            return false;
        }

        if (cmd.type.equalsIgnoreCase("S")) {

            if (cmd.params.length != 1) {
                return false;
            }

            int side = cmd.params[0];
            return side >= 15 && side <= 85;
        }

        if (cmd.type.equalsIgnoreCase("T")) {

            if (cmd.params.length != 3) {
                return false;
            }

            int a = cmd.params[0];
            int b = cmd.params[1];
            int c = cmd.params[2];

            if (a < 15 || a > 85 || b < 15 || b > 85 || c < 15 || c > 85) {
                return false;
            }

            if (a + b <= c) {
                return false;
            }

            if (a + c <= b) {
                return false;
            }

            if (b + c <= a) {
                return false;
            }

            return true;
        }

        return false;
    }
}