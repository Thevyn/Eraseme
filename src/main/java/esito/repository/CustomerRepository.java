package esito.repository;

import esito.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CustomerRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void eraseCustomer(String customerNo) {
        this.jdbcTemplate.update(
                "Update CUSTOM set name = 'firstname lastname', email = 'email@email.com' where customerNo = ?",
                customerNo);
    }

    public List<Customer> findAll() {

        List<Customer> result = jdbcTemplate.query(
                "SELECT creditCard, customerNo, email, name, password, phone FROM custom",
                (rs, rowNum) -> new Customer(rs.getString("creditCard"),
                        rs.getInt("customerNo"), rs.getString("email"),
                        rs.getString("name"), rs.getString("password"), rs.getString("phone"))
        );
        return result;
    }


    // Add new customer
    public void addCustomer(String creditCard, int customerNo,String name, String email, String password, String phone) {
        jdbcTemplate.update("INSERT INTO custom( creditCard, customerNo, email, name, password, phone) VALUES (?,?,?,?,?,?)",
                creditCard, customerNo, email, name, password, phone);

    }
}
