package Presentation;

import BLL.BillBLL;
import BLL.ClientBLL;
import Model.Bill;
import Model.Client;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * * Represents the graphical user interface for managing clients.
 * *
 * * <p>This class provides a graphical interface for adding, editing, and deleting clients from a database.
 * * It allows viewing the list of clients in a table format.</p>
 */
public class ClientInterface extends JFrame {
    private JPanel panel1;
    private JButton addClientButton;
    private JButton editClientButton;
    private JButton deleteClientButton;
    private JButton viewClientsButton;
    private JButton mainInterfaceButton;
    private JTextField textFieldId;
    private JTextField textFieldName;
    private JTextField textFieldAddress;
    private JTextField textFieldEmail;
    private JTextField textFieldAge;
    private JTable table1;

    /**
     * Constructs a new ClientInterface.
     * Sets up the GUI components and event listeners.
     */
    public ClientInterface() {
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
        addClientButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (textFieldId.getText().isEmpty() || textFieldName.getText().isEmpty() || textFieldAddress.getText().isEmpty()
                        || textFieldEmail.getText().isEmpty() || textFieldAge.getText().isEmpty())
                    JOptionPane.showMessageDialog(panel1, "COMPLETE ALL THE FIELDS!", "ALERT", JOptionPane.WARNING_MESSAGE);
                else {
                    ClientBLL clientBLL = new ClientBLL();
                    Client cl1 = null;
                    Client firstClient = new Client(Integer.parseInt(textFieldId.getText()), textFieldName.getText(), textFieldAddress.getText(),
                            textFieldEmail.getText(), Integer.parseInt(textFieldAge.getText()));
                    try {
                        cl1 = clientBLL.findClientById(Integer.parseInt(textFieldId.getText()));
                        if (cl1 != null)
                            JOptionPane.showMessageDialog(panel1, "Client with id " + Integer.parseInt(textFieldId.getText()) + " already exists", "ALERT", JOptionPane.WARNING_MESSAGE);
                        else {
                            clientBLL.insert(firstClient);
                            System.out.println("CLIENT INSERTED!");
                            JOptionPane.showMessageDialog(panel1, "Client inserted!");
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
        editClientButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (textFieldId.getText().isEmpty() || textFieldName.getText().isEmpty() || textFieldAddress.getText().isEmpty()
                        || textFieldEmail.getText().isEmpty() || textFieldAge.getText().isEmpty())
                    JOptionPane.showMessageDialog(panel1, "COMPLETE ALL THE FIELDS!", "ALERT", JOptionPane.WARNING_MESSAGE);
                else {
                    ClientBLL clientBLL = new ClientBLL();
                    Client cl1 = null;
                    Client firstClient = new Client(Integer.parseInt(textFieldId.getText()), textFieldName.getText(), textFieldAddress.getText(),
                            textFieldEmail.getText(), Integer.parseInt(textFieldAge.getText()));
                    try {
                        cl1 = clientBLL.findClientById(Integer.parseInt(textFieldId.getText()));
                        if (cl1 == null)
                            JOptionPane.showMessageDialog(panel1, "Client with id " + Integer.parseInt(textFieldId.getText()) + " cannot be updated because it does not exist!", "ALERT", JOptionPane.WARNING_MESSAGE);
                        else {
                            clientBLL.update(firstClient);
                            System.out.println("CLIENT UPDATED!");
                            JOptionPane.showMessageDialog(panel1, "Client updated!");
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
        deleteClientButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int ok = 1;
                if (textFieldId.getText().isEmpty())
                    JOptionPane.showMessageDialog(panel1, "COMPLETE THE ID FIELD!", "ALERT", JOptionPane.WARNING_MESSAGE);
                else {
                    ClientBLL clientBLL = new ClientBLL();
                    Client cl1 = null;
                    if (textFieldAge.getText().isEmpty()) textFieldAge.setText("0");
                    Client firstClient = new Client(Integer.parseInt(textFieldId.getText()), textFieldName.getText(), textFieldAddress.getText(),
                            textFieldEmail.getText(), Integer.parseInt(textFieldAge.getText()));
                    try {
                        cl1 = clientBLL.findClientById(Integer.parseInt(textFieldId.getText()));
                        if (cl1 == null)
                            JOptionPane.showMessageDialog(panel1, "Client with id " + Integer.parseInt(textFieldId.getText()) + " cannot be deleted because it does not exist!", "ALERT", JOptionPane.WARNING_MESSAGE);
                        else {
                            List<Bill> list = new ArrayList<>();
                            BillBLL billBLL = new BillBLL();
                            list = billBLL.findAll();
                            for (Bill bill : list) {
                                if (bill.client_id() == firstClient.getId()) {
                                    JOptionPane.showMessageDialog(panel1, "Client with id " + Integer.parseInt(textFieldId.getText()) + " cannot be deleted because it is a bill with him!", "ALERT", JOptionPane.WARNING_MESSAGE);
                                    ok = 0;
                                }
                            }
                            if (ok == 1) {
                                clientBLL.delete(firstClient.getId(), firstClient);
                                System.out.println("CLIENT DELETED!");
                                JOptionPane.showMessageDialog(panel1, "Client deleted!");
                                textFieldAge.setText("");
                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
        viewClientsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<Client> list = new ArrayList<>();
                ClientBLL clientBLL = new ClientBLL();
                GenerateTable<Client> t = new GenerateTable<>();
                list = clientBLL.findAll();
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
