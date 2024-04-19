import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;

class ListManager {
    private static ArrayList<String> list = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);
    private static boolean needsToBeSaved = false;
    private static String currentFileName = "";

    public static void main(String[] args) {
        boolean running = true;
        while (running) {
            printList();
            showMenu();
            String choice = getSafeInput("[AaDdVvOoSsCcQq]", "Choose an option: ");
            switch (choice.toUpperCase()) {
                case "A":
                    addItem();
                    break;
                case "D":
                    deleteItem();
                    break;
                case "V":
                    printList();
                    break;
                case "O":
                    openList();
                    break;
                case "S":
                    saveList();
                    break;
                case "C":
                    clearList();
                    break;
                case "Q":
                    running = quitProgram();
                    break;
                default:
                    System.out.println("Invalid option try again."); //Tell them they entered a invalid option
            }
        }
    }

   //List of options
    private static void showMenu() {
        System.out.println("\nMenu Options:");
        System.out.println("A - Add item to the list");
        System.out.println("D - Delete item from the list");
        System.out.println("V - View list");
        System.out.println("O - Open list file from disk");
        System.out.println("S - Save current list to disk");
        System.out.println("C - Clear list");
        System.out.println("Q - Quit program");
    }

    private static void addItem() {
        System.out.print("Enter Item: "); //ask to enter items
        String item = scanner.nextLine();
        if (!item.isEmpty()) {
            list.add(item);
            needsToBeSaved = true;
        }
    }

    private static void deleteItem() {
        if (list.isEmpty()) {
            System.out.println("Nothing is in the list"); // Tell them list is empty
            return;
        }
        printNumberedList();
        System.out.print("Enter number of the item you want to delete (or 0 to cancel): "); // Ask number of the items they want to delete
        int itemNumber = getSafeRangedInt(0, list.size(), "Choose an item number you want to delete: "); // Ask what item they want to delete
        if (itemNumber > 0) {
            list.remove(itemNumber - 1);
            needsToBeSaved = true;
        }
    }

    private static void printList() {
        if (list.isEmpty()) {
            System.out.println("List is empty."); // Tell them list is empty
        } else {
            System.out.println("Your List:"); // Give them their list
            for (int i = 0; i < list.size(); i++) {
                System.out.println((i + 1) + ". " + list.get(i));
            }
        }
    }

    private static boolean quitProgram() {
        if (needsToBeSaved && getSafeYNConfirm("You did not save changes. Want to save them? (y/n): ")) {
            saveList(); // Tell them they didn't save their changes and ask if they want to.
        }
        return !getSafeYNConfirm("You sure you want to quit? (y/n): "); // Confirm they want to quit
    }

    private static String getSafeInput(String pattern, String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    private static int getSafeRangedInt(int min, int max, String prompt) {
        int number;
        do {
            System.out.print(prompt);
            while (!scanner.hasNextInt()) {
                System.out.println("That isn't a number! Please Try again."); // Tell them that isn't a number
                scanner.next();
                System.out.print(prompt);
            }
            number = scanner.nextInt();
            scanner.nextLine();
        } while (number < min || number > max);
        return number;
    }

    private static boolean getSafeYNConfirm(String prompt) {
        String input;
        do {
            System.out.print(prompt);
            input = scanner.nextLine().trim().toLowerCase();
        } while (!input.equals("y") && !input.equals("n"));
        return input.equals("y");
    }

    private static void printNumberedList() {
        int number = 1;
        for (String item : list) {
            System.out.println(number++ + ". " + item);
        }
    }

    private static void openList() {
        if (needsToBeSaved && getSafeYNConfirm("You did not save changes. Want to save them before loading a new list? (y/n): ")) {
            saveList(); // Tell them they didn't save. Offer them to save
        }
        System.out.print("Enter the filename you want to open: "); // Ask what file they want to open
        String filename = scanner.nextLine();
        File file = new File(filename + ".txt");
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                list.clear();
                String line;
                while ((line = reader.readLine()) != null) {
                    list.add(line);
                }
                currentFileName = filename;
                needsToBeSaved = false;
            } catch (IOException e) {
                System.out.println("Failed reading file: " + e.getMessage()); // Reading file error message
            }
        } else {
            System.out.println("File doesn't exist."); // Tell them file doesn't exist
        }
    }

    private static void saveList() {
        if (currentFileName.isEmpty()) {
            System.out.print("Enter filename to save: "); // Ask them what file they want to save
            currentFileName = scanner.nextLine();
        }
        try (PrintWriter out = new PrintWriter(new FileWriter(currentFileName + ".txt"))) {
            for (String item : list) {
                out.println(item);
            }
            needsToBeSaved = false;
            System.out.println("List was saved to " + currentFileName + ".txt"); // Tell them the list was saved
        } catch (IOException e) {
            System.out.println("Failed saving file: " + e.getMessage()); // Error for saved files
        }
    }

    private static void clearList() {
        if (!list.isEmpty() && getSafeYNConfirm("Do you want to clear this list? (y/n): ")) { // Ask if they want to clear list
            list.clear();
            needsToBeSaved = true;
        }
    }
}
