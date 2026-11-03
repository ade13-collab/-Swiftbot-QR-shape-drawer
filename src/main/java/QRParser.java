
import java.util.ArrayList;
import java.util.List;

public class QRParser {

    public static List<ShapeCommand> parse(String qr) {

        List<ShapeCommand> list = new ArrayList<>();

        if (qr == null || qr.trim().isEmpty()) {
            return list;
        }

        String[] shapes = qr.split("&");

        for (String shape : shapes) {

            shape = shape.trim();

            if (shape.isEmpty()) {
                continue;
            }

            String[] parts = shape.split(":");
            String type = parts[0].trim();

            if (!type.equalsIgnoreCase("S") && !type.equalsIgnoreCase("T")) {
                continue;
            }

            int[] params = new int[parts.length - 1];
            boolean valid = true;

            for (int i = 1; i < parts.length; i++) {
                try {
                    params[i - 1] = Integer.parseInt(parts[i].trim());
                } catch (NumberFormatException e) {
                    valid = false;
                    break;
                }
            }

            if (valid) {
                list.add(new ShapeCommand(type, params));
            }
        }

        return list;
    }
}
