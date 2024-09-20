package BLL;

import Dao.BillDAO;
import Model.Bill;

import java.util.ArrayList;
import java.util.List;

public class BillBLL {
    private BillDAO billDAO;

    public BillBLL() {
        billDAO = new BillDAO(Bill.class);
    }

    public List<Bill> findAll() {
        List<Bill> b = new ArrayList<>();
        b = billDAO.findAll();
        return b;
    }

    public Bill insert(Bill bill) throws IllegalAccessException {
        Bill b = billDAO.insert(bill);
        return b;
    }

}
