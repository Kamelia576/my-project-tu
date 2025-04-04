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
    public void crop(int startX, int startY, int cropWidth, int cropHeight) {
        if (!format.equals("P2") && !format.equals("P3")) {
            System.out.println("Crop operation is only supported for P2 and P3 formats.");
            return;
        }

        List<String> newPixelData = new ArrayList<>();
        int pixelsPerLine = (format.equals("P3")) ? width * 3 : width;
        List<String> allPixels = new ArrayList<>();

        // Обединяваме всички пиксели в един списък
        for (String line : pixelData) {
            for (String token : line.trim().split("\\s+")) {
                if (!token.isEmpty()) {
                    allPixels.add(token);
                }
            }
        }

        // Изрязване ред по ред
        for (int row = 0; row < cropHeight; row++) {
            int y = startY + row;
            if (y >= height) break;

            StringBuilder newLine = new StringBuilder();
            for (int col = 0; col < cropWidth; col++) {
                int x = startX + col;
                if (x >= width) break;

                int index = (y * width + x) * (format.equals("P3") ? 3 : 1);
                if (index < allPixels.size()) {
                    if (format.equals("P3")) {
                        newLine.append(allPixels.get(index)).append(" ");
                        newLine.append(allPixels.get(index + 1)).append(" ");
                        newLine.append(allPixels.get(index + 2)).append(" ");
                    } else {
                        newLine.append(allPixels.get(index)).append(" ");
                    }
                }
            }
            newPixelData.add(newLine.toString().trim());
        }

        // Обновяване на размери и пикселни данни
        this.width = cropWidth;
        this.height = cropHeight;
        this.pixelData = newPixelData;

        System.out.println("Image cropped successfully.");
    }
    public void rotate() {
        if (!format.equals("P2") && !format.equals("P3")) {
            System.out.println("Rotate operation is only supported for P2 and P3 formats.");
            return;
        }

        List<String> allPixels = new ArrayList<>();
        for (String line : pixelData) {
            for (String token : line.trim().split("\\s+")) {
                if (!token.isEmpty()) {
                    allPixels.add(token);
                }
            }
        }

        int newWidth = height;
        int newHeight = width;
        List<String> newPixelData = new ArrayList<>();

        if (format.equals("P3")) {
            for (int x = 0; x < width; x++) {
                StringBuilder row = new StringBuilder();
                for (int y = height - 1; y >= 0; y--) {
                    int index = (y * width + x) * 3;
                    if (index + 2 < allPixels.size()) {
                        row.append(allPixels.get(index)).append(" ")
                                .append(allPixels.get(index + 1)).append(" ")
                                .append(allPixels.get(index + 2)).append(" ");
                    }
                }
                newPixelData.add(row.toString().trim());
            }
        } else {
            for (int x = 0; x < width; x++) {
                StringBuilder row = new StringBuilder();
                for (int y = height - 1; y >= 0; y--) {
                    int index = y * width + x;
                    if (index < allPixels.size()) {
                        row.append(allPixels.get(index)).append(" ");
                    }
                }
                newPixelData.add(row.toString().trim());
            }
        }

        this.width = newWidth;
        this.height = newHeight;
        this.pixelData = newPixelData;

        System.out.println("Image rotated 90 degrees clockwise.");
    }
    public void grayscale() {
        if (!format.equals("P3")) {
            System.out.println("Grayscale operation is only supported for P3 format.");
            return;
        }

        List<String> newPixelData = new ArrayList<>();
        for (String line : pixelData) {
            StringBuilder newLine = new StringBuilder();
            String[] tokens = line.trim().split("\\s+");
            for (int i = 0; i < tokens.length; i += 3) {
                try {
                    int r = Integer.parseInt(tokens[i]);
                    int g = Integer.parseInt(tokens[i + 1]);
                    int b = Integer.parseInt(tokens[i + 2]);
                    int avg = (r + g + b) / 3;
                    newLine.append(avg).append(" ").append(avg).append(" ").append(avg).append(" ");
                } catch (Exception e) {
                    newLine.append("0 0 0 ");
                }
            }
            newPixelData.add(newLine.toString().trim());
        }

        this.pixelData = newPixelData;
        System.out.println("Image converted to grayscale.");
    }
}
