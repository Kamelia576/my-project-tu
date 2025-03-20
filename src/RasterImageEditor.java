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
                    System.out.println("No images loaded in the session.");
                }
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
        System.out.println("exit - Exits the program");
        System.out.println("help - Shows available commands");
    }
}