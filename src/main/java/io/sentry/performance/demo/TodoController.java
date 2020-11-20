package io.sentry.performance.demo;

import io.sentry.IHub;
import io.sentry.ISpan;
import io.sentry.SentryTransaction;
import io.sentry.spring.tracing.SentrySpan;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/todo")
public class TodoController {
    private final TodoService todoService;
    private final IHub iHub;

    public TodoController(TodoService todoService, IHub iHub) {
        this.todoService = todoService;
        this.iHub = iHub;
    }

    @GetMapping("{id}")
    @SentrySpan
    Todo get(@PathVariable Long id) {
        return todoService.findById(id);
    }

    @GetMapping
    @SentrySpan
    List<Todo> findAll() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        requestAttributes.setAttribute("id", UUID.randomUUID().toString(), 1);
        iHub.configureScope(scope -> {
            ISpan span = scope.getSpan();
            span.setTag("request_id", (String) requestAttributes.getAttribute("id", 1));
            SentryTransaction transaction = scope.getTransaction();
            transaction.setTag("request_id", (String) requestAttributes.getAttribute("id", 1));
        });
        return todoService.findAll();
    }
}
