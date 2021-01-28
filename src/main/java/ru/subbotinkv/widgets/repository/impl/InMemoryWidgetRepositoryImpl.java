package ru.subbotinkv.widgets.repository.impl;

import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;
import ru.subbotinkv.widgets.model.Widget;
import ru.subbotinkv.widgets.repository.IWidgetRepository;

import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class InMemoryWidgetRepositoryImpl implements IWidgetRepository {

    private final AtomicLong idSequenceGenerator = new AtomicLong(1);
    private final Map<Long, Widget> widgetStorage = new HashMap<>();

    @Override
    public Widget save(Widget widget) {
        Assert.notNull(widget, "Widget must not be null");

        Long widgetId = widget.getId();
        if (widgetId == null) {
            widgetId = idSequenceGenerator.getAndIncrement();

            widget.setId(widgetId);
            widget.setLastModifiedDate(ZonedDateTime.now());

            widgetStorage.put(widgetId, widget);
        } else {

            widget.setLastModifiedDate(ZonedDateTime.now());
            widgetStorage.replace(widgetId, widget);
        }

        return widget;
    }

    @Override
    public Collection<Widget> findAll() {
        return widgetStorage.values();
    }

    @Override
    public Optional<Widget> findById(Long id) {
        Assert.notNull(id, "Id must not be null");

        Widget widget = widgetStorage.get(id);
        return widget != null ? Optional.of(widget) : Optional.empty();
    }

    @Override
    public Collection<Widget> saveAll(Collection<Widget> widgets) {
        Assert.notNull(widgets, "Widgets must not be null");

        for (Widget widget : widgets) {
            save(widget);
        }

        return widgets;
    }

    @Override
    public boolean deleteWidget(Long id) {
        Assert.notNull(id, "Id must not be null");

        Widget widget = widgetStorage.remove(id);

        return widget != null;
    }

    @Override
    public Optional<Widget> findByZIndex(Integer zIndex) {
        Assert.notNull(zIndex, "Z-index must not be null");

        return widgetStorage.values().stream().filter(widget -> widget.getZIndex().intValue() == zIndex.intValue()).findAny();
    }

    @Override
    public Optional<Integer> getMaxZIndex() {
        return widgetStorage.values().stream().map(Widget::getZIndex).max(Comparator.naturalOrder());
    }
}
