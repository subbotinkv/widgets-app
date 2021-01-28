package ru.subbotinkv.widgets.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.zalando.problem.violations.ConstraintViolationProblem;
import ru.subbotinkv.widgets.dto.WidgetDto;
import ru.subbotinkv.widgets.exception.WidgetNotFoundException;
import ru.subbotinkv.widgets.service.IWidgetService;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping(value = "/api/widgets")
@RequiredArgsConstructor
public class WidgetController {

    private final IWidgetService widgetService;

    @ApiOperation("Create a widget")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Widget created"),
            @ApiResponse(code = 400, message = "Incorrect widget", response = ConstraintViolationProblem.class)
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_PROBLEM_JSON_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<WidgetDto> createWidget(@Valid @RequestBody WidgetDto widget) {
        WidgetDto createdWidget = widgetService.createWidget(widget);
        return new ResponseEntity<>(createdWidget, HttpStatus.CREATED);
    }

    @ApiOperation("Get a list of widgets")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Widgets found")
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Collection<WidgetDto>> getAllWidgets() {
        return new ResponseEntity<>(widgetService.getAllWidgets(), HttpStatus.OK);
    }

    @ApiOperation("Get a widget by id")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Widget found"),
            @ApiResponse(code = 404, message = "Widget not found", response = WidgetNotFoundException.class)
    })
    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_PROBLEM_JSON_VALUE})
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<WidgetDto> getWidgetById(@PathVariable Long id) {
        return widgetService.getWidgetById(id).map(ResponseEntity::ok)
                .orElseThrow(() -> new WidgetNotFoundException(id));
    }

    @ApiOperation("Change widget data by id")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Widget changed"),
            @ApiResponse(code = 400, message = "Incorrect widget", response = ConstraintViolationProblem.class)}

    )
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_PROBLEM_JSON_VALUE})
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<WidgetDto> updateWidget(@PathVariable Long id, @Valid @RequestBody WidgetDto widget) {
        widget.setId(id);
        return new ResponseEntity<>(widgetService.updateWidget(widget), HttpStatus.OK);
    }

    @ApiOperation("Delete a widget")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Widget deleted"),
            @ApiResponse(code = 404, message = "Widget not found", response = WidgetNotFoundException.class)
    })
    @DeleteMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_PROBLEM_JSON_VALUE})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteWidget(@PathVariable Long id) {
        boolean deleted = widgetService.deleteWidget(id);
        if (!deleted) {
            throw new WidgetNotFoundException(id);
        }
    }
}
