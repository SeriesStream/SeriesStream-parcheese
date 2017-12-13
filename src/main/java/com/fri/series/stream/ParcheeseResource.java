package com.fri.series.stream;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import com.kumuluz.ee.logs.cdi.Log;
import com.kumuluz.ee.logs.LogManager;
import com.kumuluz.ee.logs.Logger;
import com.kumuluz.ee.discovery.annotations.DiscoverService;
import javax.inject.Inject;
import java.util.Optional;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

@Produces(MediaType.APPLICATION_JSON)
@Path("parcheeses")
@Log
@ApplicationScoped
public class ParcheeseResource {

    private Logger log = LogManager.getLogger(ParcheeseResource.class.getName());

    @Inject
    @DiscoverService("series-stream-catalog")
    private Optional<String> baseUrl;
    //private String baseUrl;

    private Client httpClient = ClientBuilder.newClient();

    @GET
    public Response getAllParcheeses() {
        List<Parcheese> parcheeses = ParcheesesDatabase.getParcheeses();
        return Response.ok(parcheeses).build();
    }

    @GET
    @Path("{id}/episode")
    public Response getParcheesedEpisode(@PathParam("id") int id) {
        Parcheese parcheese = ParcheesesDatabase.getParcheese(id);
        if(parcheese != null){
            if (baseUrl.isPresent()) {
                try {
                    String link = baseUrl.get();
                    System.out.println(link);
                    return httpClient
                            .target(link + "/v1/episodes/" + parcheese.getEpisodeId())
                            .request().get();
                } catch (WebApplicationException | ProcessingException e) {
                    log.error(e);
                    throw new InternalServerErrorException(e);
                }
            }
            log.error("baseUrl ni prisoten");
            return Response.status(Response.Status.NOT_FOUND).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @GET
    @Path("{id}")
    public Response getParcheese(@PathParam("id") int id) {
        Parcheese parcheese = ParcheesesDatabase.getParcheese(id);
        if(parcheese != null){
            return Response.ok(parcheese).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @POST
    public Response addParcheese(Parcheese parcheese) {
        ParcheesesDatabase.addParcheese(parcheese);
        return Response.noContent().build();
    }

    @DELETE
    @Path("{id}")
    public Response deleteParcheese(@PathParam("id") int id) {
        ParcheesesDatabase.deleteParcheese(id);
        return Response.noContent().build();
    }
}
