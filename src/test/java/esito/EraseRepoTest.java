package esito;

import esito.model.Customer;
import esito.repository.CustomerRepository;
import esito.repository.EraseRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EraseRepoTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private EraseRepository eraseRepository;

    @Test
    public void eraseCustomer() {
        String[] identifier = {"1000234"};
        eraseRepository.eraseMe(identifier);
        Customer customer = customerRepository.findCustomerById("1000234");

        Assert.assertEquals("firstname lastname", customer.getName() );
        Assert.assertEquals("epost@email.com",customer.getEmail());
    }
}
