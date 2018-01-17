package si.fri.rso.samples.itemSpecific.services;

import com.kumuluz.ee.logs.LogManager;
import com.kumuluz.ee.logs.Logger;
import com.kumuluz.ee.rest.beans.QueryParameters;
import com.kumuluz.ee.rest.utils.JPAUtils;
import si.fri.rso.samples.itemSpecific.models.ItemSpecific;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.client.Client;
import javax.ws.rs.core.UriInfo;
import java.util.List;


@ApplicationScoped
public class ItemSpecificBean {

    private Logger log = LogManager.getLogger(ItemSpecificBean.class.getName());

    @Inject
    private EntityManager em;

    private Client httpClient;

    public List<ItemSpecific> getItemSpecific(UriInfo uriInfo) {

        QueryParameters queryParameters = QueryParameters.query(uriInfo.getRequestUri().getQuery())
                .defaultOffset(0)
                .build();

        return JPAUtils.queryEntities(em, ItemSpecific.class, queryParameters);

    }

    public ItemSpecific getItemSpecific(String itemSpecificId) {

        ItemSpecific itemSpecific = em.find(ItemSpecific.class, itemSpecificId);

        if (itemSpecific == null){
            throw new NotFoundException();
        }

        return itemSpecific;
    }

    public ItemSpecific createItemSpecific(ItemSpecific itemSpecific) {

        try {
            beginTx();
            em.persist(itemSpecific);
            commitTx();
        } catch (Exception e) {
            rollbackTx();
        }

        return itemSpecific;
    }

    public ItemSpecific putItemSpecific(String itemSpecificId, ItemSpecific itemSpecific) {

        ItemSpecific c = em.find(ItemSpecific.class, itemSpecific);

        if (c == null) {
            return null;
        }

        try {
            beginTx();
            itemSpecific.setId(c.getId());
            itemSpecific = em.merge(itemSpecific);
            commitTx();
        } catch (Exception e) {
            rollbackTx();
        }

        return itemSpecific;
    }

    public boolean deleteItemSpecific(String itemSpecificId) {

        ItemSpecific itemSpecific = em.find(ItemSpecific.class, itemSpecificId);

        if (itemSpecific != null) {
            try {
                beginTx();
                em.remove(itemSpecific);
                commitTx();
            } catch (Exception e) {
                rollbackTx();
            }
        } else
            return false;

        return true;
    }

    private void beginTx() {
        if (!em.getTransaction().isActive())
            em.getTransaction().begin();
    }

    private void commitTx() {
        if (em.getTransaction().isActive())
            em.getTransaction().commit();
    }

    private void rollbackTx() {
        if (em.getTransaction().isActive())
            em.getTransaction().rollback();
    }
}
