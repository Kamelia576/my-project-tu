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
            case "session info":
                sessionManager.printSessionInfo();
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
        File file = new File(filename);
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
        System.out.println("exit - Exits the program");
        System.out.println("help - Shows available commands");
    }
}

