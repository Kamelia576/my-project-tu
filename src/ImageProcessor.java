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
            System.out.println("DEBUG: Read format -> '" + format + "'");
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
    }

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

    public void save() {
        saveAs(filename);  // Save използва текущото име
    }

    public void saveAs(String newFilename) {
        try {
            // Проверка: ако няма директория в името, добавяме "src/"
            String path = newFilename;
            if (!newFilename.contains("/") && !newFilename.contains("\\")) {
                path = "src/" + newFilename;
            }

            PrintWriter writer = new PrintWriter(path);
            writer.println(format);
            writer.println(width + " " + height);
            writer.println(maxColor);
            for (String line : pixelData) {
                writer.println(line);
            }
            writer.close();
            System.out.println("Image saved as: " + path);
        } catch (IOException e) {
            System.out.println("Error saving file: " + e.getMessage());
        }
    }

    public void invert() {
        if (format.equals("P3")) { // Color image
            List<String> newPixelData = new ArrayList<>();
            for (String line : pixelData) {
                StringBuilder newLine = new StringBuilder();
                String[] tokens = line.trim().split("\\s+");
                for (String token : tokens) {
                    try {
                        int value = Integer.parseInt(token);
                        int inverted = maxColor - value;
                        newLine.append(inverted).append(" ");
                    } catch (NumberFormatException e) {
                        newLine.append(token).append(" ");
                    }
                }
                newPixelData.add(newLine.toString().trim());
            }
            pixelData = newPixelData;
            System.out.println("Image colors inverted.");
        } else {
            System.out.println("Invert operation is currently supported only for P3 images.");
        }
    }
}


