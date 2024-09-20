package Dao;

import Model.Bill;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.logging.Logger;

import Connection.ConnectionFactory;


public class BillDAO {
    protected static final Logger LOGGER = Logger.getLogger(AbstractDAO.class.getName());
    private final Class<Bill> type;

    public BillDAO(Class<Bill> type) {
        this.type = type;

    }

    private String createSelectAllQuery() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" * ");
        sb.append(" FROM ");
        sb.append(type.getSimpleName());
        return sb.toString();
    }

    public List<Bill> findAll() {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = createSelectAllQuery();
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();
            return createObjects(resultSet);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return null;
    }

    private List<Bill> createObjects(ResultSet resultSet) {
        List<Bill> list = new ArrayList<Bill>();
        try {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int client_id = resultSet.getInt("client_id");
                int product_id = resultSet.getInt("product_id");
                int quantity = resultSet.getInt("quantity");
                BigDecimal total_price = resultSet.getBigDecimal("total_price");

                Bill instance = new Bill(id, client_id, product_id, quantity, total_price);
                list.add(instance);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Bill insert(Bill t) throws IllegalAccessException {

        Connection connection = null;
        PreparedStatement statement = null;
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT ");
        sb.append(" INTO ");
        sb.append(type.getSimpleName());
        sb.append(" VALUES (");
        int ctr = 0;
        Field[] f2 = t.getClass().getDeclaredFields();
        for (Field field : f2) {
            field.setAccessible(true);
            Object a = new Object();
            a = field.get(t);
            try {
                if (ctr == 0) {
                    ctr++;
                    if (a.getClass().getSimpleName().equals("String")) {
                        sb.append(" ' ");
                        sb.append(String.valueOf(field.get(t)));
                        sb.append(" ' ");
                    } else {
                        sb.append(String.valueOf(field.get(t)));
                    }
                } else {
                    sb.append(" , ");
                    if (a.getClass().getSimpleName().equals("String")) {
                        sb.append(" ' ");
                        sb.append(String.valueOf(field.get(t)));
                        sb.append(" ' ");
                    } else {
                        sb.append(String.valueOf(field.get(t)));
                    }
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        sb.append(")");
        String sql = sb.toString();
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(sql);
            System.out.println(sql);
            statement.execute(sql);
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:Insert " + e.getMessage());
        } finally {
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return null;
    }
}
