import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class MainScreen implements ActionListener {
    private JTextField addIPTextField;
    private JTextField addInterfaceTextField;
    private JTextField routeIPTextField;
    private JLabel routeResultLabel;
    private Map<String, String> routingTable;
    private DefaultTableModel tableModel;

    public MainScreen() {
        JFrame frame = new JFrame("Routing Table GUI");

        /* Routing Table Panel */
        JPanel routingTablePanel = new JPanel();
        routingTablePanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 10, 30));
        JLabel routingTableLabel = new JLabel("Routing Table");
        routingTable = new LinkedHashMap<>();
        this.populateTable();
        JTable jTable = new JTable(tableModel);
        JScrollPane routingTableScrollBar = new JScrollPane(jTable);
        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(this);
        routingTablePanel.setLayout(new FlowLayout());
        routingTablePanel.add(routingTableLabel);
        routingTablePanel.add(routingTableScrollBar);
        routingTablePanel.add(clearButton);
        JLabel addAddressLabel = new JLabel("Add IPv4 address in CIDR notation and Interface to routing table");
        addIPTextField = new JTextField("135.46.56.0/22");
        addInterfaceTextField = new JTextField("Interface 0");
        JButton addButton = new JButton("Add");
        addButton.addActionListener(this);
        routingTablePanel.add(addAddressLabel);
        routingTablePanel.add(addIPTextField);
        routingTablePanel.add(addInterfaceTextField);
        routingTablePanel.add(addButton);

        JPanel routePanel = new JPanel();
        JLabel routeIPLabel = new JLabel("Test an IPv4 address to see which interface it routes to.");
        routeIPTextField = new JTextField("192.53.40.7");
        JButton routeButton = new JButton("Route");
        routeResultLabel = new JLabel();
        routeButton.addActionListener(this);
        routePanel.add(routeIPLabel);
        routePanel.add(routeIPTextField);
        routePanel.add(routeButton);
        routePanel.add(routeResultLabel);

        frame.add(routingTablePanel, BorderLayout.CENTER);
        frame.add(routePanel, BorderLayout.SOUTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println(e.getActionCommand());
        if (e.getActionCommand().equals("Add")) {
            // TODO: input validation. Input must be IPv4 CIDR notation
            String[] newRow = {addIPTextField.getText(), addInterfaceTextField.getText()};
            tableModel.addRow(newRow);
        } else if (e.getActionCommand().equals("Clear")) {
            this.resetTable();
        } else if (e.getActionCommand().equals("Route")) {
            routeResultLabel.setText(RoutingService.route(routeIPTextField.getText(), routingTable));
        }
    }

    public static void main(String[] args) {
        new MainScreen();
    }

    private void populateTable() {
        routingTable.put("Default", "Router 2");
        routingTable.put("135.46.56.0/22", "Interface 0");
        routingTable.put("135.46.60.0/22", "Interface 1");
        routingTable.put("192.53.40.0/23", "Router 1");

        tableModel = new DefaultTableModel();
        tableModel.addColumn("Address/mask", routingTable.keySet().toArray(new String[0]));
        tableModel.addColumn("Next hop",  routingTable.values().toArray(new String[0]));
    }

    private void resetTable() {
        routingTable.clear();
        routingTable.put("Default", "Router 2");

        int rowCount = tableModel.getRowCount();
        for (int i = 0; i < rowCount; i++) {
            tableModel.removeRow(0);
        }
        String[] defaultRow = {"Default", "Router 2"};
        tableModel.addRow(defaultRow);
    }
}
