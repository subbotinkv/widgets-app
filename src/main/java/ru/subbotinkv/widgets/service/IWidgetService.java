package ru.subbotinkv.widgets.service;

import org.springframework.data.domain.Page;
import ru.subbotinkv.widgets.dto.WidgetDto;

import java.util.Optional;

public interface IWidgetService {
    WidgetDto createWidget(WidgetDto widgetDto);

    Page<WidgetDto> getAllWidgets(Integer page, Integer size);

    Optional<WidgetDto> getWidgetById(Long id);

    boolean deleteWidget(Long id);

    WidgetDto updateWidget(WidgetDto widgetDto);
}
