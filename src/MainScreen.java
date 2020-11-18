import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

public class MainScreen implements ActionListener {
    private DefaultListModel<String> defaultListModel;
    private JTextField addToRoutingTableTextField;
    private JTextField routeIPTextField;

    public MainScreen() {
        JFrame frame = new JFrame("Routing Table GUI");

        JPanel routingTablePanel = new JPanel();
        routingTablePanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 10, 30));
        JLabel routingTableLabel = new JLabel("Routing Table");
        defaultListModel = new DefaultListModel<>();

        defaultListModel.addAll(Arrays.asList("Default", "135.46.60.0/22", "192.53.40.0/23"));
        JList<String> routingTableList = new JList<>(defaultListModel);
        JScrollPane routingTableScrollBar = new JScrollPane(routingTableList);
        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(this);
        routingTablePanel.setLayout(new FlowLayout());
        routingTablePanel.add(routingTableLabel);
        routingTablePanel.add(routingTableScrollBar);
        routingTablePanel.add(clearButton);

        JPanel addAddressPanel = new JPanel();
        JLabel addAddressLabel = new JLabel("Add IPv4 address in CIDR notation to routing table");
        addToRoutingTableTextField = new JTextField("135.46.56.0/22");
        JButton addButton = new JButton("Add");
        addButton.addActionListener(this);
        addAddressPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 10, 30));
        addAddressPanel.setLayout(new FlowLayout());
        addAddressPanel.add(addAddressLabel);
        addAddressPanel.add(addToRoutingTableTextField);
        addAddressPanel.add(addButton);

        JPanel routePanel = new JPanel();
        JLabel routeIPLabel = new JLabel("Test an IPv4 address to see where it routes to");
        routeIPTextField = new JTextField("192.53.40.7");
        JButton routeButton = new JButton("Route");
        routeButton.addActionListener(this);
        routePanel.add(routeIPLabel);
        routePanel.add(routeIPTextField);
        routePanel.add(routeButton);

        frame.add(routingTablePanel);
        frame.add(addAddressPanel);
        frame.add(routePanel);
        frame.setLayout(new GridLayout(2, 2));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println(e.getActionCommand());
        if (e.getActionCommand().equals("Add")) {
            // TODO: input validation. Input must be IPv4 CIDR notation
            defaultListModel.addElement(addToRoutingTableTextField.getText());
        } else if (e.getActionCommand().equals("Clear")) {
            defaultListModel.clear();
            defaultListModel.addElement("Default"); // Default is always in list
        } else if (e.getActionCommand().equals("Route")) {
            // TODO: call route function
        }
    }

    public static void main(String[] args) {
        new MainScreen();
    }
}
