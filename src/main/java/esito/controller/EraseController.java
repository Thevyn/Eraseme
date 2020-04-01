package esito.controller;

import esito.model.Customer;
import esito.repository.CustomerRepository;
import esito.repository.EraseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class EraseController {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private EraseRepository eraseRepository;

    /**
     * POST Method to erase database data
     *
     * @param taskName task to run
     * @param identifier erase params
     */
    @RequestMapping("/eraseMe")
    public void eraseMe(String taskName, String[] identifier) {
       eraseRepository.eraseMe(taskName, identifier);
    }

    // URL: ../api/Erase_CUSTOMER?custnum=x
    @GetMapping("/Erase_CUSTOMER")
    public void eraseCustomer(@RequestParam String custnum) {
        eraseRepository.eraseMe("CUSTOMER",new String[] {custnum});
    }

    @RequestMapping("/getCustomers")
    public List<Customer> getCustomers() {
        return customerRepository.findAll();
    }


}
