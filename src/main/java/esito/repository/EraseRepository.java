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
        try {
            IContext context = ContextFactory.createEraseContext(DataSourceUtils.getConnection(dataSource), identifier);

            Class<?> eraseClass = Class.forName("forgetme.Erase_" + taskName.toUpperCase());
            Object erase = eraseClass.getDeclaredConstructor().newInstance();
            Method runMethod = erase.getClass().getMethod("run", IContext.class);
            runMethod.invoke(erase, context);

        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    public void eraseMe(String[] identifier) {
        try {
            IContext context = ContextFactory.createEraseContext(DataSourceUtils.getConnection(dataSource), identifier);

          new Erase_CUSTOMER().run(context);


        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
}
