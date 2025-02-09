package org.example.studentsvoice.resource;

import jakarta.annotation.security.DeclareRoles;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.example.studentsvoice.cdi.producer.MyLogger;
import org.example.studentsvoice.entity.TimeRange;
import org.example.studentsvoice.service.TimeRangeService;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * TimeRangeResource handles RESTful requests for time range-related operations.
 * It enforces security constraints based on user roles (ADMIN, TEACHER, STUDENT).
 */
@DeclareRoles({"ADMIN", "TEACHER", "STUDENT"})
@Path("timeRanges")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TimeRangeResource {

    @Inject
    @MyLogger
    private Logger LOGGER;

    @Inject
    private TimeRangeService timeRangeService;

    /**
     * Retrieves all time ranges. Accessible only by ADMIN.
     *
     * @return A list of time ranges.
     */
    @GET
    @RolesAllowed("ADMIN")
    @Operation(summary = "Get all time ranges", description = "Returns a list of all time ranges in the database.")
    public Response getAllTimeRanges() {
        LOGGER.info("Getting all time ranges");

        try {
            List<TimeRange> timeRanges = timeRangeService.findAll();
            return Response.ok(timeRanges).build();
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

    /**
     * Retrieves a time range by ID. Accessible by ADMIN, TEACHER, and STUDENT roles.
     *
     * @param id The ID of the time range to retrieve.
     * @return The time range with the specified ID.
     */
    @GET
    @Path("{id}")
    @RolesAllowed({"ADMIN", "TEACHER", "STUDENT"})
    @Operation(summary = "Get a time range by ID", description = "Returns a time range by its ID.")
    public Response getTimeRangeById(@PathParam("id") Long id) {
        LOGGER.info("Getting time range by ID: " + id);

        Optional<TimeRange> timeRange = timeRangeService.findById(id);
        if (timeRange.isPresent()) {
            return Response.ok(timeRange.get()).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    /**
     * Creates a new time range. Accessible only by ADMIN.
     *
     * @param timeRange The time range object to create.
     * @return Response indicating the creation status.
     */
    @POST
    @RolesAllowed("ADMIN")
    @Operation(summary = "Create a new time range", description = "Creates a new time range in the database.")
    public Response createTimeRange(TimeRange timeRange) {
        LOGGER.info("Creating time range...");

        try {
            timeRangeService.save(timeRange);
            return Response.status(Response.Status.CREATED).entity(timeRange).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    /**
     * Updates an existing time range. Accessible only by ADMIN.
     *
     * @param id        The ID of the time range to update.
     * @param timeRange The updated time range data.
     * @return Response indicating the update status.
     */
    @PUT
    @Path("{id}")
    @RolesAllowed("ADMIN")
    @Operation(summary = "Update a time range", description = "Updates an existing time range in the database.")
    public Response updateTimeRange(@PathParam("id") Long id, TimeRange timeRange) {
        LOGGER.info("Updating time range with ID: " + id);

        try {
            timeRange.setId(id);
            Optional<TimeRange> updatedTimeRange = timeRangeService.update(timeRange);
            if (updatedTimeRange.isPresent()) {
                return Response.ok(updatedTimeRange.get()).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    /**
     * Deletes a time range by ID. Accessible only by ADMIN.
     *
     * @param id The ID of the time range to delete.
     * @return Response indicating the deletion status.
     */
    @DELETE
    @Path("{id}")
    @RolesAllowed("ADMIN")
    @Operation(summary = "Delete a time range", description = "Deletes an existing time range by ID.")
    public Response deleteTimeRange(@PathParam("id") Long id) {
        LOGGER.info("Deleting time range with ID: " + id);

        try {
            timeRangeService.deleteById(id);
            return Response.noContent().build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
}