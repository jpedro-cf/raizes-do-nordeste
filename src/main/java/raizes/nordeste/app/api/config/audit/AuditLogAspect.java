package raizes.nordeste.app.api.config.audit;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import raizes.nordeste.app.api.config.security.BearerTokenAuthentication;
import raizes.nordeste.app.domain.entities.Log;
import raizes.nordeste.app.infra.repositories.LogsRepository;

import java.time.Instant;
import java.util.Arrays;

@Aspect
@Component
@RequiredArgsConstructor
public class AuditLogAspect {

    private final LogsRepository logsRepository;
    private final HttpServletRequest httpRequest;
    private final ObjectMapper objectMapper;

    @Around("@annotation(auditLog)")
    public Object log(ProceedingJoinPoint joinPoint, AuditLog auditLog) throws Throwable {
        var auth = (BearerTokenAuthentication) SecurityContextHolder
                .getContext()
                .getAuthentication();

        Long userId = null;
        if (auth != null && auth.getUser() != null) {
            userId = auth.getUser().getId();
        }

        Object result = null;
        try {
            result = joinPoint.proceed();
            return result;
        } finally {
            var entry = Log.builder()
                    .userId(userId)
                    .endpoint(httpRequest.getMethod() + " " + httpRequest.getRequestURI())
                    .requestArgs(serializeArgs(joinPoint.getArgs()))
                    .timestamp(Instant.now())
                    .build();

            logsRepository.save(entry);
        }
    }

    private String serializeArgs(Object[] args) {
        if (args == null || args.length == 0) return "N/A";
        try {
            var dtos = Arrays.stream(args)
                    .filter(arg -> arg != null
                            && !(arg instanceof Long)
                            && !(arg instanceof String)
                            && !(arg instanceof Integer)
                            && !(arg instanceof Pageable))
                    .toList();

            if (dtos.isEmpty()) return "N/A";
            return objectMapper.writeValueAsString(dtos.size() == 1 ? dtos.get(0) : dtos);
        } catch (Exception e) {
            return "N/A";
        }
    }
}