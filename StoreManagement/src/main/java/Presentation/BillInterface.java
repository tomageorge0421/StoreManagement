package Presentation;

import BLL.BillBLL;
import Model.Bill;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class BillInterface extends JFrame {
    private JPanel panel1;
    private JButton viewBillsButton;
    private JButton mainInterfaceButton;
    private JTable table1;

    public BillInterface() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("BILL INTERFACE");
        setSize(900, 600);
        setContentPane(panel1);
        setVisible(true);
        mainInterfaceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                MainInterface mainInterface = new MainInterface();
            }
        });
        viewBillsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<Bill> list = new ArrayList<>();
                BillBLL billBLL = new BillBLL();
                GenerateTable<Bill> t = new GenerateTable<>();
                list = billBLL.findAll();
                if (t.createTable(list) == null)
                    JOptionPane.showMessageDialog(panel1, "Table empty!", "ALERT", JOptionPane.WARNING_MESSAGE);
                else {
                    JTable table = t.createTable(list);
                    table1.setModel(table.getModel());
                }
            }
        });
    }
}
