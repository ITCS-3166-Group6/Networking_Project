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
        routingTable = new LinkedHashMap<>();
        this.populateTable();
        JTable jTable = new JTable(tableModel);
        JScrollPane routingTableScrollBar = new JScrollPane(jTable);
        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(this);
        routingTablePanel.setLayout(new FlowLayout());
        routingTablePanel.add(routingTableScrollBar);
        routingTablePanel.add(clearButton);
        JLabel addAddressLabel = new JLabel("<html>Add IPv4 address in CIDR notation and Interface to routing table.<br>Duplicate address/mask is not allowed</html>", JLabel.RIGHT);
        addIPTextField = new JTextField("145.90.56.0/22");
        addInterfaceTextField = new JTextField("Interface 2");
        JButton addButton = new JButton("Add");
        addButton.addActionListener(this);
        routingTablePanel.add(addAddressLabel);
        routingTablePanel.add(addIPTextField);
        routingTablePanel.add(addInterfaceTextField);
        routingTablePanel.add(addButton);
        routingTablePanel.setBackground(new Color(146,229,214));

        /* test IP Panel */
        JPanel routePanel = new JPanel();
        JLabel routeIPLabel = new JLabel("Test an IPv4 address to see its next hop!");
        routeIPTextField = new JTextField("192.53.40.7");
        JButton routeButton = new JButton("Route");
        routeResultLabel = new JLabel("Destination: __");
        routeButton.addActionListener(this);
        routePanel.add(routeIPLabel);
        routePanel.add(routeIPTextField);
        routePanel.add(routeButton);
        routePanel.add(routeResultLabel);
        routePanel.setBackground(Color.CYAN);

        frame.add(routingTablePanel, BorderLayout.CENTER);
        frame.add(routePanel, BorderLayout.SOUTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setBackground(Color.CYAN);
        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String actionCommand = e.getActionCommand();
        if (actionCommand.equals("Add")) {
            String inputIP = addIPTextField.getText();
            boolean validCIDR = inputIP.matches("^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])(\\/(3[0-2]|[1-2][0-9]|[0-9]))$");
            if (validCIDR && !routingTable.containsKey(inputIP)) {
                String[] newRow = {inputIP, addInterfaceTextField.getText()};
                tableModel.addRow(newRow);
                routingTable.put(inputIP, addInterfaceTextField.getText());
                addIPTextField.setBackground(Color.WHITE);
            } else {
                addIPTextField.setBackground(Color.RED);
            }
        } else if (actionCommand.equals("Clear")) {
            this.resetTable();
        } else if (actionCommand.equals("Route")) {
            routeResultLabel.setText("Destination: " + RoutingService.route(routeIPTextField.getText(), routingTable));
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
