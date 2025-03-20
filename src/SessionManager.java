import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SessionManager {
    private List<ImageProcessor> images = new ArrayList<>();

    public void loadImage(String filename) {
        File file = new File(System.getProperty("user.dir") + "/src/" + filename);

        System.out.println("DEBUG: Attempting to load file: " + file.getAbsolutePath());
        if (!file.exists()) {
            System.out.println("Error: Cannot find file " + file.getAbsolutePath());
            return;
        }
        ImageProcessor imageProcessor = new ImageProcessor(file.getAbsolutePath());
        if (imageProcessor.load()) {
            images.add(imageProcessor);
            System.out.println("Image successfully loaded: " + filename);
        } else {
            System.out.println("Failed to load image: " + filename);
        }
    }

    public List<ImageProcessor> getImages() {
        return images;
    }

    public void printSessionInfo() {
        if (images.isEmpty()) {
            System.out.println("No images loaded in the session.");
        } else {
            System.out.println("Loaded Images:");
            for (ImageProcessor image : images) {
                System.out.println("- " + image.getFilename());
            }
        }
    }
}