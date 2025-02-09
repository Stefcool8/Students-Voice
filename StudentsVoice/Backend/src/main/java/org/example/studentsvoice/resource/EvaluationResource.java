package org.example.studentsvoice.resource;

import io.jsonwebtoken.Claims;
import jakarta.annotation.security.DeclareRoles;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.example.studentsvoice.cdi.producer.MyLogger;
import org.example.studentsvoice.dto.Evaluation.EvaluationAdminDTO;
import org.example.studentsvoice.dto.Evaluation.EvaluationStudentRequestDTO;
import org.example.studentsvoice.dto.Evaluation.EvaluationStudentResponseDTO;
import org.example.studentsvoice.dto.Evaluation.EvaluationTeacherDTO;
import org.example.studentsvoice.entity.Evaluation;
import org.example.studentsvoice.entity.User;
import org.example.studentsvoice.mapper.EvaluationMapper;
import org.example.studentsvoice.security.JWTService;
import org.example.studentsvoice.service.EvaluationService;
import org.example.studentsvoice.service.UserService;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;
import java.time.LocalDateTime;

/**
 * EvaluationResource handles RESTful requests for evaluation-related operations.
 * It enforces security constraints based on user roles (ADMIN, TEACHER, STUDENT).
 */
@DeclareRoles({"ADMIN", "TEACHER", "STUDENT"})
@Path("evaluations")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EvaluationResource {
    @Inject
    @MyLogger
    private Logger LOGGER;

    @Inject
    private EvaluationService evaluationService;

    @Inject
    private UserService userService;

    @Inject
    private JWTService jwtService;

    @Inject
    private EvaluationMapper evaluationMapper;

    /**
     * Gets a list of all evaluations. Accessible only by ADMIN.
     *
     * @return A list of evaluations.
     */
    @GET
    @RolesAllowed("ADMIN")
    @Operation(summary = "Get all evaluations", description = "Returns a list of all evaluations in the database.")
    public Response getAllEvaluations() {
        try {
            LOGGER.info("Getting all evaluations");

            List<Evaluation> evaluations = evaluationService.findAll();

            // Map Evaluation entities to EvaluationAdminDTO
            List<EvaluationAdminDTO> evaluationsDTO = evaluationMapper.toAdminDTOs(evaluations);

            return Response.ok(evaluationsDTO).build();
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

    /**
     * Gets an evaluation by ID. Accessible by ADMIN, TEACHER, and STUDENT roles.
     *
     * @param id The ID of the evaluation to retrieve.
     * @param request The HTTP request.
     * @return The evaluation with the specified ID.
     */
    @GET
    @Path("{id}")
    @RolesAllowed({"ADMIN", "TEACHER", "STUDENT"})
    @Operation(summary = "Get an evaluation by ID", description = "Returns an evaluation by ID.")
    public Response getEvaluationById(@PathParam("id") int id, @Context HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        Response validationResponse = jwtService.validateToken(authorizationHeader);
        if (validationResponse.getStatus() != Response.Status.OK.getStatusCode()) {
            // Return the validation response if the token is invalid
            return validationResponse;
        }

        Claims claims = (Claims) validationResponse.getEntity();
        String username = jwtService.extractUsername(claims);
        Set<String> roles = jwtService.extractRoles(claims);

        LOGGER.info("User " + username + " with roles " + roles + " is fetching evaluation with ID: " + id);

        Response evaluationResponse = getEvaluationByUsernameAndRoles(username, roles, (long) id);
        if (evaluationResponse.getStatus() != Response.Status.OK.getStatusCode()) {
            return evaluationResponse;
        }

        Evaluation evaluation = (Evaluation) evaluationResponse.getEntity();
        // Map Evaluation entity to DTOs based on user role

        if (roles.contains("ADMIN")) {
            EvaluationAdminDTO evaluationDTO = evaluationMapper.toAdminDTO(evaluation);
            return Response.ok(evaluationDTO).build();
        } else if (roles.contains("TEACHER")) {
            // Teacher can access only their own evaluations
            EvaluationTeacherDTO evaluationDTO = evaluationMapper.toTeacherDTO(evaluation);
            return Response.ok(evaluationDTO).build();
        } else if (roles.contains("STUDENT")) {
            // Student can access only their own evaluations
            EvaluationStudentResponseDTO evaluationDTO = evaluationMapper.toStudentResponseDTO(evaluation);
            return Response.ok(evaluationDTO).build();
        }

        return Response.status(Response.Status.FORBIDDEN).entity("You are not authorized to access this evaluation.").build();
    }

    /**
     * Gets evaluations by teacher. Accessible only by TEACHER role.
     *
     * @param request The HTTP request.
     * @return A list of evaluations for the specified teacher.
     */
    @GET
    @Path("teacher")
    @RolesAllowed({"TEACHER"})
    @Operation(summary = "Get evaluations by teacher", description = "Returns a list of evaluations for a specific teacher.")
    public Response getEvaluationsByTeacher(@Context HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        Response validationResponse = jwtService.validateToken(authorizationHeader);
        if (validationResponse.getStatus() != Response.Status.OK.getStatusCode()) {
            // Return the validation response if the token is invalid
            return validationResponse;
        }

        Claims claims = (Claims) validationResponse.getEntity();
        String username = jwtService.extractUsername(claims);
        Set<String> roles = jwtService.extractRoles(claims);

        LOGGER.info("User " + username + " with roles " + roles + " is fetching evaluations for teacher");

        User teacher = userService.findByUsername(username).orElse(null);
        if (teacher == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Teacher not found.").build();
        }
        Long teacherId = teacher.getId();

        if (roles.contains("ADMIN")) {
            // Admin can access any teacher's evaluations
            List<Evaluation> evaluations = evaluationService.findByTeacherId(teacherId);
            List<EvaluationAdminDTO> evaluationsDTO = evaluationMapper.toAdminDTOs(evaluations);
            return responseForEvaluationList(evaluationsDTO);
        } else if (roles.contains("TEACHER")) {
            // Teacher can access only their own evaluations
            if (teacher.getUsername().equals(username)) {
                List<Evaluation> evaluations = evaluationService.findByTeacherId(teacherId);
                List<EvaluationTeacherDTO> evaluationsDTO = evaluationMapper.toTeacherDTOs(evaluations);
                return responseForEvaluationList(evaluationsDTO);
            }
        }

        return Response.status(Response.Status.FORBIDDEN).entity("You are not authorized to access evaluations for this teacher.").build();
    }

    /**
     * Gets evaluations by student. Accessible only by STUDENT role.
     *
     * @param request The HTTP request.
     * @return A list of evaluations for the specified student.
     */
    @GET
    @Path("student")
    @RolesAllowed("STUDENT")
    @Operation(summary = "Get evaluations by student", description = "Returns a list of evaluations for a specific student.")
    public Response getEvaluationsByStudentId(@Context HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        Response validationResponse = jwtService.validateToken(authorizationHeader);
        if (validationResponse.getStatus() != Response.Status.OK.getStatusCode()) {
            // Return the validation response if the token is invalid
            return validationResponse;
        }

        Claims claims = (Claims) validationResponse.getEntity();
        String username = jwtService.extractUsername(claims);
        Set<String> roles = jwtService.extractRoles(claims);

        LOGGER.info("User " + username + " with roles " + roles + " is fetching evaluations for student");

        User student = userService.findByUsername(username).orElse(null);
        if (student == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Student not found.").build();
        }
        Long studentId = student.getId();

        if (roles.contains("ADMIN")) {
            // Admin can access any student's evaluations
            List<Evaluation> evaluations = evaluationService.findByStudentId(studentId);
            List<EvaluationAdminDTO> evaluationsDTO = evaluationMapper.toAdminDTOs(evaluations);
            return responseForEvaluationList(evaluationsDTO);
        } else if (roles.contains("STUDENT")) {
            // Student can access only their own evaluations
            if (student.getUsername().equals(username)) {
                List<Evaluation> evaluations = evaluationService.findByStudentId(studentId);
                List<EvaluationStudentResponseDTO> evaluationsDTO = evaluationMapper.toStudentResponseDTOs(evaluations);
                return responseForEvaluationList(evaluationsDTO);
            }
        }

        return Response.status(Response.Status.FORBIDDEN).entity("You are not authorized to access evaluations for this student.").build();
    }

    /**
     * Creates a new evaluation. Accessible only by STUDENT role.
     *
     * @param evaluationDTO The evaluation data.
     * @param request The HTTP request.
     * @return Response indicating the creation status.
     */
    @POST
    @RolesAllowed({"STUDENT"})
    @Operation(summary = "Create a new evaluation", description = "Creates a new evaluation to the database.")
    public Response createEvaluation(@Valid EvaluationStudentRequestDTO evaluationDTO, @Context HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        Response validationResponse = jwtService.validateToken(authorizationHeader);
        if (validationResponse.getStatus() != Response.Status.OK.getStatusCode()) {
            // Return the validation response if the token is invalid
            return validationResponse;
        }

        Claims claims = (Claims) validationResponse.getEntity();
        String username = jwtService.extractUsername(claims);
        Set<String> roles = jwtService.extractRoles(claims);

        LOGGER.info("User " + username + " with roles " + roles + " is creating a new evaluation.");

        User student = userService.findByUsername(username).orElse(null);
        if (student == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Student not found.").build();
        }
        Evaluation evaluation = evaluationMapper.studentRequestDTOtoEntity(evaluationDTO, student);

        try {
            Evaluation newEvaluation = evaluationService.save(evaluation);

            // Map the new evaluation to StudentResponseDTO
            EvaluationStudentResponseDTO evaluationResponseDTO = evaluationMapper.toStudentResponseDTO(newEvaluation);
            return Response.ok(evaluationResponseDTO).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    /**
     * Updates an existing evaluation. Accessible only by STUDENT role.
     *
     * @param id The ID of the evaluation to update.
     * @param evaluationDTO The updated evaluation data.
     * @param request The HTTP request.
     * @return Response indicating the update status.
     */
    @PUT
    @Path("{id}")
    @RolesAllowed({"STUDENT"})
    @Operation(summary = "Update an evaluation", description = "Updates an existing evaluation in the database.")
    public Response updateEvaluation(@PathParam("id") Long id, @Valid EvaluationStudentRequestDTO evaluationDTO, @Context HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        Response validationResponse = jwtService.validateToken(authorizationHeader);
        if (validationResponse.getStatus() != Response.Status.OK.getStatusCode()) {
            // Return the validation response if the token is invalid
            return validationResponse;
        }

        Claims claims = (Claims) validationResponse.getEntity();
        String username = jwtService.extractUsername(claims);
        Set<String> roles = jwtService.extractRoles(claims);

        LOGGER.info("User " + username + " with roles " + roles + " is updating evaluation with ID: " + id);

        User student = userService.findByUsername(username).orElse(null);
        if (student == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Student not found.").build();
        }
        Evaluation evaluation = evaluationMapper.studentRequestDTOtoEntity(evaluationDTO, student);
        evaluation.setId(id);
        evaluation.setTimestamp(LocalDateTime.now());

        Response evaluationResponse = getEvaluationByUsernameAndRoles(username, roles, id);
        if (evaluationResponse.getStatus() != Response.Status.OK.getStatusCode()) {
            return evaluationResponse;
        }

        // We don't need the evaluation entity from the response, just the status
        try {
            Optional<Evaluation> updatedEvaluation = evaluationService.update(evaluation);
            if (updatedEvaluation.isPresent()) {
                // Map the updated evaluation to StudentResponseDTO
                EvaluationStudentResponseDTO evaluationResponseDTO = evaluationMapper.toStudentResponseDTO(updatedEvaluation.get());
                return Response.ok(evaluationResponseDTO).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).entity("Evaluation not found.").build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    /**
     * Deletes an evaluation by ID. Accessible only by ADMIN and STUDENT roles.
     *
     * @param id The ID of the evaluation to delete.
     * @param request The HTTP request.
     * @return Response indicating the deletion status.
     */
    @DELETE
    @Path("{id}")
    @RolesAllowed({"ADMIN", "STUDENT"})
    @Operation(summary = "Delete an evaluation", description = "Deletes an existing evaluation by ID.")
    public Response deleteEvaluation(@PathParam("id") Long id, @Context HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        Response validationResponse = jwtService.validateToken(authorizationHeader);
        if (validationResponse.getStatus() != Response.Status.OK.getStatusCode()) {
            // Return the validation response if the token is invalid
            return validationResponse;
        }

        Claims claims = (Claims) validationResponse.getEntity();
        String username = jwtService.extractUsername(claims);
        Set<String> roles = jwtService.extractRoles(claims);

        LOGGER.info("User " + username + " with roles " + roles + " is deleting evaluation with ID: " + id);

        Response evaluationResponse = getEvaluationByUsernameAndRoles(username, roles, id);
        if (evaluationResponse.getStatus() != Response.Status.OK.getStatusCode()) {
            return evaluationResponse;
        }

        // We don't need the evaluation entity from the response, just the status
        try {
            evaluationService.deleteById(id);
            return Response.ok("Evaluation deleted successfully.").build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    /**
     * Gets the total number of evaluations. Accessible only by ADMIN.
     *
     * @return The total number of evaluations.
     */
    @GET
    @Path("count")
    @RolesAllowed("ADMIN")
    @Operation(summary = "Get total number of evaluations", description = "Returns the total number of evaluations.")
    public Response countEvaluations() {
        try {
            LOGGER.info("Getting total number of evaluations");

            long count = evaluationService.countEvaluations();
            return Response.ok(count).build();
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

    /**
     * Gets the average grade of all evaluations. Accessible only by ADMIN.
     *
     * @return The average grade of all evaluations.
     */
    @GET
    @Path("average-grade")
    @RolesAllowed("ADMIN")
    @Operation(summary = "Get average grade", description = "Returns the average grade of all evaluations.")
    public Response getAverageGrade() {
        try {
            LOGGER.info("Getting average grade of all evaluations");

            double averageGrade = evaluationService.averageGrade();
            return Response.ok(averageGrade).build();
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

    /**
     * Gets the average grade of all evaluations per teacher. Accessible only by ADMIN.
     *
     * @return A map of teacher usernames and their average grades.
     */
    @GET
    @Path("average-grade-map")
    @RolesAllowed("ADMIN")
    @Operation(summary = "Get average grade per teacher", description = "Returns the average grade of all evaluations per teacher.")
    public Response getAverageGradePerTeacher() {
        try {
            LOGGER.info("Getting average grade per teacher");

            return Response.ok(evaluationService.averageGradePerTeacher()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

    private Response getEvaluationByUsernameAndRoles(String username, Set<String> roles, Long id) {
        Evaluation evaluation = evaluationService.findById(id).orElse(null);
        if (evaluation == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Evaluation not found.").build();
        }

        if (roles.contains("ADMIN")) {
            // Admin can access any evaluation
            return Response.ok(evaluation).build();
        } else if (roles.contains("TEACHER")) {
            // Teacher can access evaluations of their students
            if (evaluation.getTeacher().getUsername().equals(username)) {
                return Response.ok(evaluation).build();
            }
        } else if (roles.contains("STUDENT")) {
            // Student can access only their own evaluations
            if (evaluation.getStudent().getUsername().equals(username)) {
                return Response.ok(evaluation).build();
            }
        }

        return Response.status(Response.Status.FORBIDDEN).entity("You are not authorized to access this evaluation.").build();
    }

    private Response responseForEvaluationList(List<?> evaluations) {
        if (evaluations.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).entity("No evaluations found.").build();
        }
        return Response.ok(evaluations).build();
    }
}
