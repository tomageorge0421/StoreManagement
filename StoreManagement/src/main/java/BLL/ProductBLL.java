package BLL;

import Dao.ProductDAO;
import Model.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductBLL {
    private ProductDAO productDAO;

    public ProductBLL() {
        productDAO = new ProductDAO();
    }

    public Product findProductById(int id) {
        Product pr = productDAO.findById(id);
        if (pr == null) {
            return null;
        }
        return pr;
    }

    public List<Product> findAll() {
        List<Product> p = new ArrayList<>();
        p = productDAO.findAll();
        return p;
    }

    public Product insert(Product product) throws IllegalAccessException {
        Product p = productDAO.insert(product);
        return p;
    }

    public Product update(Product product) throws IllegalAccessException {
        Product p = productDAO.update(product);
        return p;
    }

    public Product delete(int id, Product product) throws IllegalAccessException {
        Product p = productDAO.delete(id, product);
        return p;
    }

    public boolean CheckQuantity(int id, int quantity) {
        return ProductDAO.checkQuantity(id, quantity);
    }

    public void updateQuantity(int id, int quantity) throws IllegalAccessException {
        ProductDAO.updateQuantity(id, quantity);
    }
}
