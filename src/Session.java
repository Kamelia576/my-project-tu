import java.util.ArrayList;
import java.util.List;

public class Session {
    private final int sessionId;
    private final List<ImageProcessor> images = new ArrayList<>();
    private ImageProcessor currentImage;

    public Session(int id) {
        this.sessionId = id;
    }

    public int getSessionId() {
        return sessionId;
    }

    public void setCurrentImage(ImageProcessor image) {
        this.currentImage = image;
        if (!images.contains(image)) {
            images.add(image);
        }
    }

    public ImageProcessor getCurrentImage() {
        return currentImage;
    }

    public List<ImageProcessor> getImages() {
        return images;
    }

    public void applyToAllGrayscale() {
        for (ImageProcessor img : images) {
            if (img.getFormat().equals("P3")) {
                img.grayscale();
            }
        }
    }

    public void applyToAllMonochrome() {
        for (ImageProcessor img : images) {
            if (img.getFormat().equals("P2")) {
                img.monochrome();
            }
        }
    }

    public void applyToAllNegative() {
        for (ImageProcessor img : images) {
                img.negative();
        }
    }
    public void applyToAllConvert(String targetFormat) {
        for (ImageProcessor img : images) {
            img.convert(targetFormat);
        }
    }


    public void printInfo() {
        System.out.println("Session ID: " + sessionId);
        System.out.println("Images in session:");
        for (ImageProcessor img : images) {
            System.out.println("- " + img.getFilename() + " (" + img.getFormat() + ")");
        }
    }
    public void createCollage(String direction, String name1, String name2, String outName) {
        ImageProcessor img1 = null, img2 = null;

        for (ImageProcessor img : images) {
            if (img.getFilename().equals(name1)) img1 = img;
            if (img.getFilename().equals(name2)) img2 = img;
        }

        if (img1 == null || img2 == null) {
            System.out.println("One or both images not found in the session.");
            return;
        }

        if (!img1.getFormat().equals(img2.getFormat()) ||
                img1.getWidth() != img2.getWidth() ||
                img1.getHeight() != img2.getHeight()) {
            System.out.println("Images must have same format and dimensions.");
            return;
        }

        List<String> newPixels = new ArrayList<>();

        if (direction.equals("horizontal")) {
            for (int i = 0; i < img1.getPixelData().size(); i++) {
                String row1 = img1.getPixelData().get(i);
                String row2 = img2.getPixelData().get(i);
                newPixels.add(row1 + " " + row2);
            }
        } else if (direction.equals("vertical")) {
            newPixels.addAll(img1.getPixelData());
            newPixels.addAll(img2.getPixelData());
        } else {
            System.out.println("Invalid direction. Use horizontal or vertical.");
            return;
        }

        ImageProcessor out = new ImageProcessor(outName);
        out.setFormat(img1.getFormat());
        out.setWidth(direction.equals("horizontal") ? img1.getWidth() * 2 : img1.getWidth());
        out.setHeight(direction.equals("vertical") ? img1.getHeight() * 2 : img1.getHeight());
        out.setMaxColor(img1.getMaxColor());
        out.setPixelData(newPixels);

        images.add(out);
        currentImage = out;

        System.out.println("Collage created and added as: " + outName);
    }

}
