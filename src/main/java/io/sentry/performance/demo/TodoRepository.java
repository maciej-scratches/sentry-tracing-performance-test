package io.sentry.performance.demo;

import io.sentry.IHub;
import io.sentry.ISpan;
import io.sentry.spring.tracing.SentrySpan;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.List;

@Repository
public class TodoRepository {
    private final RestTemplate restTemplate;
    private final IHub iHub;

    public TodoRepository(RestTemplate restTemplate, IHub iHub) {
        this.restTemplate = restTemplate;
        this.iHub = iHub;
    }

    @SentrySpan
    public List<Todo> findAll() {
        try {
            Thread.sleep(20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        iHub.configureScope(scope -> {
            ISpan span = scope.getSpan();
            span.setTag("request_id", (String) RequestContextHolder.getRequestAttributes().getAttribute("id", 1));
        });
        return List.of(
                new Todo(1L, "todo 1", false),
                new Todo(2L, "todo 2", false),
                new Todo(3L, "todo 3", true),
                new Todo(4L, "todo 4", false),
                new Todo(5L, "todo 5", true)
        );
    }
}
