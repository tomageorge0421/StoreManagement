package BLL;

import Dao.OrdersDAO;
import Model.Orders;

import java.util.ArrayList;
import java.util.List;

public class OrdersBLL {
    private OrdersDAO ordersDAO;

    public OrdersBLL() {
        ordersDAO = new OrdersDAO();
    }

    public Orders findOrdersById(int id) {
        Orders or = ordersDAO.findById(id);
        if (or == null) {
            return null;
        }
        return or;
    }

    public List<Orders> findAll() {
        List<Orders> o = new ArrayList<>();
        o = ordersDAO.findAll();
        return o;
    }

    public Orders insert(Orders order) throws IllegalAccessException {
        Orders o = ordersDAO.insert(order);
        return o;
    }

    public Orders delete(int id, Orders order) throws IllegalAccessException {
        Orders o = ordersDAO.delete(id, order);
        return o;
    }
}
