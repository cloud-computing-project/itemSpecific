package si.fri.rso.samples.itemSpecific.services.producers;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

public class PersistenceProducer {

    @PersistenceUnit(unitName = "itemSpecific-jpa")
    private EntityManagerFactory emf;

    @Produces
    @RequestScoped
    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void disposeEntityManager(@Disposes EntityManager entityManager) {

        if (entityManager.isOpen()) {
            entityManager.close();
        }
    }
}
