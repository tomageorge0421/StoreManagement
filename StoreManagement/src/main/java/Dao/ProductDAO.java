package Dao;

import Model.Product;

public class ProductDAO extends AbstractDAO<Product> {
    public static boolean checkQuantity(int id, int quantity) {
        ProductDAO productDAO = new ProductDAO();
        Product p = productDAO.findById(id);
        if (p.getQuantity() < quantity) return false;
        return true;
    }

    public static void updateQuantity(int id, int quantity) throws IllegalAccessException {
        ProductDAO productDAO = new ProductDAO();
        Product p = productDAO.findById(id);
        p.setQuantity(p.getQuantity() - quantity);
        productDAO.update(p);
    }

}
