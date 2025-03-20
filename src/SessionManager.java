import java.util.ArrayList;
import java.util.List;

public class SessionManager {
    private int sessionId = 1;
    private List<ImageProcessor> images = new ArrayList<>();

    public void loadImage(String filename) {
        ImageProcessor image = new ImageProcessor(filename);
        if (image.load()) {
            images.add(image);
            System.out.println("Session with ID: " + sessionId + " started");
            System.out.println("Image \"" + filename + "\" added");
        } else {
            System.out.println("Error: Failed to load image " + filename);
        }
    }

    public void printSessionInfo() {
        System.out.println("Session ID: " + sessionId);
        System.out.print("Loaded images: ");
        for (ImageProcessor img : images) {
            System.out.print(img.getFilename() + " ");
        }
        System.out.println();
    }
        public List<ImageProcessor> getImages() {
    return images;

    }
}
