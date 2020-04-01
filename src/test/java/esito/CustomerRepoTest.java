package esito;

import esito.model.Customer;
import esito.repository.CustomerRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CustomerRepoTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    public void findUserById() {
        Customer customer = customerRepository.findCustomerById("1000234");
        Assert.assertNotNull(customer);
    }
}
