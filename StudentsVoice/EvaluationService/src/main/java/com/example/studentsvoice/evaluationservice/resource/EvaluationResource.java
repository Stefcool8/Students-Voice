package com.example.studentsvoice.evaluationservice.resource;

import com.example.studentsvoice.core.command.CreateEvaluationCommand;
import com.example.studentsvoice.core.dto.Evaluation.EvaluationStudentRequestDTO;
import com.example.studentsvoice.evaluationservice.cdi.producer.MyLogger;
import com.example.studentsvoice.evaluationservice.cdi.producer.RegistrationNumber;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.axonframework.commandhandling.gateway.CommandGateway;

import java.util.UUID;
import java.util.logging.Logger;

@Path("evaluations")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EvaluationResource {
    @Inject
    @MyLogger
    private Logger LOGGER;

    @Inject
    @RegistrationNumber
    private Instance<UUID> registrationNumberGenerator;

    @Inject
    private CommandGateway commandGateway;

    @POST
    @Path("{studentUsername}")
    public Response createEvaluation(@Valid EvaluationStudentRequestDTO evaluationDTO, @PathParam("studentUsername") String studentUsername) {
        LOGGER.info("Received evaluation creation request for student: " + studentUsername);

        CreateEvaluationCommand createEvaluationCommand = CreateEvaluationCommand.builder()
                .registrationNumber(registrationNumberGenerator.get())
                .teacherName(evaluationDTO.getTeacherName())
                .studentUsername(studentUsername)
                .activityName(evaluationDTO.getActivityName())
                .activityType(evaluationDTO.getActivityType())
                .grade(evaluationDTO.getGrade())
                .comment(evaluationDTO.getComment())
                .build();

        Object result = commandGateway.sendAndWait(createEvaluationCommand);
        return Response.ok(result).build();
    }
}
