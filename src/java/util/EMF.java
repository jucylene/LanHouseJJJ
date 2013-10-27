package util;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Monnalisa Medeiros
 */
public class EMF {
    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("LanHousePU");

    public static EntityManagerFactory getEntityManagerFactory() {
        return emf;
    }
}