package si.fri.rso.samples.itemSpecific.api.v1.resources;

import com.kumuluz.ee.logs.cdi.Log;
import si.fri.rso.samples.itemSpecific.models.ItemSpecific;
import si.fri.rso.samples.itemSpecific.services.ItemSpecificBean;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;


@RequestScoped
@Path("/itemSpecific")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Log
public class ItemSpecificResource {

    @Inject
    private ItemSpecificBean itemSpecificBean;

    @Context
    protected UriInfo uriInfo;

    @GET
    public Response getItemSpecific() {

        List<ItemSpecific> itemSpecifics = itemSpecificBean.getItemSpecific(uriInfo);

        return Response.ok(itemSpecifics).build();
    }

    @GET
    @Path("/{itemSpecificId}")
    public Response getItemSpecific(@PathParam("itemSpecificId") String itemSpecificId) {

        ItemSpecific itemSpecific = itemSpecificBean.getItemSpecific(itemSpecificId);

        if (itemSpecific == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.status(Response.Status.OK).entity(itemSpecific).build();
    }

    @POST
    public Response createItemSpecific(ItemSpecific itemSpecific) {

        if ((itemSpecific.getType() == null || itemSpecific.getType().isEmpty()) ) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } else {
            itemSpecific = itemSpecificBean.createItemSpecific(itemSpecific);
        }

        if (itemSpecific.getId() != null) {
            return Response.status(Response.Status.CREATED).entity(itemSpecific).build();
        } else {
            return Response.status(Response.Status.CONFLICT).entity(itemSpecific).build();
        }
    }

    @PUT
    @Path("{itemSpecificId}")
    public Response putItemSpecific(@PathParam("itemSpecificId") String itemSpecificId, ItemSpecific itemSpecific) {

        itemSpecific = itemSpecificBean.putItemSpecific(itemSpecificId, itemSpecific);

        if (itemSpecific == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } else {
            if (itemSpecific.getId() != null)
                return Response.status(Response.Status.OK).entity(itemSpecific).build();
            else
                return Response.status(Response.Status.NOT_MODIFIED).build();
        }
    }

    @DELETE
    @Path("{itemSpecificId}")
    public Response deleteItemSpecific(@PathParam("itemSpecificId") String itemSpecificId) {

        boolean deleted = itemSpecificBean.deleteItemSpecific(itemSpecificId);

        if (deleted) {
            return Response.status(Response.Status.GONE).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}
