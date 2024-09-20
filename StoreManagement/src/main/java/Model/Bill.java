package Model;

import java.math.BigDecimal;

public record Bill(int id, int client_id, int product_id, int quantity, BigDecimal total_price) {
    @Override
    public String toString() {
        return "Bill{" +
                "id=" + id +
                ", client_id=" + client_id +
                ", product_id=" + product_id +
                ", quantity=" + quantity +
                ", total_price=" + total_price +
                '}';
    }
}
