import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class RotateVectorPathsHelper {

    private double px = 12.0d;
    private double py = 12.0d;

    private double angle = Math.toRadians(-45d);


    public String convertSingleCommand(String conversion) {
        conversion = conversion.trim();
        String prefix = conversion.substring(0, 1);
        conversion = conversion.substring(1);
        StringBuilder result = new StringBuilder(prefix);
        String[] points = conversion.split(" ");
        for (String point : points) {
            String[] coordinates = point.split(",");
            double x = Double.parseDouble(coordinates[0]);
            double y = Double.parseDouble(coordinates[1]);
            double x1 = (x - px) * Math.cos(angle) + px - (y - py) * Math.sin(angle);
            double y1 = (x - px) * Math.sin(angle) + py + (y - py) * Math.cos(angle);
            result.append(BigDecimal.valueOf(x1).setScale(2, RoundingMode.HALF_UP)).append(",")
                    .append(BigDecimal.valueOf(y1).setScale(2, RoundingMode.HALF_UP)).append(" ");
        }

        return result.toString();
    }

    public void convertPath(String all) {
        String[] lines = all.split("\n");
        for (String line : lines) {
            System.out.println(convertSingleCommand(line));
        }
    }

    @Test
    public void launchHelper() {
        convertPath("        M19,13\n" +
                "        C19,13 12,13 12,13\n" +
                "        L12,11\n" +
                "        C12,11 19,11 19,11");
    }


}
