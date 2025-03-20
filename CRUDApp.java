import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.HashMap;
import java.util.Map;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CRUDApp {
    private static ArrayList<String> list = new ArrayList<>();
    private static DefaultListModel<String> listModel = new DefaultListModel<>();
    private static JList<String> jList = new JList<>(listModel);
    private static List<String> validHeroes = Arrays.asList(
            "Anti-Mage", "Axe", "Crystal Maiden", "Juggernaut", "Invoker", "Pudge", "Sniper"
    );
    private static Map<String, Integer> dotaItems = new HashMap<>();
    private static Map<String, ArrayList<String>> heroInventory = new HashMap<>();
    private static Map<String, Integer> heroDamage = new HashMap<>();

    public static void main(String[] args) {
        list.add("Anti-Mage");
        list.add("Axe");
        list.add("Crystal Maiden");

        initializeItems();
        SwingUtilities.invokeLater(() -> createUI());
    }

    public static void createUI() {
        JFrame frame = new JFrame("CRUD Application - Dota 2 Heroes");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 350);

        frame.setLayout(new BorderLayout());
        listModel.clear();
        for (String item : list) {
            listModel.addElement(item);
        }

        JScrollPane scrollPane = new JScrollPane(jList);
        frame.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        JButton addButton = new JButton("Add");
        JButton deleteButton = new JButton("Delete");
        JButton updateButton = new JButton("Update");
        JButton versusButton = new JButton("Versus Mode");
        JButton buyItemButton = new JButton("Buy Items");

        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(versusButton);
        buttonPanel.add(buyItemButton);

        frame.add(buttonPanel, BorderLayout.SOUTH);

        addButton.addActionListener(e -> addItem());
        deleteButton.addActionListener(e -> deleteItem());
        updateButton.addActionListener(e -> updateItem());
        versusButton.addActionListener(e -> versusMode());
        buyItemButton.addActionListener(e -> buyItem());

        frame.setVisible(true);
    }

    public static void initializeItems() {
        dotaItems.put("Aghanim's Scepter", 25);
        dotaItems.put("Desolator", 40);
        dotaItems.put("Radiance", 50);
        dotaItems.put("Black King Bar", 20);
        dotaItems.put("Manta Style", 18);
        dotaItems.put("Satanic", 30);
    }

    public static void addItem() {
        String newItem = JOptionPane.showInputDialog("Enter Dota 2 hero to add:");
        if (newItem != null && !newItem.trim().isEmpty()) {
            newItem = capitalizeFirstLetter(newItem.trim());

            if (validHeroes.contains(newItem)) {
                if (!list.contains(newItem)) {
                    list.add(newItem);
                    listModel.addElement(newItem);
                    JOptionPane.showMessageDialog(null, "Successfully added: " + newItem);
                } else {
                    JOptionPane.showMessageDialog(null, "This hero already exists in the list!");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Invalid hero name! Please enter a valid Dota 2 hero.");
            }
        }
    }

    public static void deleteItem() {
        String itemToDelete = jList.getSelectedValue();
        if (itemToDelete != null) {
            list.remove(itemToDelete);
            listModel.removeElement(itemToDelete);
            heroInventory.remove(itemToDelete);
            heroDamage.remove(itemToDelete);
            JOptionPane.showMessageDialog(null, "Successfully deleted: " + itemToDelete);
        } else {
            JOptionPane.showMessageDialog(null, "Please select an item to delete.");
        }
    }

    public static void updateItem() {
        String selectedItem = jList.getSelectedValue();
        if (selectedItem != null) {
            String updatedItem = JOptionPane.showInputDialog("Update hero name:", selectedItem);
            if (updatedItem != null && !updatedItem.trim().isEmpty()) {
                updatedItem = capitalizeFirstLetter(updatedItem.trim());
                if (validHeroes.contains(updatedItem) && !list.contains(updatedItem)) {
                    int selectedIndex = jList.getSelectedIndex();
                    list.set(selectedIndex, updatedItem);
                    listModel.set(selectedIndex, updatedItem);

                    heroInventory.put(updatedItem, heroInventory.getOrDefault(selectedItem, new ArrayList<>()));
                    heroDamage.put(updatedItem, heroDamage.getOrDefault(selectedItem, 0));

                    heroInventory.remove(selectedItem);
                    heroDamage.remove(selectedItem);

                    JOptionPane.showMessageDialog(null, "Successfully updated to: " + updatedItem);
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid or duplicate hero name.");
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Please select an item to update.");
        }
    }

    public static void buyItem() {
        String selectedHero = jList.getSelectedValue();
        if (selectedHero != null) {
            Object[] itemArray = dotaItems.keySet().toArray();
            String selectedItem = (String) JOptionPane.showInputDialog(null,
                    "Select an item to buy:", "Buy Items",
                    JOptionPane.QUESTION_MESSAGE, null, itemArray, itemArray[0]);

            if (selectedItem != null) {
                heroInventory.putIfAbsent(selectedHero, new ArrayList<>());
                heroInventory.get(selectedHero).add(selectedItem.toLowerCase()); // Convert to lowercase

                int itemDamage = dotaItems.get(selectedItem);
                int currentDamage = heroDamage.getOrDefault(selectedHero, 10);
                heroDamage.put(selectedHero, currentDamage + itemDamage);

                JOptionPane.showMessageDialog(null,
                        selectedHero + " bought: " + selectedItem + " (+" + itemDamage + " damage)\n" +
                                "Total Damage: " + heroDamage.get(selectedHero));
            }
        } else {
            JOptionPane.showMessageDialog(null, "Please select a hero to buy items.");
        }
    }

    public static void versusMode() {
        List<String> selectedHeroes = jList.getSelectedValuesList();
        if (selectedHeroes.size() == 2) {
            String hero1 = selectedHeroes.get(0);
            String hero2 = selectedHeroes.get(1);

            int damage1 = heroDamage.getOrDefault(hero1, 10);
            int damage2 = heroDamage.getOrDefault(hero2, 10);

            ArrayList<String> itemsHero1 = heroInventory.getOrDefault(hero1, new ArrayList<>());
            ArrayList<String> itemsHero2 = heroInventory.getOrDefault(hero2, new ArrayList<>());

            if (itemsHero1.isEmpty() && itemsHero2.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Both heroes have no items. It's a fair match!");
            } else if (itemsHero1.isEmpty()) {
                JOptionPane.showMessageDialog(null, hero1 + " has no items! Don't expect to win!");
            } else if (itemsHero2.isEmpty()) {
                JOptionPane.showMessageDialog(null, hero2 + " has no items! Don't expect to win!");
            } else {
                JOptionPane.showMessageDialog(null, "Both heroes are fully geared! May the best hero win!");
            }

            showCountdown(() -> {
                SwingUtilities.invokeLater(() -> showBattleAnimation(hero1, hero2, damage1, damage2));
            });
        } else {
            JOptionPane.showMessageDialog(null, "Please select exactly 2 heroes for Versus Mode.");
        }
    }

    public static void showCountdown(Runnable onComplete) {
        JFrame countdownFrame = new JFrame("Get Ready!");
        countdownFrame.setSize(200, 150);
        countdownFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JLabel label = new JLabel("3", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 50));
        countdownFrame.add(label);
        countdownFrame.setLocationRelativeTo(null);
        countdownFrame.setVisible(true);

        Timer timer = new Timer(1000, new ActionListener() {
            int count = 3;

            @Override
            public void actionPerformed(ActionEvent e) {
                count--;
                if (count == 0) {
                    ((Timer) e.getSource()).stop();
                    countdownFrame.dispose();
                    onComplete.run();
                } else {
                    label.setText(String.valueOf(count));
                }
            }
        });
        timer.start();
    }

    public static void showBattleAnimation(String hero1, String hero2, int damage1, int damage2) {
        JFrame battleFrame = new JFrame("Hero Battle!");
        battleFrame.setSize(600, 300);
        battleFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        BattlePanel battlePanel = new BattlePanel(hero1, hero2, damage1, damage2);
        battleFrame.add(battlePanel);
        battleFrame.setVisible(true);
    }

    public static String capitalizeFirstLetter(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
    }

    static class BattlePanel extends JPanel {
        private String hero1, hero2;
        private int damage1, damage2;
        private int hero1X = 50;
        private int hero2X = 500;
        private int hero1HP = 100;
        private int hero2HP = 100;
        private boolean isHero1Attacking = false;
        private boolean isHero2Attacking = false;
    
        // Fading Damage Text
        private String damageText1 = "";
        private String damageText2 = "";
        private float damageAlpha1 = 1.0f;
        private float damageAlpha2 = 1.0f;
    
        private Random random = new Random();
    
        public BattlePanel(String hero1, String hero2, int damage1, int damage2) {
            this.hero1 = hero1;
            this.hero2 = hero2;
            this.damage1 = damage1;
            this.damage2 = damage2;
    
            // Move heroes toward each other
            Timer timer = new Timer(200, e -> {
                hero1X += 10;
                hero2X -= 10;
    
                // Stop movement when they meet
                if (hero1X >= 250 || hero2X <= 250) {
                    ((Timer) e.getSource()).stop();
                    startBattle();
                }
                repaint();
            });
            timer.start();
        }

        

        @Override
protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    // Draw health bars
    g.setColor(Color.RED);
    g.fillRect(50, 20, hero1HP * 2, 20); // Hero 1 HP bar
    g.setColor(Color.GREEN);
    g.fillRect(500 - (hero2HP * 2), 20, hero2HP * 2, 20); // Hero 2 HP bar

    // Draw Hero HP Text
    g.setColor(Color.BLACK);
    g.drawString(hero1 + " HP: " + hero1HP + "%", 50, 50);
    g.drawString(hero2 + " HP: " + hero2HP + "%", 400, 50);

    // Draw "VS" in the center of the HP bar
    g.setFont(new Font("Arial", Font.BOLD, 18)); // Set font size and style
    String vsText = "VS";
    int vsWidth = g.getFontMetrics().stringWidth(vsText);
    g.drawString(vsText, 275 - (vsWidth / 2), 35); // Position "VS" in center

    // Draw Damage Text with Fading Effect
    Graphics2D g2d = (Graphics2D) g;
    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, damageAlpha1));
    g.setColor(Color.RED);
    if (!damageText1.isEmpty()) {
        g.drawString(damageText1, hero1X - 15, 120);
    }

    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, damageAlpha2));
    if (!damageText2.isEmpty()) {
        g.drawString(damageText2, hero2X - 15, 120);
    }

    // Draw heroes or dust if defeated
    if (hero1HP > 0) {
        drawHero(g, hero1X + (isHero1Attacking ? 10 : 0), 150, hero1, Color.RED);
    } else {
        drawDust(g, hero1X);
    }

    if (hero2HP > 0) {
        drawHero(g, hero2X - (isHero2Attacking ? 10 : 0), 150, hero2, Color.GREEN);
    } else {
        drawDust(g, hero2X);
    }
}

        private void drawHero(Graphics g, int x, int y, String hero, Color color) {
            g.setColor(color);
            g.fillOval(x - 10, y - 50, 20, 20);
            g.drawLine(x, y - 30, x, y);
            g.drawLine(x, y, x - 20, y + 30);
            g.drawLine(x, y, x + 20, y + 30);
            g.drawLine(x, y - 20, x - 20, y - 10);
            g.drawLine(x, y - 20, x + 20, y - 10);
        
            // Get the first item bought by the hero
            ArrayList<String> items = heroInventory.getOrDefault(hero, new ArrayList<>());
        
            if (!items.isEmpty()) {
                // Display item name instead of hero name
                String firstItem = capitalizeFirstLetter(items.get(0)); // Capitalize item name
                g.setColor(Color.BLACK);
                g.drawString(firstItem, x - 15, y + 45); // Show item name
            } else {
                // If no item, show "No Items"
                g.setColor(Color.GRAY);
                g.drawString("No Items", x - 20, y + 45);
            }
        }

        private void drawDust(Graphics g, int x) {
            g.setColor(Color.LIGHT_GRAY);
            g.fillOval(x - 20, 150 - 20, 40, 40);
        }

        private void startBattle() {
            Timer attackTimer = new Timer(500, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (random.nextBoolean()) {
                        startAttackAnimation(true);
                        int randomDamage = random.nextInt(10);
                        if (randomDamage == 0) {
                            showDamageText(false, "-0"); // Miss
                        } else {
                            hero2HP -= randomDamage;
                            showDamageText(false, "-" + randomDamage);
                        }
                        if (hero2HP <= 0) {
                            hero2HP = 0;
                            ((Timer) e.getSource()).stop();
                            showResult(hero1, hero1HP);
                        }
                    } else {
                        startAttackAnimation(false);
                        int randomDamage = random.nextInt(10);
                        if (randomDamage == 0) {
                            showDamageText(true, "-0"); // Miss
                        } else {
                            hero1HP -= randomDamage;
                            showDamageText(true, "-" + randomDamage);
                        }
                        if (hero1HP <= 0) {
                            hero1HP = 0;
                            ((Timer) e.getSource()).stop();
                            showResult(hero2, hero2HP);
                        }
                    }
                    repaint();
                }
            });
            attackTimer.start();
        }

        private void startAttackAnimation(boolean isHero1) {
            Timer punchTimer = new Timer(100, new ActionListener() {
                int punchCount = 0;

                @Override
                public void actionPerformed(ActionEvent e) {
                    if (punchCount < 4) {
                        if (isHero1) {
                            isHero1Attacking = !isHero1Attacking;
                        } else {
                            isHero2Attacking = !isHero2Attacking;
                        }
                        repaint();
                        punchCount++;
                    } else {
                        ((Timer) e.getSource()).stop();
                        isHero1Attacking = false;
                        isHero2Attacking = false;
                        repaint();
                    }
                }
            });
            punchTimer.start();
        }

        private void showDamageText(boolean isHero1, String text) {
            if (isHero1) {
                damageText1 = text;
                damageAlpha1 = 1.0f;
                startFadeOut(true);
            } else {
                damageText2 = text;
                damageAlpha2 = 1.0f;
                startFadeOut(false);
            }
        }

        private void startFadeOut(boolean isHero1) {
            Timer fadeTimer = new Timer(100, new ActionListener() {
                float alpha = 1.0f;

                @Override
                public void actionPerformed(ActionEvent e) {
                    alpha -= 0.1f;
                    if (alpha <= 0) {
                        ((Timer) e.getSource()).stop();
                        if (isHero1) {
                            damageText1 = "";
                        } else {
                            damageText2 = "";
                        }
                    } else {
                        if (isHero1) {
                            damageAlpha1 = alpha;
                        } else {
                            damageAlpha2 = alpha;
                        }
                    }
                    repaint();
                }
            });
            fadeTimer.start();
        }

        public static String capitalizeFirstLetter(String input) {
            if (input == null || input.isEmpty()) {
                return input;
            }
            return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
        }

        private void showResult(String winner, int hp) {
            String result = winner + " wins with " + hp + "% HP remaining!";
            JOptionPane.showMessageDialog(null, result);
        
            int choice = JOptionPane.showConfirmDialog(null,
                    "Do you want to continue fighting with " + winner + "?",
                    "Continue Battle?", JOptionPane.YES_NO_OPTION);
        
            if (choice == JOptionPane.YES_OPTION) {
                continueFight(winner, hp);
            } else {
                JOptionPane.showMessageDialog(null, "Battle ended. Thanks for playing!");
            }
        }

        private void continueFight(String winner, int hp) {
            // Get list of all available heroes except the winner
            List<String> availableHeroes = new ArrayList<>(list);
            availableHeroes.remove(winner);
        
            if (availableHeroes.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No other heroes left to fight! Battle ends.");
                return;
            }
        
            // Select a new opponent for the winner
            String newOpponent = (String) JOptionPane.showInputDialog(null,
                    "Select a new opponent for " + winner + ":",
                    "Continue Fighting",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    availableHeroes.toArray(),
                    availableHeroes.get(0));
        
            // Check if a new opponent was selected
            if (newOpponent != null) {
                int winnerDamage = heroDamage.getOrDefault(winner, 10);
                int opponentDamage = heroDamage.getOrDefault(newOpponent, 10);
        
                // Remove the selected opponent from the list of available heroes
                list.remove(newOpponent);
        
                // Start a new battle and pass remaining HP of the winner
                SwingUtilities.invokeLater(() -> 
                    showBattleAnimationWithHP(winner, newOpponent, winnerDamage, opponentDamage, hp)
                );
            } else {
                JOptionPane.showMessageDialog(null, "No opponent selected. Battle ends.");
            }
        }

        public static void showBattleAnimationWithHP(String hero1, String hero2, int damage1, int damage2, int hero1HP) {
            JFrame battleFrame = new JFrame("Hero Battle!");
            battleFrame.setSize(600, 300);
            battleFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            BattlePanel battlePanel = new BattlePanel(hero1, hero2, damage1, damage2, hero1HP);
            battleFrame.add(battlePanel);
            battleFrame.setVisible(true);
        }

        public BattlePanel(String hero1, String hero2, int damage1, int damage2, int hero1HP) {
    this.hero1 = hero1;
    this.hero2 = hero2;
    this.damage1 = damage1;
    this.damage2 = damage2;
    this.hero1HP = hero1HP; // Carry over winning HP
    this.hero2HP = 100; // New opponent starts with full HP

    Timer timer = new Timer(200, e -> {
        hero1X += 10;
        hero2X -= 10;

        if (hero1X >= 250 || hero2X <= 250) {
            ((Timer) e.getSource()).stop();
            startBattle();
        }
        repaint();
    });
    timer.start();
}
    }
}