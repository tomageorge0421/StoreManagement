package Presentation;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainInterface extends JFrame {
    private JPanel panel1;
    private JButton clientInterfaceButton;
    private JButton productInterfaceButton;
    private JButton ordersInterfaceButton;
    private JButton billInterfaceButton;

    public MainInterface() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("MAIN INTERFACE");
        setSize(600, 600);
        setContentPane(panel1);
        setVisible(true);

        clientInterfaceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                ClientInterface clientInterface = new ClientInterface();
            }
        });
        productInterfaceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                ProductInterface productInterface = new ProductInterface();
            }
        });
        billInterfaceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                BillInterface billInterface = new BillInterface();
            }
        });
        ordersInterfaceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                OrdersInterface ordersInterface = new OrdersInterface();
            }
        });
    }
}
