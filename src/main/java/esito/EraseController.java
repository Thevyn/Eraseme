package esito;

import forgetme.Erase_CUSTOMER;
import no.esito.anonymizer.ContextFactory;
import no.esito.anonymizer.IContext;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Method;
import java.sql.Connection;

@RestController
public class EraseController {

    @PostMapping("/eraseMe")
    public void eraseMe(String taskName, String[] identifier) {


        Connection connection = null;
        try {
            connection = Connect.createDefaultConnection();

            new ContextFactory();
            IContext context = ContextFactory.createEraseContext(connection, identifier);

            Class<?> eraseClass = Class.forName("forgetme.Erase_" + taskName.toUpperCase());
            Object erase = eraseClass.getDeclaredConstructor().newInstance();

            Method method = erase.getClass().getMethod("run", IContext.class);
            method.invoke(erase, context);

        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }




    }
}
