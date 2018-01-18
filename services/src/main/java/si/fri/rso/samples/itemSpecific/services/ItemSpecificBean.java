package si.fri.rso.samples.itemSpecific.services;

import com.kumuluz.ee.discovery.annotations.DiscoverService;
import com.kumuluz.ee.logs.LogManager;
import com.kumuluz.ee.logs.Logger;
import com.kumuluz.ee.rest.beans.QueryParameters;
import com.kumuluz.ee.rest.utils.JPAUtils;
import si.fri.rso.samples.itemSpecific.models.ItemSpecific;
import si.fri.rso.samples.itemSpecific.models.Product;
import si.fri.rso.samples.itemSpecific.services.config.RestProperties;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.UriInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@ApplicationScoped
public class ItemSpecificBean {

    private Logger log = LogManager.getLogger(ItemSpecificBean.class.getName());

    @Inject
    private EntityManager em;

    private Client httpClient;

    @Inject
    private RestProperties restProperties;

    @Inject
    private ItemSpecificBean itemSpecificBean;

    @Inject
    @DiscoverService("products")
    private Optional<String> baseUrlProducts;

    @PostConstruct
    private void init() {
        httpClient = ClientBuilder.newClient();
    }

    public List<ItemSpecific> getItemSpecific() {

        TypedQuery<ItemSpecific> query = em.createNamedQuery("ItemSpecific.getAll", ItemSpecific.class);

        return query.getResultList();

    }

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

        if (restProperties.isProductServiceEnabled()) {
            List<Product> products = itemSpecificBean.getProducts(itemSpecificId);
            log.info("list of products: " + products.toString());
            itemSpecific.setProducts(products);
            log.info("list of products for itemSpecificId: " + itemSpecific.getProducts().toString());
        }

        return itemSpecific;
    }

    public List<Product> getProducts(String itemSpecificId) {
        log.info("base url products " + baseUrlProducts);
        if (baseUrlProducts.isPresent()) {
            try {
                return httpClient
                        .target(baseUrlProducts.get() + "/v1/products?where=itemSpecificId:EQ:" + itemSpecificId)
                        .request().get(new GenericType<List<Product>>() {
                        });
            } catch (WebApplicationException | ProcessingException e) {
                log.error(e);
                throw new InternalServerErrorException(e);
            }
        }

        return new ArrayList<>();
    }

    public List<Product> getProductsFallback(String customerId) {
        return new ArrayList<>();
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
