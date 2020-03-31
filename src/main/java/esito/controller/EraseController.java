package esito.controller;

import esito.Connect;

import esito.model.Customer;
import esito.repository.CustomerRepository;
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

    /**
     * POST Method to erase database data
     *
     * @param taskName task to run
     * @param identifier erase params
     */
    @RequestMapping("/eraseMe")
    public void eraseMe(String taskName, String[] identifier) {
        Connection connection = null;
        try {
            connection = Connect.createDefaultConnection();

            new ContextFactory();
            IContext context = ContextFactory.createEraseContext(connection, identifier);

            Class<?> eraseClass = Class.forName("forgetme.Erase_" + taskName.toUpperCase());
            Object erase = eraseClass.getDeclaredConstructor().newInstance();
            Method runMethod = erase.getClass().getMethod("run", IContext.class);
            runMethod.invoke(erase, context);

            connection.close();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    @RequestMapping("/Erase_CUSTOMER")
    public void eraseCustomer(String identifier) {
        customerRepository.eraseCustomer(identifier);
    }

    @RequestMapping("/getCustomers")
    public List<Customer> getCustomers() {

        List<Customer> list = customerRepository.findAll();

        return list;
    }


}
