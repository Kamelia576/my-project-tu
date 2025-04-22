import java.util.Scanner;

public class RasterImageEditor {
    private static final Scanner scanner = new Scanner(System.in);
    private static final SessionManager sessionManager = new SessionManager();

    public static void main(String[] args) {
        System.out.println("Raster Image Editor Started. Type 'help' for a list of commands.");
        while (true) {
            System.out.print("> ");
            String input = scanner.nextLine().trim();
            processCommand(input);
        }
    }

    public static void processCommand(String input) {
        if (input.isEmpty()) return;
        String[] parts = input.split(" ");
        String command = parts[0];

        switch (command) {
            case "help":
                printHelp();
                break;
            case "open":
            case "load":
                if (parts.length < 2) {
                    System.out.println("Usage: " + command + " <filename>");
                    break;
                }
                String filename = parts[1];
                ImageProcessor img = new ImageProcessor(filename);
                if (img.load()) {
                    sessionManager.createSession(img);
                } else {
                    System.out.println("Error: Failed to load image.");
                }
                break;
            case "view":
                if (sessionManager.hasActiveSession()) {
                    sessionManager.getCurrentImage().displayImage();
                } else {
                    System.out.println("No active session.");
                }
                break;
            case "save":
                if (sessionManager.hasActiveSession()) {
                    sessionManager.getCurrentImage().save();
                } else {
                    System.out.println("No active session.");
                }
                break;
            case "saveas":
                if (parts.length < 2) {
                    System.out.println("Usage: saveas <newfilename>");
                    break;
                }
                if (sessionManager.hasActiveSession()) {
                    sessionManager.getCurrentImage().saveAs(parts[1]);
                } else {
                    System.out.println("No active session.");
                }
                break;
            case "rotate":
                if (parts.length < 2) {
                    System.out.println("Usage: rotate <left|right>");
                    break;
                }
                if (sessionManager.hasActiveSession()) {
                    sessionManager.getCurrentImage().rotate(parts[1]);
                } else {
                    System.out.println("No active session.");
                }
                break;
            case "crop":
                if (parts.length < 5) {
                    System.out.println("Usage: crop x y w h");
                    break;
                }
                if (sessionManager.hasActiveSession()) {
                    try {
                        int x = Integer.parseInt(parts[1]);
                        int y = Integer.parseInt(parts[2]);
                        int w = Integer.parseInt(parts[3]);
                        int h = Integer.parseInt(parts[4]);
                        sessionManager.getCurrentImage().crop(x, y, w, h);
                    } catch (NumberFormatException e) {
                        System.out.println("Error: Invalid numbers.");
                    }
                } else {
                    System.out.println("No active session.");
                }
                break;
            case "invert":
                if (sessionManager.hasActiveSession()) {
                    sessionManager.getCurrentImage().invert();
                } else {
                    System.out.println("No active session.");
                }
                break;
            case "grayscale":
                if (sessionManager.hasActiveSession()) {
                    sessionManager.getCurrentSession().applyToAllGrayscale();
                } else {
                    System.out.println("No active session.");
                }
                break;
            case "monochrome":
                if (sessionManager.hasActiveSession()) {
                    sessionManager.getCurrentSession().applyToAllMonochrome();
                } else {
                    System.out.println("No active session.");
                }
                break;
            case "negative":
                if (sessionManager.hasActiveSession()) {
                    sessionManager.getCurrentSession().applyToAllNegative();
                } else {
                    System.out.println("No active session.");
                }
                break;
            case "flipH":
                if (sessionManager.hasActiveSession()) {
                    sessionManager.getCurrentImage().flipHorizontal();
                } else {
                    System.out.println("No active session.");
                }
                break;
            case "flipV":
                if (sessionManager.hasActiveSession()) {
                    sessionManager.getCurrentImage().flipVertical();
                } else {
                    System.out.println("No active session.");
                }
                break;
            case "convert":
                if (parts.length < 2) {
                    System.out.println("Usage: convert P1|P2");
                    break;
                }
                if (sessionManager.hasActiveSession()) {
                    sessionManager.getCurrentImage().convert(parts[1]);
                } else {
                    System.out.println("No active session.");
                }
                break;
            case "undo":
                if (sessionManager.hasActiveSession()) {
                    sessionManager.getCurrentImage().undo();
                } else {
                    System.out.println("No active session.");
                }
                break;
            case "add":
                if (parts.length < 2) {
                    System.out.println("Usage: add <image>");
                    break;
                }
                String addName = parts[1];
                ImageProcessor toAdd = new ImageProcessor(addName);
                if (toAdd.load()) {
                    sessionManager.addImageToCurrentSession(toAdd);
                }
                break;
            case "session":
                if (input.equals("session info")) {
                    if (sessionManager.hasActiveSession()) {
                        sessionManager.getCurrentSession().printInfo();
                    } else {
                        System.out.println("No active session.");
                    }
                } else {
                    System.out.println("Unknown session command.");
                }
                break;
            case "collage":
                if (parts.length < 5) {
                    System.out.println("Usage: collage <direction> <image1> <image2> <outimage>");
                    break;
                }
                if (sessionManager.hasActiveSession()) {
                    String dir = parts[1];
                    String i1 = parts[2];
                    String i2 = parts[3];
                    String out = parts[4];
                    sessionManager.getCurrentSession().createCollage(dir, i1, i2, out);
                } else {
                    System.out.println("No active session.");
                }
                break;

            case "switch":
                if (parts.length < 2) {
                    System.out.println("Usage: switch <sessionId>");
                    break;
                }
                try {
                    int sessionId = Integer.parseInt(parts[1]);
                    sessionManager.switchSession(sessionId);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid session ID.");
                }
                break;
            case "close":
                sessionManager.closeSession();
                break;
            case "exit":
                System.out.println("Goodbye!");
                System.exit(0);
                break;
            default:
                System.out.println("Unknown command. Type 'help' for a list of commands.");
        }
    }

    private static void printHelp() {
        System.out.println("Available commands:");
        System.out.println("open <file>           - Opens a file");
        System.out.println("load <file>           - Loads an image into a session");
        System.out.println("view                  - Displays the current image");
        System.out.println("save                  - Saves the current image");
        System.out.println("saveas <filename>     - Saves the image with a new name");
        System.out.println("rotate <left|right>   - Rotates the image 90 degrees");
        System.out.println("crop x y w h          - Crops the image");
        System.out.println("invert                - Inverts colors (P3 only)");
        System.out.println("grayscale             - Converts image to grayscale");
        System.out.println("monochrome            - Converts to black & white");
        System.out.println("negative              - Inverts colors (all P3)");
        System.out.println("flipH                 - Flips image horizontally");
        System.out.println("flipV                 - Flips image vertically");
        System.out.println("convert P1|P2         - Converts to P1 or P2");
        System.out.println("undo                  - Undo last transformation");
        System.out.println("add <image>           - Adds image to session");
        System.out.println("session info          - Shows current session info");
        System.out.println("collage <direction> <image1> <image2> <outimage> - Creates collage (horizontal|vertical)");
        System.out.println("switch <sessionId>    - Switch session by ID");
        System.out.println("close                 - Closes the session");
        System.out.println("exit                  - Exits the program");
    }
}
