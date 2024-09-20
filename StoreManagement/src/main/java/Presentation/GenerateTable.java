package Presentation;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class to generate JTables dynamically from a list of objects.
 *
 * <p>This class provides a method to create a JTable based on a list of objects. Each object in the list
 * corresponds to a row in the table, and the fields of the object correspond to the columns.</p>
 *
 * @param <T> The type of objects in the list.
 */
public class GenerateTable<T> {
    /**
     * Creates a JTable from the given list of objects.
     *
     * @param list The list of objects from which to create the table.
     * @return A JTable representing the data from the list, or null if the list is empty or null.
     */
    public JTable createTable(List<T> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        DefaultTableModel tableModel = new DefaultTableModel();
        Field[] fields = list.get(0).getClass().getDeclaredFields();
        for (Field field : fields) {
            tableModel.addColumn(field.getName());
        }
        for (Object o : list) {
            List<Object> secondList = new ArrayList<>();
            Field[] fields2 = o.getClass().getDeclaredFields();
            for (Field field : fields2) {
                field.setAccessible(true);
                try {
                    secondList.add(field.get(o));
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
            tableModel.addRow(secondList.toArray());
        }
        JTable goodTable = new JTable(tableModel);
        return goodTable;
    }
}
