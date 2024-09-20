package Presentation;

import BLL.BillBLL;
import BLL.ClientBLL;
import BLL.OrdersBLL;
import BLL.ProductBLL;
import Model.Bill;
import Model.Client;
import Model.Orders;
import Model.Product;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class OrdersInterface extends JFrame {
    private JPanel panel1;
    private JButton addOrderButton;
    private JButton deleteOrderButton;
    private JButton viewOrdersButton;
    private JButton mainInterfaceButton;
    private JTextField textFieldId;
    private JTextField textFieldClientId;
    private JTextField textFieldProductId;
    private JTextField textFieldQuantity;
    private JTable table1;

    public OrdersInterface() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("CLIENT INTERFACE");
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
        addOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int ok = 1;
                if (textFieldId.getText().isEmpty() || textFieldClientId.getText().isEmpty() || textFieldProductId.getText().isEmpty()
                        || textFieldQuantity.getText().isEmpty())
                    JOptionPane.showMessageDialog(panel1, "COMPLETE ALL THE FIELDS!", "ALERT", JOptionPane.WARNING_MESSAGE);
                else {
                    OrdersBLL ordersBLL = new OrdersBLL();
                    Orders or1 = null;
                    Orders firstOrder = new Orders(Integer.parseInt(textFieldId.getText()), Integer.parseInt(textFieldClientId.getText()), Integer.parseInt(textFieldProductId.getText()),
                            Integer.parseInt(textFieldQuantity.getText()));
                    ClientBLL clientBLL = new ClientBLL();
                    Client firstClient = null;
                    ProductBLL productBLL = new ProductBLL();//this needed for checking
                    Product firstProduct = null;
                    Bill firstBill = null;
                    BillBLL billBLL = new BillBLL();
                    try {
                        or1 = ordersBLL.findOrdersById(Integer.parseInt(textFieldId.getText()));
                        if (or1 != null)
                            JOptionPane.showMessageDialog(panel1, "Cannot insert command because command with id " + Integer.parseInt(textFieldId.getText()) + " already exist", "ALERT", JOptionPane.WARNING_MESSAGE);
                        else {
                            firstClient = clientBLL.findClientById(firstOrder.getClient_id());
                            firstProduct = productBLL.findProductById(firstOrder.getProduct_id());
                            if (firstClient == null)
                                JOptionPane.showMessageDialog(panel1, "Client with id " + Integer.parseInt(textFieldClientId.getText()) + " does not exist", "ALERT", JOptionPane.WARNING_MESSAGE);
                            else {
                                if (firstProduct == null)
                                    JOptionPane.showMessageDialog(panel1, "Product with id " + Integer.parseInt(textFieldProductId.getText()) + " does not exist", "ALERT", JOptionPane.WARNING_MESSAGE);
                                else {
                                    if (productBLL.CheckQuantity(firstOrder.getProduct_id(), firstOrder.getQuantity()) == false || Integer.parseInt(textFieldQuantity.getText()) < 1)
                                        JOptionPane.showMessageDialog(panel1, "Cannot proceed command", "ALERT", JOptionPane.WARNING_MESSAGE);
                                    else {
                                        int idForOrder = 0;
                                        BigDecimal quantity = BigDecimal.valueOf(firstOrder.getQuantity());//from here
                                        BigDecimal price = firstProduct.getPrice();
                                        BigDecimal totalPrice = quantity.multiply(price);//to here needed for all price
                                        ordersBLL.insert(firstOrder);
                                        System.out.println("ORDER INSERTED!");
                                        JOptionPane.showMessageDialog(panel1, "Order inserted!");
                                        List<Bill> list = new ArrayList<>();
                                        list = billBLL.findAll();
                                        Bill firstBillFromList = null;
                                        if (list.isEmpty() || list == null) ok = 0;
                                        if (ok == 1) {
                                            firstBillFromList = list.get(list.size() - 1);
                                            idForOrder = firstBillFromList.id();
                                        }
                                        firstBill = new Bill(idForOrder + 1, firstOrder.getClient_id(), firstOrder.getProduct_id(), firstOrder.getQuantity(), totalPrice);
                                        billBLL.insert(firstBill);
                                        System.out.println("BILL INSERTED");
                                        JOptionPane.showMessageDialog(panel1, "Bill inserted!");
                                        productBLL.updateQuantity(firstOrder.getProduct_id(), firstOrder.getQuantity());
                                    }
                                }
                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
        deleteOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (textFieldId.getText().isEmpty() || textFieldClientId.getText().isEmpty() || textFieldProductId.getText().isEmpty()
                        || textFieldQuantity.getText().isEmpty())
                    JOptionPane.showMessageDialog(panel1, "COMPLETE ALL THE FIELDS!", "ALERT", JOptionPane.WARNING_MESSAGE);
                else {
                    OrdersBLL ordersBLL = new OrdersBLL();
                    Orders or1 = null;
                    Orders firstOrder = new Orders(Integer.parseInt(textFieldId.getText()), Integer.parseInt(textFieldClientId.getText()), Integer.parseInt(textFieldProductId.getText()),
                            Integer.parseInt(textFieldQuantity.getText()));
                    try {
                        or1 = ordersBLL.findOrdersById(Integer.parseInt(textFieldId.getText()));
                        if (or1 == null)
                            JOptionPane.showMessageDialog(panel1, "Order with id " + Integer.parseInt(textFieldId.getText()) + " cannot be deleted because it does not exist!", "ALERT", JOptionPane.WARNING_MESSAGE);
                        else {
                            ordersBLL.delete(firstOrder.getId(), firstOrder);
                            System.out.println("ORDER DELETED!");
                            JOptionPane.showMessageDialog(panel1, "Order deleted!");
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
        viewOrdersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<Orders> list = new ArrayList<>();
                OrdersBLL ordersBLL = new OrdersBLL();
                GenerateTable<Orders> t = new GenerateTable<>();
                list = ordersBLL.findAll();
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
