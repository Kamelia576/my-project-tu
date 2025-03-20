import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ImageProcessor {
    private String filename;
    private String format;
    private int width;
    private int height;
    private int maxColor;
    private List<String> pixelData = new ArrayList<>();

    public ImageProcessor(String filename) {
        this.filename = filename;
    }

    public boolean load() {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            format = reader.readLine().trim();
            if (!format.equals("P1") && !format.equals("P2") && !format.equals("P3")) {
                System.out.println("Unsupported format: " + format);
                return false;
            }

            String[] dimensions = reader.readLine().trim().split(" ");
            width = Integer.parseInt(dimensions[0]);
            height = Integer.parseInt(dimensions[1]);

            if (!format.equals("P1")) {
                maxColor = Integer.parseInt(reader.readLine().trim());
            }

            String line;
            while ((line = reader.readLine()) != null) {
                pixelData.add(line.trim());
            }

            System.out.println("Loaded " + filename + " successfully. Format: " + format);
            return true;
        } catch (IOException e) {
            System.out.println("Error loading file: " + e.getMessage());
            return false;
        }
    }

    public String getFilename() {
        return filename;


        public void displayImage() {
    if (pixelData.isEmpty()) {
        System.out.println("No image data loaded.");
        return;
    }

    System.out.println("Displaying image: " + filename);
    for (String line : pixelData) {
        System.out.println(line);
    }
}

    }
}
