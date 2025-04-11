import java.io.*;
import java.util.*;

public class RasterImageEditor {
    private static boolean running = true;
    private static Scanner scanner = new Scanner(System.in);
    private static SessionManager sessionManager = new SessionManager();

    public static void main(String[] args) {
        System.out.println("Raster Image Editor Started. Type 'help' for commands.");

        while (running) {
            System.out.print("> ");
            String input = scanner.nextLine();
            processCommand(input);
        }
    }

    private static void processCommand(String input) {
        // Проверява директно за "session info" (тъй като е команда с повече от една дума)
        if (input.equalsIgnoreCase("session info")) {
            sessionManager.printSessionInfo();
            return; // Спира изпълнението тук, за да не влиза в switch
        }

        // Разделя командите на части, като приема първата дума за команда
        String[] parts = input.split(" ");
        String command = parts[0].toLowerCase();

        switch (command) {
            case "open":
                if (parts.length > 1) {
                    openFile(parts[1]);
                } else {
                    System.out.println("Usage: open <filename>");
                }
                break;

            case "load":
                if (parts.length > 1) {
                    sessionManager.loadImage(parts[1]);
                } else {
                    System.out.println("Usage: load <filename>");
                }
                break;

            case "view":
                if (!sessionManager.getImages().isEmpty()) {
                    sessionManager.getImages().get(0).displayImage();
                } else {
                    System.out.println("No images loaded to view.");
                }
                break;

            case "invert":
                if (!sessionManager.getImages().isEmpty()) {
                    sessionManager.getImages().get(0).invert();
                } else {
                    System.out.println("No images loaded in the session.");
                }
                break;

            case "rotate":
                if (!sessionManager.getImages().isEmpty()) {
                    ImageProcessor current = sessionManager.getImages().get(sessionManager.getImages().size() - 1);
                    current.rotate();
                } else {
                    System.out.println("No image loaded.");
                }
                break;

            case "grayscale":
                if (!sessionManager.getImages().isEmpty()) {
                    sessionManager.getImages().get(0).grayscale();
                } else {
                    System.out.println("No image loaded.");
                }
                break;
            case "fliph":
                if (!sessionManager.getImages().isEmpty()) {
                    sessionManager.getImages().get(0).flipHorizontal();
                } else {
                    System.out.println("No image loaded.");
                }
                break;

            case "flipv":
                if (!sessionManager.getImages().isEmpty()) {
                    sessionManager.getImages().get(0).flipVertical();
                } else {
                    System.out.println("No image loaded.");
                }
                break;

            case "convert":
                if (parts.length > 1) {
                    if (!sessionManager.getImages().isEmpty()) {
                        sessionManager.getImages().get(0).convertTo(parts[1]);
                    } else {
                        System.out.println("No image loaded.");
                    }
                } else {
                    System.out.println("Usage: convert <P1 | P2>");
                }
                break;

            case "crop":
                if (parts.length == 5) {
                    try {
                        int x = Integer.parseInt(parts[1]);
                        int y = Integer.parseInt(parts[2]);
                        int w = Integer.parseInt(parts[3]);
                        int h = Integer.parseInt(parts[4]);

                        // Извикваме crop върху последното заредено изображение
                        if (!sessionManager.getImages().isEmpty()) {
                            ImageProcessor current = sessionManager.getImages().get(sessionManager.getImages().size() - 1);
                            current.crop(x, y, w, h);
                        } else {
                            System.out.println("No image loaded.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid crop parameters.");
                    }
                } else {
                    System.out.println("Usage: crop <x> <y> <width> <height>");
                }
                break;

            case "save":
                if (!sessionManager.getImages().isEmpty()) {
                    sessionManager.getImages().get(0).save();
                } else {
                    System.out.println("No image to save.");
                }
                break;

            case "saveas":
                if (parts.length > 1) {
                    if (!sessionManager.getImages().isEmpty()) {
                        sessionManager.getImages().get(0).saveAs(parts[1]);
                    } else {
                        System.out.println("No image to save.");
                    }
                } else {
                    System.out.println("Usage: saveas <filename>");
                }
                break;

            case "session info":
                sessionManager.printSessionInfo();
                break;

            case "help":
                printHelp();
                break;

            case "exit":
                running = false;
                System.out.println("Exiting the program...");
                break;

            default:
                System.out.println("Unknown command. Type 'help' for a list of commands.");
        }
    }

    private static void openFile(String filename) {
        System.out.println("DEBUG: openFile() called with filename: " + filename);
        File file = new File(System.getProperty("user.dir") + "/src/" + filename);

        System.out.println("Checking file: " + file.getAbsolutePath());
        if (!file.exists()) {
            System.out.println("Error: File does not exist.");
            return;
        }

        System.out.println("Successfully opened " + filename);
    }

    private static void printHelp() {
        System.out.println("The following commands are supported:");
        System.out.println("open <file> - Opens a file");
        System.out.println("load <file> - Loads an image into a session");
        System.out.println("session info - Displays the current session details");
        System.out.println("view - Displays the first loaded image");
        System.out.println("invert - Inverts the colors of the currently loaded image");
        System.out.println("rotate - Rotates the image 90 degrees clockwise");
        System.out.println("grayscale - Converts a P3 image to grayscale");
        System.out.println("fliph - Flips the image horizontally (left-right)");
        System.out.println("flipv - Flips the image vertically (top-bottom)");
        System.out.println("convert <P1 | P2> - Converts a P3 image to grayscale (P2) or black-and-white (P1)");
        System.out.println("crop <x> <y> <width> <height> - Crops the image starting at (x,y) with given width and height");
        System.out.println("save - Saves the current image to its original file");
        System.out.println("saveas <file> - Saves the current image under a new filename");
        System.out.println("exit - Exits the program");
        System.out.println("help - Shows available commands");
    }
}