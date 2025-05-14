package fr.istic.tlc.resources;

import java.util.concurrent.atomic.AtomicInteger;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/abtest")
public class ABTestResource {
    private static final AtomicInteger versionAClicks = new AtomicInteger(0);
    private static final AtomicInteger versionAAccesses = new AtomicInteger(0);
    private static final AtomicInteger versionBClicks = new AtomicInteger(0);
    private static final AtomicInteger versionBAccesses = new AtomicInteger(0);

    @POST
    @Path("/log")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response logEvent(ABTestEvent event) {
        if ("A".equals(event.getVersion())) {
            if ("click".equals(event.getEvent())) versionAClicks.incrementAndGet();
            if ("page_access".equals(event.getEvent())) versionAAccesses.incrementAndGet();
        } else if ("B".equals(event.getVersion())) {
            if ("click".equals(event.getEvent())) versionBClicks.incrementAndGet();
            if ("page_access".equals(event.getEvent())) versionBAccesses.incrementAndGet();
        }
        return Response.ok().build();
    }

    @GET
    @Path("/results")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getResults() {
        return Response.ok(new ABTestResults(
            versionAClicks.get(), versionAAccesses.get(),
            versionBClicks.get(), versionBAccesses.get()
        )).build();
    }

    public static class ABTestEvent {
        private String version;
        private String event;

        public String getVersion() { return version; }
        public void setVersion(String version) { this.version = version; }
        public String getEvent() { return event; }
        public void setEvent(String event) { this.event = event; }
    }

    public static class ABTestResults {
        private final int versionAClicks;
        private final int versionAAccesses;
        private final int versionBClicks;
        private final int versionBAccesses;

        public ABTestResults(int versionAClicks, int versionAAccesses, int versionBClicks, int versionBAccesses) {
            this.versionAClicks = versionAClicks;
            this.versionAAccesses = versionAAccesses;
            this.versionBClicks = versionBClicks;
            this.versionBAccesses = versionBAccesses;
        }

        public int getVersionAClicks() { return versionAClicks; }
        public int getVersionAAccesses() { return versionAAccesses; }
        public int getVersionBClicks() { return versionBClicks; }
        public int getVersionBAccesses() { return versionBAccesses; }
    }
}
