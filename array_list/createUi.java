package array_list;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class createUi {
    public static void createUI(DefaultListModel<String> listModel, JList<String> jList, 
                                 ActionListener addItem, ActionListener deleteItem, 
                                 ActionListener updateItem, ActionListener versusMode, 
                                 ActionListener buyItem, ActionListener deleteBuyItem, 
                                 ActionListener viewBoughtItems) {

        JFrame frame = new JFrame("CRUD Application - Dota 2 Heroes");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 350);

        frame.setLayout(new BorderLayout());
        JScrollPane scrollPane = new JScrollPane(jList);
        frame.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        JButton addButton = new JButton("Add Hero");
        JButton deleteButton = new JButton("Delete Hero");
        JButton updateButton = new JButton("Update Hero");
        JButton versusButton = new JButton("Versus Mode");
        JButton buyItemButton = new JButton("Buy Items");
        JButton deleteBuyItemButton = new JButton("Delete Buy Item");
        JButton viewItemsButton = new JButton("View Items");

        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(versusButton);
        buttonPanel.add(buyItemButton);
        buttonPanel.add(deleteBuyItemButton);
        buttonPanel.add(viewItemsButton);

        frame.add(buttonPanel, BorderLayout.SOUTH);

        // Add action listeners from passed parameters
        addButton.addActionListener(addItem);
        deleteButton.addActionListener(deleteItem);
        updateButton.addActionListener(updateItem);
        versusButton.addActionListener(versusMode);
        buyItemButton.addActionListener(buyItem);
        deleteBuyItemButton.addActionListener(deleteBuyItem);
        viewItemsButton.addActionListener(viewBoughtItems);

        frame.setVisible(true);
    }
}
