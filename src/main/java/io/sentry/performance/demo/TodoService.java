package io.sentry.performance.demo;

import io.sentry.IHub;
import io.sentry.ISpan;
import io.sentry.spring.tracing.SentrySpan;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.List;

@Service
public class TodoService {
    private final TodoRepository todoRepository;
    private final IHub iHub;

    public TodoService(TodoRepository todoRepository, IHub iHub) {
        this.todoRepository = todoRepository;
        this.iHub = iHub;
    }

    @SentrySpan
    public Todo findById(Long id) {
        try {
            Thread.sleep(30);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("Failed to load todo with id = " + id);
    }

    @SentrySpan
    public List<Todo> findAll() {
        iHub.configureScope(scope -> {
            ISpan span = scope.getSpan();
            span.setTag("request_id", (String) RequestContextHolder.getRequestAttributes().getAttribute("id", 1));
        });
        Sleep.sleep(10);
        List<Todo> all = todoRepository.findAll();
        Sleep.sleep(20);
        return all;
    }
}
