package esito.repository;

import forgetme.Erase_CUSTOMER;
import no.esito.anonymizer.ContextFactory;
import no.esito.anonymizer.IContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.lang.reflect.Method;
import java.sql.Connection;


@Repository
public class EraseRepository {

    @Autowired
    DataSource dataSource;

    public void eraseMe(String taskName, String[] identifier) {
        Connection connection = null;
        try {
            connection = DataSourceUtils.getConnection(dataSource);

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

    public void eraseMe(String[] identifier) {
        Connection connection = null;
        try {
            connection = DataSourceUtils.getConnection(dataSource);

            new ContextFactory();
            IContext context = ContextFactory.createEraseContext(connection, identifier);

          new Erase_CUSTOMER().run(context);


        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
}
