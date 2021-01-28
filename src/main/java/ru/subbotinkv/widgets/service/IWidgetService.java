package ru.subbotinkv.widgets.service;

import ru.subbotinkv.widgets.dto.WidgetDto;

import java.util.Collection;
import java.util.Optional;

public interface IWidgetService {
    WidgetDto createWidget(WidgetDto widgetDto);

    Collection<WidgetDto> getAllWidgets();

    Optional<WidgetDto> getWidgetById(Long id);

    boolean deleteWidget(Long id);

    WidgetDto updateWidget(WidgetDto widgetDto);
}
