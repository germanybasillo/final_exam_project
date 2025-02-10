import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CRUDApp {
    // Create an ArrayList to store Dota 2 heroes
    private static ArrayList<String> list = new ArrayList<>();
    private static DefaultListModel<String> listModel = new DefaultListModel<>();
    private static JList<String> jList = new JList<>(listModel);

    // List of valid Dota 2 heroes
    private static List<String> validHeroes = Arrays.asList(
            "Anti-Mage", "Axe", "Bane", "Batrider", "Beastmaster", "Bloodseeker", "Bounty Hunter",
            "Brewmaster", "Broodmother", "Crystal Maiden", "Dark Seer", "Dazzle", "Death Prophet",
            "Disruptor", "Doom", "Dragon Knight", "Drow Ranger", "Earthshaker", "Elder Titan",
            "Enchantress", "Faceless Void", "Grimstroke", "Huskar", "Invoker", "Jakiro", "Juggernaut",
            "Kunkka", "Legion Commander", "Leshrac", "Lich", "Lina", "Lion", "Lone Druid", "Luna",
            "Magnus", "Medusa", "Meepo", "Mirana", "Monkey King", "Morphling", "Naga Siren", "Necrophos",
            "Night Stalker", "Nyx Assassin", "Ogre Magi", "Oracle", "Outworld Destroyer", "Pangolier",
            "Phantom Assassin", "Phantom Lancer", "Puck", "Pugna", "Riki", "Rubick", "Sand King",
            "Shadow Demon", "Shadow Fiend", "Silencer", "Slardar", "Slark", "Sniper", "Spectre",
            "Spirit Breaker", "Storm Spirit", "Sven", "Tinker", "Tiny", "Treant Protector", "Troll Warlord",
            "Tinker", "Vengeful Spirit", "Venomancer", "Warlock", "Windranger", "Witch Doctor", "Zeus");

    public static void main(String[] args) {
        // Add initial Dota 2 hero names
        list.add("Anti-Mage");
        list.add("Axe");
        list.add("Crystal Maiden");

        // Initialize the Swing UI
        SwingUtilities.invokeLater(() -> createUI());
    }

    public static void createUI() {
        JFrame frame = new JFrame("CRUD Application - Dota 2 Heroes");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        // Layout manager
        frame.setLayout(new BorderLayout());

        // Set up the list to show the items
        listModel.clear();
        for (String item : list) {
            listModel.addElement(item);
        }

        // Add JScrollPane for better UI experience
        JScrollPane scrollPane = new JScrollPane(jList);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Create a panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        // Create buttons for CRUD operations
        JButton addButton = new JButton("Add");
        JButton deleteButton = new JButton("Delete");
        JButton updateButton = new JButton("Update");

        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(updateButton);

        frame.add(buttonPanel, BorderLayout.SOUTH);

        // Add button action listeners
        addButton.addActionListener(e -> addItem());
        deleteButton.addActionListener(e -> deleteItem());
        updateButton.addActionListener(e -> updateItem());

        // Make the window visible
        frame.setVisible(true);
    }

    // Capitalize the first letter of each word (handling hyphen correctly)
    public static String capitalizeFirstLetter(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        StringBuilder capitalized = new StringBuilder();
        String[] words = input.split(" "); // Split by spaces only

        for (String word : words) {
            if (word.contains("-")) {
                // If the word contains a hyphen (like "Anti-Mage"), capitalize both parts
                String[] parts = word.split("-");
                for (int i = 0; i < parts.length; i++) {
                    if (i > 0)
                        capitalized.append("-"); // Add the hyphen back
                    capitalized.append(parts[i].substring(0, 1).toUpperCase())
                            .append(parts[i].substring(1).toLowerCase());
                }
            } else {
                // Capitalize normal word
                capitalized.append(word.substring(0, 1).toUpperCase())
                        .append(word.substring(1).toLowerCase());
            }
            capitalized.append(" ");
        }

        return capitalized.toString().trim(); // Remove trailing space
    }

    // Add item (Dota 2 hero)
    public static void addItem() {
        String newItem = JOptionPane.showInputDialog("Enter Dota 2 hero to add:");
        if (newItem != null && !newItem.trim().isEmpty()) {
            newItem = newItem.trim();

            // Capitalize the first letter and handle hyphenated names
            newItem = capitalizeFirstLetter(newItem);

            // Check if the entered hero name is valid and formatted correctly
            if (validHeroes.contains(newItem)) {
                // Check if the hero already exists in the list (case-insensitive comparison)
                boolean alreadyExists = false;
                for (String hero : list) {
                    if (hero.equalsIgnoreCase(newItem)) {
                        alreadyExists = true;
                        break;
                    }
                }

                if (!alreadyExists) {
                    list.add(newItem);
                    listModel.addElement(newItem);
                    // Success message
                    JOptionPane.showMessageDialog(null, "Successfully added: " + newItem);
                } else {
                    JOptionPane.showMessageDialog(null, "This hero already exists in the list!");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Invalid hero name! Please enter a valid Dota 2 hero.");
            }
        }
    }

    // Delete item (Dota 2 hero)
    public static void deleteItem() {
        String itemToDelete = jList.getSelectedValue();
        if (itemToDelete != null) {
            int confirmation = JOptionPane.showConfirmDialog(null,
                    "Are you sure you want to delete " + itemToDelete + "?",
                    "Delete Confirmation", JOptionPane.YES_NO_OPTION);
            if (confirmation == JOptionPane.YES_OPTION) {
                list.remove(itemToDelete);
                listModel.removeElement(itemToDelete);
                // Success message
                JOptionPane.showMessageDialog(null, "Successfully deleted: " + itemToDelete);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Please select an item to delete.");
        }
    }

    // Update item (Dota 2 hero)
    public static void updateItem() {
        String selectedItem = jList.getSelectedValue();
        if (selectedItem != null) {
            String updatedItem = JOptionPane.showInputDialog("Update hero name:", selectedItem);
            if (updatedItem != null && !updatedItem.trim().isEmpty()) {
                updatedItem = updatedItem.trim();

                // Capitalize the first letter and handle hyphenated names
                updatedItem = capitalizeFirstLetter(updatedItem);

                // Check if the entered hero name is valid
                if (validHeroes.contains(updatedItem)) {
                    // Check if the updated hero name already exists in the list (case-insensitive
                    // comparison)
                    boolean alreadyExists = false;
                    for (String hero : list) {
                        if (hero.equalsIgnoreCase(updatedItem)) {
                            alreadyExists = true;
                            break;
                        }
                    }

                    if (!alreadyExists) {
                        int selectedIndex = jList.getSelectedIndex();
                        list.set(selectedIndex, updatedItem);
                        listModel.set(selectedIndex, updatedItem);
                        // Success message
                        JOptionPane.showMessageDialog(null, "Successfully updated to: " + updatedItem);
                    } else {
                        JOptionPane.showMessageDialog(null, "This hero name is already in the list!");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid hero name! Please enter a valid Dota 2 hero.");
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Please select an item to update.");
        }
    }
}
