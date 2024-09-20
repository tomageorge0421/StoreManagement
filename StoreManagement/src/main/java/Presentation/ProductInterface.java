package Presentation;

import BLL.BillBLL;
import BLL.ProductBLL;
import Model.Bill;
import Model.Product;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ProductInterface extends JFrame {
    private JPanel panel1;
    private JButton mainInterfaceButton;
    private JButton addProductButton;
    private JButton editProductButton;
    private JButton deleteProductButton;
    private JButton viewProductsButton;
    private JTextField textFieldId;
    private JTextField textFieldName;
    private JTextField textFieldQuantity;
    private JTextField textFieldPrice;
    private JTable table1;

    public ProductInterface() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Product INTERFACE");
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
        addProductButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (textFieldId.getText().isEmpty() || textFieldName.getText().isEmpty() || textFieldQuantity.getText().isEmpty()
                        || textFieldPrice.getText().isEmpty())
                    JOptionPane.showMessageDialog(panel1, "COMPLETE ALL THE FIELDS!", "ALERT", JOptionPane.WARNING_MESSAGE);
                else {
                    ProductBLL productBLL = new ProductBLL();
                    Product pr1 = null;
                    Product firstProduct = new Product(Integer.parseInt(textFieldId.getText()), textFieldName.getText(), Integer.parseInt(textFieldQuantity.getText()),
                            BigDecimal.valueOf(Double.parseDouble(textFieldPrice.getText())));
                    try {
                        pr1 = productBLL.findProductById(Integer.parseInt(textFieldId.getText()));
                        if (pr1 != null)
                            JOptionPane.showMessageDialog(panel1, "Product with id " + Integer.parseInt(textFieldId.getText()) + " already exists", "ALERT", JOptionPane.WARNING_MESSAGE);
                        else {
                            productBLL.insert(firstProduct);
                            System.out.println("Product INSERTED!");
                            JOptionPane.showMessageDialog(panel1, "Product inserted!");
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
        editProductButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (textFieldId.getText().isEmpty() || textFieldName.getText().isEmpty() || textFieldQuantity.getText().isEmpty()
                        || textFieldPrice.getText().isEmpty())
                    JOptionPane.showMessageDialog(panel1, "COMPLETE ALL THE FIELDS!", "ALERT", JOptionPane.WARNING_MESSAGE);
                else {
                    ProductBLL productBLL = new ProductBLL();
                    Product pr1 = null;
                    Product firstProduct = new Product(Integer.parseInt(textFieldId.getText()), textFieldName.getText(), Integer.parseInt(textFieldQuantity.getText()),
                            BigDecimal.valueOf(Double.parseDouble(textFieldPrice.getText())));
                    try {
                        pr1 = productBLL.findProductById(Integer.parseInt(textFieldId.getText()));
                        if (pr1 == null)
                            JOptionPane.showMessageDialog(panel1, "Product with id " + Integer.parseInt(textFieldId.getText()) + " cannot be updated because it does not exist!", "ALERT", JOptionPane.WARNING_MESSAGE);
                        else {
                            productBLL.update(firstProduct);
                            System.out.println("PRODUCT UPDATED!");
                            JOptionPane.showMessageDialog(panel1, "Product updated!");
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
        deleteProductButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int ok = 1;
                if (textFieldId.getText().isEmpty() || textFieldName.getText().isEmpty() || textFieldQuantity.getText().isEmpty()
                        || textFieldPrice.getText().isEmpty())
                    JOptionPane.showMessageDialog(panel1, "COMPLETE ALL THE FIELDS!", "ALERT", JOptionPane.WARNING_MESSAGE);
                else {
                    ProductBLL productBLL = new ProductBLL();
                    Product pr1 = null;
                    Product firstProduct = new Product(Integer.parseInt(textFieldId.getText()), textFieldName.getText(), Integer.parseInt(textFieldQuantity.getText()),
                            BigDecimal.valueOf(Double.parseDouble(textFieldPrice.getText())));
                    try {
                        pr1 = productBLL.findProductById(Integer.parseInt(textFieldId.getText()));
                        if (pr1 == null)
                            JOptionPane.showMessageDialog(panel1, "Product with id " + Integer.parseInt(textFieldId.getText()) + " cannot be deleted because it does not exist!", "ALERT", JOptionPane.WARNING_MESSAGE);
                        else {
                            List<Bill> list = new ArrayList<>();
                            BillBLL billBLL = new BillBLL();
                            list = billBLL.findAll();
                            for (Bill bill : list) {
                                if (bill.product_id() == firstProduct.getId()) {
                                    JOptionPane.showMessageDialog(panel1, "Product with id " + Integer.parseInt(textFieldId.getText()) + " cannot be deleted because it is a bill with him!", "ALERT", JOptionPane.WARNING_MESSAGE);
                                    ok = 0;
                                }
                            }
                            if (ok == 1) {
                                productBLL.delete(firstProduct.getId(), firstProduct);
                                System.out.println("PRODUCT DELETED!");
                                JOptionPane.showMessageDialog(panel1, "Product deleted!");
                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
        viewProductsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<Product> list = new ArrayList<>();
                ProductBLL productBLL = new ProductBLL();
                GenerateTable<Product> t = new GenerateTable<>();
                list = productBLL.findAll();
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
