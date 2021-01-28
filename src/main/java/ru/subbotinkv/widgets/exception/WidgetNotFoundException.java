package ru.subbotinkv.widgets.exception;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import java.net.URI;

public class WidgetNotFoundException extends AbstractThrowableProblem {

    private static final URI TYPE = URI.create("https://example.org/not-found");

    public WidgetNotFoundException(Long id) {
        super(TYPE,
                "Not found",
                Status.NOT_FOUND,
                String.format("Widget %d not found", id));
    }
}
