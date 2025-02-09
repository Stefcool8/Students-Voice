//package org.example.studentsvoice.filter;
//
//import jakarta.annotation.Priority;
//import jakarta.enterprise.context.ApplicationScoped;
//import jakarta.ws.rs.container.*;
//import jakarta.ws.rs.ext.Provider;
//import jakarta.ws.rs.core.Response;
//import org.example.studentsvoice.entity.Evaluation;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.logging.Logger;
//import java.util.stream.Collectors;
//
//@Provider
//@Priority(1)
//@ApplicationScoped
//public class EvaluationCacheFilter implements ContainerRequestFilter, ContainerResponseFilter {
//
//    private final Map<String, Object> cache = new HashMap<>();
//    private static final String ALL_EVALUATIONS_KEY = "all_evaluations";
//    private final Logger LOGGER = Logger.getLogger(EvaluationCacheFilter.class.getName());
//
//    @Override
//    public void filter(ContainerRequestContext requestContext) {
//        String method = requestContext.getMethod();
//        String path = requestContext.getUriInfo().getPath();
//
//        if (method.equalsIgnoreCase("GET")) {
//            // Check for cached responses
//            if (path.equals("evaluations")) {
//                LOGGER.info("Returning cached response for all evaluations.");
//                List<Evaluation> cachedEvaluations = getCachedAllEvaluations();
//                if (cachedEvaluations != null) {
//                    respondWithCache(requestContext, getCachedAllEvaluations());
//                }
//            } else if (path.startsWith("evaluations/teacher/")) {
//                String teacherId = requestContext.getUriInfo().getPathParameters().getFirst("teacherId");
//                LOGGER.info("Returning cached response for evaluations by teacher ID: " + teacherId);
//
//                List<Evaluation> cachedEvaluations = getCachedEvaluationsByTeacher(teacherId);
//                if (cachedEvaluations != null) {
//                    respondWithCache(requestContext, cachedEvaluations);
//                }
//            } else if (path.matches("evaluations/\\d+")) {
//                String id = requestContext.getUriInfo().getPathParameters().getFirst("id");
//                LOGGER.info("Returning cached response for evaluation ID: " + id);
//
//                Evaluation cachedEvaluation = getCachedEvaluationById(Long.parseLong(id));
//                if (cachedEvaluation != null) {
//                    respondWithCache(requestContext, cachedEvaluation);
//                }
//            }
//        }
//    }
//
//    @Override
//    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) {
//        String method = requestContext.getMethod();
//        String path = requestContext.getUriInfo().getPath();
//
//        if (method.equalsIgnoreCase("GET")) {
//            if (path.equals("evaluations")) {
//                cacheAllEvaluations(responseContext);
//            } else if (path.startsWith("evaluations/teacher/")) {
//                cacheEvaluationsByTeacher(requestContext, responseContext);
//            } else if (path.matches("evaluations/\\d+")) {
//                cacheEvaluationById(requestContext, responseContext);
//            }
//        } else if (isModificationMethod(method)) {
//            LOGGER.info("Cache invalidated due to modification operation.");
//            cache.clear();
//        }
//    }
//
//    private void respondWithCache(ContainerRequestContext requestContext, Object cachedEntity) {
//        Response response = Response.ok(cachedEntity).build();
//        requestContext.abortWith(response); // Stops further processing and sends the cached response
//    }
//
//    private void cacheAllEvaluations(ContainerResponseContext responseContext) {
//        if (cache.containsKey(ALL_EVALUATIONS_KEY)) {
//            LOGGER.info("All evaluations cached already.");
//            return;
//        }
//
//        Object responseEntity = responseContext.getEntity();
//        if (responseEntity instanceof List<?>) {
//            try {
//                @SuppressWarnings("unchecked")
//                List<Evaluation> evaluations = (List<Evaluation>) responseEntity;
//                cache.clear(); // Clear all previous cache entries
//                cache.put(ALL_EVALUATIONS_KEY, evaluations);
//                LOGGER.info("Cached all evaluations and cleared specific caches.");
//            } catch (ClassCastException e) {
//                LOGGER.warning("Failed to cast response entity to List<Evaluation>");
//            }
//        } else {
//            LOGGER.warning("Response entity is not a List");
//        }
//    }
//
//    private void cacheEvaluationsByTeacher(ContainerRequestContext requestContext, ContainerResponseContext responseContext) {
//        if (cache.containsKey(ALL_EVALUATIONS_KEY)) {
//            LOGGER.info("All evaluations cached already.");
//        } else if (cache.containsKey("teacher_" + requestContext.getUriInfo().getPathParameters().getFirst("teacherId"))) {
//            LOGGER.info("Evaluations for specific teacher cached already.");
//        } else {
//            LOGGER.info("Caching evaluations for specific teacher directly from response.");
//            Object responseEntity = responseContext.getEntity();
//
//            if (responseEntity instanceof List<?>) {
//                try {
//                    @SuppressWarnings("unchecked")
//                    List<Evaluation> evaluations = (List<Evaluation>) responseEntity;
//                    String teacherId = requestContext.getUriInfo().getPathParameters().getFirst("teacherId");
//                    cache.put("teacher_" + teacherId, evaluations);
//                    LOGGER.info("Cached evaluations for teacher ID: " + teacherId);
//                } catch (ClassCastException e) {
//                    LOGGER.warning("Failed to cast response entity to List<Evaluation>");
//                }
//            } else {
//                LOGGER.warning("Response entity is not a List");
//            }
//        }
//    }
//
//    private void cacheEvaluationById(ContainerRequestContext requestContext, ContainerResponseContext responseContext) {
//        String id = requestContext.getUriInfo().getPathParameters().getFirst("id");
//
//        if (cache.containsKey(ALL_EVALUATIONS_KEY)) {
//            LOGGER.info("All evaluations cached already.");
//        } else if (cache.containsKey("evaluation_" + id)) {
//            LOGGER.info("Evaluation with ID: " + id + " cached already.");
//        } else {
//            LOGGER.info("Caching single evaluation directly from response.");
//            Evaluation evaluation = (Evaluation) responseContext.getEntity();
//            if (evaluation != null && id != null) {
//                cache.put("evaluation_" + id, evaluation);
//                LOGGER.info("Cached evaluation with ID: " + id);
//            }
//        }
//    }
//
//    private boolean isModificationMethod(String method) {
//        return method.equalsIgnoreCase("POST") ||
//                method.equalsIgnoreCase("PUT") ||
//                method.equalsIgnoreCase("DELETE");
//    }
//
//    public List<Evaluation> getCachedAllEvaluations() {
//        LOGGER.info("Getting cached all evaluations");
//
//        Object obj = cache.get(ALL_EVALUATIONS_KEY);
//        if (obj instanceof List<?>) {
//            try {
//                @SuppressWarnings("unchecked")
//                List<Evaluation> evaluations = (List<Evaluation>) obj;
//                return evaluations;
//            } catch (ClassCastException e) {
//                LOGGER.warning("Failed to cast cached object to List<Evaluation>");
//            }
//        } else {
//            LOGGER.warning("No cached all evaluations found");
//        }
//        return null;
//    }
//
//    public List<Evaluation> getCachedEvaluationsByTeacher(String teacherId) {
//        if (cache.containsKey(ALL_EVALUATIONS_KEY)) {
//            LOGGER.info("Getting cached evaluations for teacher ID: " + teacherId + " from all evaluations");
//            List<Evaluation> allEvaluations = getCachedAllEvaluations();
//            if (allEvaluations != null && teacherId != null) {
//                return allEvaluations.stream()
//                        .filter(e -> e.getTeacher().getId().toString().equals(teacherId))
//                        .collect(Collectors.toList());
//            }
//        }
//
//        LOGGER.info("Getting cached evaluations for teacher ID: " + teacherId);
//        Object obj = cache.get("teacher_" + teacherId);
//        if (obj instanceof List<?>) {
//            try {
//                @SuppressWarnings("unchecked")
//                List<Evaluation> evaluations = (List<Evaluation>) obj;
//                return evaluations;
//            } catch (ClassCastException e) {
//                LOGGER.warning("Failed to cast cached object to List<Evaluation>");
//            }
//        } else {
//            LOGGER.warning("No cached evaluations found for teacher ID: " + teacherId);
//        }
//        return null;
//    }
//
//    public Evaluation getCachedEvaluationById(Long id) {
//        if (cache.containsKey(ALL_EVALUATIONS_KEY)) {
//            LOGGER.info("Getting cached evaluation with ID: " + id + " from all evaluations");
//            List<Evaluation> allEvaluations = getCachedAllEvaluations();
//            if (allEvaluations != null) {
//                return allEvaluations.stream()
//                        .filter(e -> e.getId().equals(id))
//                        .findFirst().orElse(null);
//            }
//        }
//
//        LOGGER.info("Getting cached evaluation with ID: " + id);
//        return (Evaluation) cache.get("evaluation_" + id);
//    }
//}
