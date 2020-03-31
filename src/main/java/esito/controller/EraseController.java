package esito.controller;

import esito.Connect;

import esito.model.Customer;
import esito.repository.CustomerRepository;
import esito.repository.EraseRepository;
import no.esito.anonymizer.ContextFactory;
import no.esito.anonymizer.IContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Method;
import java.sql.Connection;
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

    @RequestMapping("/Erase_CUSTOMER")
    public void eraseCustomer(String[] identifier) {
        eraseRepository.eraseMe(identifier);
    }

    @RequestMapping("/getCustomers")
    public List<Customer> getCustomers() {
        return customerRepository.findAll();
    }


}
