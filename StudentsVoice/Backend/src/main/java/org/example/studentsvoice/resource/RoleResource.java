package org.example.studentsvoice.resource;

import jakarta.annotation.security.DeclareRoles;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.example.studentsvoice.cdi.producer.MyLogger;
import org.example.studentsvoice.dto.Role.RoleDTO;
import org.example.studentsvoice.entity.Role;
import org.example.studentsvoice.mapper.RoleMapper;
import org.example.studentsvoice.service.RoleService;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * RoleResource handles RESTful requests for role-related operations.
 * It enforces security constraints based on user roles (ADMIN, TEACHER, STUDENT).
 */
@DeclareRoles({"ADMIN", "TEACHER", "STUDENT"})
@Path("roles")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RoleResource {

    @Inject
    @MyLogger
    private Logger LOGGER;

    @Inject
    private RoleService roleService;

    @Inject
    private RoleMapper roleMapper;

    /**
     * Gets all roles. Accessible only by ADMIN.
     *
     * @return A list of all roles.
     */
    @GET
    @RolesAllowed("ADMIN")
    @Operation(summary = "Get all roles", description = "Returns a list of all roles.")
    public Response getAllRoles() {
        try {
            LOGGER.info("Fetching all roles.");
            List<Role> roles = roleService.findAll();

            // Map Role entities to RoleDTO
            List<RoleDTO> rolesDTO = roleMapper.toDTOList(roles);
            return Response.ok(rolesDTO).build();
        } catch (Exception e) {
            LOGGER.severe("Error fetching roles: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    /**
     * Gets a role by ID. Accessible by ADMIN, TEACHER, and STUDENT roles.
     *
     * @param id The ID of the role to retrieve.
     * @return The role with the specified ID.
     */
    @GET
    @Path("{id}")
    @RolesAllowed({"ADMIN", "TEACHER", "STUDENT"})
    @Operation(summary = "Get a role by ID", description = "Returns a role by its ID.")
    public Response getRoleById(@PathParam("id") Long id) {
        LOGGER.info("Fetching role with ID: " + id);
        Optional<Role> role = roleService.findById(id);
        if (role.isPresent()) {
            RoleDTO roleDTO = roleMapper.toDTO(role.get());
            return Response.ok(roleDTO).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("Role not found.").build();
        }
    }

    /**
     * Gets a role by name. Accessible by ADMIN, TEACHER, and STUDENT roles.
     *
     * @param name The name of the role to retrieve.
     * @return The role with the specified name.
     */
    @GET
    @Path("name/{name}")
    @RolesAllowed({"ADMIN", "TEACHER", "STUDENT"})
    @Operation(summary = "Get a role by name", description = "Returns a role by its name.")
    public Response getRoleByName(@PathParam("name") String name) {
        LOGGER.info("Fetching role with name: " + name);
        Optional<Role> role = roleService.findByName(name);
        if (role.isPresent()) {
            RoleDTO roleDTO = roleMapper.toDTO(role.get());
            return Response.ok(roleDTO).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("Role not found.").build();
        }
    }

    /**
     * Creates a new role. Accessible only by ADMIN.
     *
     * @param roleRequest The role data to create.
     * @return Response indicating the creation status.
     */
    @POST
    @RolesAllowed("ADMIN")
    @Operation(summary = "Create a new role", description = "Creates a new role in the system.")
    public Response createRole(RoleDTO roleRequest) {
        LOGGER.info("Creating a new role.");
        try {
            // Map RoleDTO to Role entity
            Role createdRole = roleService.save(roleMapper.toEntity(roleRequest));

            // Map created Role entity to RoleDTO
            RoleDTO roleDTO = roleMapper.toDTO(createdRole);
            return Response.status(Response.Status.CREATED).entity(roleDTO).build();
        } catch (Exception e) {
            LOGGER.severe("Error creating role: " + e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    /**
     * Updates an existing role. Accessible only by ADMIN.
     *
     * @param id   The ID of the role to update.
     * @param roleRequest The updated role data.
     * @return Response indicating the update status.
     */
    @PUT
    @Path("{id}")
    @RolesAllowed("ADMIN")
    @Operation(summary = "Update a role", description = "Updates an existing role in the system.")
    public Response updateRole(@PathParam("id") Long id, RoleDTO roleRequest) {
        LOGGER.info("Updating role with ID: " + id);
        try {
            // Map RoleDTO to Role entity
            Role role = roleMapper.toEntity(roleRequest);
            role.setId(id);
            Optional<Role> updatedRole = roleService.update(role);

            if (updatedRole.isPresent()) {
                // Map updated Role entity to RoleDTO
                RoleDTO roleDTO = roleMapper.toDTO(updatedRole.get());
                return Response.ok(roleDTO).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).entity("Role not found.").build();
            }
        } catch (Exception e) {
            LOGGER.severe("Error updating role: " + e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    /**
     * Deletes a role by ID. Accessible only by ADMIN.
     *
     * @param id The ID of the role to delete.
     * @return Response indicating the deletion status.
     */
    @DELETE
    @Path("{id}")
    @RolesAllowed("ADMIN")
    @Operation(summary = "Delete a role", description = "Deletes an existing role by ID.")
    public Response deleteRole(@PathParam("id") Long id) {
        LOGGER.info("Deleting role with ID: " + id);
        try {
            roleService.deleteById(id);
            return Response.noContent().build();
        } catch (Exception e) {
            LOGGER.severe("Error deleting role: " + e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
}
