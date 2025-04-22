import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;


public class ImageProcessor {
    private String filename;
    private String format;
    private int width;
    private int height;
    private int maxColor;
    private List<String> pixelData = new ArrayList<>();
    private final List<List<String>> history = new ArrayList<>();

    public ImageProcessor(String filename) {
        this.filename = filename;
    }

    public boolean load() {
        try (BufferedReader reader = new BufferedReader(new FileReader("src/" + filename))) {
            format = reader.readLine().trim();
            String[] dimensions = reader.readLine().trim().split("\\s+");
            width = Integer.parseInt(dimensions[0]);
            height = Integer.parseInt(dimensions[1]);

            if (!format.equals("P1")) {
                maxColor = Integer.parseInt(reader.readLine().trim());
            }

            pixelData.clear();
            String line;
            while ((line = reader.readLine()) != null) {
                pixelData.add(line.trim());
            }

            System.out.println("Loaded src/" + filename + " successfully. Format: " + format);
            return true;
        } catch (IOException e) {
            System.out.println("Error loading file: " + e.getMessage());
            return false;
        }
    }

    public void save() {
        saveAs(filename);
    }

    public void saveAs(String newFilename) {
        try {
            File outputDir = new File("src/output");
            if (!outputDir.exists()) {
                outputDir.mkdirs(); // създава папката, ако не съществува
            }

            PrintWriter writer = new PrintWriter("src/output/" + newFilename);
            writer.println(format);
            writer.println(width + " " + height);
            if (!format.equals("P1")) {
                writer.println(maxColor);
            }
            for (String line : pixelData) {
                writer.println(line);
            }
            System.out.println("Image saved as: src/output/" + newFilename);
            writer.close();
        } catch (IOException e) {
            System.out.println("Error saving file: " + e.getMessage());
        }
    }


    public void displayImage() {
        if (pixelData.isEmpty()) {
            System.out.println("No image data loaded.");
            return;
        }
        for (String line : pixelData) {
            System.out.println(line);
        }
    }

    private void saveState() {
        history.add(new ArrayList<>(pixelData));
    }

    public void undo() {
        if (!history.isEmpty()) {
            pixelData = history.remove(history.size() - 1);
            System.out.println("Undo successful.");
        } else {
            System.out.println("Nothing to undo.");
        }
    }

    public void rotate(String direction) {
        saveState();
        String[][] matrix = getMatrix();
        String[][] rotated;

        if (direction.equalsIgnoreCase("left")) {
            rotated = new String[width][height];
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    rotated[width - j - 1][i] = matrix[i][j];
                }
            }
            int temp = width;
            width = height;
            height = temp;
        } else {
            rotated = new String[width][height];
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    rotated[j][height - i - 1] = matrix[i][j];
                }
            }
            int temp = width;
            width = height;
            height = temp;
        }

        pixelData.clear();
        for (String[] row : rotated) {
            StringBuilder sb = new StringBuilder();
            for (String val : row) {
                sb.append(val).append(" ");
            }
            pixelData.add(sb.toString().trim());
        }

        System.out.println("Image rotated " + direction + ".");
    }

    public void crop(int x, int y, int w, int h) {
        saveState();
        String[][] matrix = getMatrix();
        List<String> newPixelData = new ArrayList<>();
        for (int i = y; i < y + h && i < height; i++) {
            StringBuilder line = new StringBuilder();
            String[] row = matrix[i];
            for (int j = x; j < x + w && j < width; j++) {
                line.append(row[j]).append(" ");
            }
            newPixelData.add(line.toString().trim());
        }
        pixelData = newPixelData;
        width = w;
        height = h;
        System.out.println("Image cropped.");
    }

    public void invert() {
        if (!format.equals("P3")) {
            System.out.println("Invert only supported for P3 format.");
            return;
        }

        saveState();

        List<String> invertedData = new ArrayList<>();

        for (String line : pixelData) {
            String[] parts = line.trim().split("\\s+");
            StringBuilder newLine = new StringBuilder();

            for (String value : parts) {
                try {
                    int pixel = Integer.parseInt(value);
                    int inverted = maxColor - pixel;
                    newLine.append(inverted).append(" ");
                } catch (NumberFormatException e) {
                    newLine.append(value).append(" ");
                }
            }

            invertedData.add(newLine.toString().trim());
        }

        pixelData = invertedData;
        System.out.println("Invert (negative) transformation applied.");
    }


    public void grayscale() {
        if (!format.equals("P3")) {
            System.out.println("Grayscale transformation is only applicable to P3 images.");
            return;
        }

        saveState(); // За undo

        List<String> grayData = new ArrayList<>();

        for (String line : pixelData) {
            String[] parts = line.trim().split("\\s+");
            StringBuilder newLine = new StringBuilder();

            for (int i = 0; i + 2 < parts.length; i += 3) {
                try {
                    int r = Integer.parseInt(parts[i]);
                    int g = Integer.parseInt(parts[i + 1]);
                    int b = Integer.parseInt(parts[i + 2]);

                    int gray = (r + g + b) / 3;
                    newLine.append(gray).append(" ").append(gray).append(" ").append(gray).append(" ");
                } catch (NumberFormatException e) {
                    // Пропуска невалидни стойности
                }
            }

            grayData.add(newLine.toString().trim());
        }

        pixelData = grayData;
        System.out.println("Grayscale transformation applied.");
    }



    public void monochrome() {
        if (!format.equals("P2")) {
            System.out.println("Monochrome transformation is only applicable to P2 images.");
            return;
        }

        saveState(); // За undo
        List<String> updatedData = new ArrayList<>();
        int threshold = maxColor / 2;

        for (String line : pixelData) {
            String[] parts = line.trim().split("\\s+");
            StringBuilder newLine = new StringBuilder();

            for (String value : parts) {
                try {
                    int pixel = Integer.parseInt(value);
                    if (pixel < threshold) {
                        newLine.append("0 ");
                    } else {
                        newLine.append("255 ");
                    }
                } catch (NumberFormatException e) {
                    // Пропуска невалидна стойност
                }
            }

            updatedData.add(newLine.toString().trim());
        }

        pixelData = updatedData;
        System.out.println("Monochrome transformation applied.");
    }


    public void negative() {
        saveState(); // за undo
        List<String> updatedData = new ArrayList<>();

        for (String line : pixelData) {
            String[] parts = line.trim().split("\\s+");
            StringBuilder newLine = new StringBuilder();

            for (String value : parts) {
                try {
                    int pixel = Integer.parseInt(value);
                    int inverted = maxColor - pixel;
                    newLine.append(inverted).append(" ");
                } catch (NumberFormatException e) {
                    // Пропуска невалидна стойност
                }
            }

            updatedData.add(newLine.toString().trim());
        }

        pixelData = updatedData;
        System.out.println("Negative transformation applied.");
    }





    public void flipHorizontal() {
        saveState();
        List<String> flipped = new ArrayList<>();
        for (String line : pixelData) {
            String[] tokens = line.split(" ");
            StringBuilder sb = new StringBuilder();
            for (int i = tokens.length - 1; i >= 0; i--) {
                sb.append(tokens[i]).append(" ");
            }
            flipped.add(sb.toString().trim());
        }
        pixelData = flipped;
        System.out.println("Image flipped horizontally.");
    }

    public void flipVertical() {
        saveState();
        List<String> flipped = new ArrayList<>();
        for (int i = pixelData.size() - 1; i >= 0; i--) {
            flipped.add(pixelData.get(i));
        }
        pixelData = flipped;
        System.out.println("Image flipped vertically.");
    }

    public void convert(String targetFormat) {
        if (!targetFormat.equals("P1") && !targetFormat.equals("P2")) {
            System.out.println("Unsupported format: " + targetFormat);
            return;
        }

        if (format.equals(targetFormat)) {
            System.out.println("Image is already in format " + targetFormat);
            return;
        }

        saveState(); // За undo

        // Ако има цветни пиксели и преминаваме към P1 или P2, трябва да изведем предупреждение.
        if (format.equals("P3")) {
            List<String> newPixelData = new ArrayList<>();
            for (String line : pixelData) {
                String[] parts = line.trim().split("\\s+");
                StringBuilder builder = new StringBuilder();
                for (int i = 0; i + 2 < parts.length; i += 3) {
                    try {
                        int r = Integer.parseInt(parts[i]);
                        int g = Integer.parseInt(parts[i + 1]);
                        int b = Integer.parseInt(parts[i + 2]);
                        int gray = (r + g + b) / 3;
                        builder.append(gray).append(" ");
                    } catch (NumberFormatException e) {
                        builder.append(parts[i]).append(" ");
                    }
                }
                newPixelData.add(builder.toString().trim());
            }
            pixelData = newPixelData;
        }

        format = targetFormat;
        System.out.println("Image converted to " + targetFormat);
    }


    private String[][] getMatrix() {
        String[][] matrix = new String[height][];
        for (int i = 0; i < height; i++) {
            matrix[i] = pixelData.get(i).split(" ");
        }
        return matrix;
    }

    public String getFilename() {
        return filename;
    }

    public String getFormat() {
        return format;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getMaxColor() {
        return maxColor;
    }

    public List<String> getPixelData() {
        return pixelData;
    }

    public void setPixelData(List<String> data) {
        this.pixelData = data;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setMaxColor(int maxColor) {
        this.maxColor = maxColor;
    }
    public void view() {
        for (String line : pixelData) {
            System.out.println(line);
        }
    }
}
