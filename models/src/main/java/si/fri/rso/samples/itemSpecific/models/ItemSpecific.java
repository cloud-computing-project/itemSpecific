package si.fri.rso.samples.itemSpecific.models;

import org.eclipse.persistence.annotations.UuidGenerator;

import javax.persistence.*;
import java.util.List;

@Entity(name = "itemSpecific")
@NamedQueries(value =
{
    @NamedQuery(name = "ItemSpecific.getAll", query = "SELECT c FROM itemSpecific c")
})
@UuidGenerator(name = "idGenerator")
public class ItemSpecific {

    @Id
    @GeneratedValue(generator = "idGenerator")
    private String id;

    @Column(name = "item_type")
    private String itemType;

    private String condition;

    private String features;

    private String brand;

    @Column(name = "product_id")
    private String productId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return itemType;
    }

    public void setType(String type) {
        this.itemType = type;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getFeatures() {
        return features;
    }

    public void setFeatures(String features) {
        this.features = features;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
}