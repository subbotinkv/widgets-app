package ru.subbotinkv.widgets.repository.impl;

import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;
import ru.subbotinkv.widgets.model.Widget;
import ru.subbotinkv.widgets.repository.IWidgetRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Profile("map")
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
            widget.setLastModifiedDate(LocalDateTime.now());

            widgetStorage.put(widgetId, widget);
        } else {
            widget.setLastModifiedDate(LocalDateTime.now());
            widgetStorage.replace(widgetId, widget);
        }

        return widget;
    }

    @Override
    public Page<Widget> findAllOrderByZetaIndexAsc(Pageable pageable) {
        Assert.notNull(pageable, "Paging information must not be null");

        List<Widget> widgets = widgetStorage.values().stream()
                .sorted(Comparator.comparingInt(Widget::getZetaIndex))
                .skip(pageable.getOffset())
                .limit(pageable.getPageSize())
                .map(Widget::new)
                .collect(Collectors.toList());

        return new PageImpl<>(widgets, pageable, widgetStorage.size());
    }

    @Override
    public Optional<Widget> findById(Long id) {
        Assert.notNull(id, "Id must not be null");

        Widget widget = widgetStorage.get(id);
        return widget != null ? Optional.of(new Widget(widget)) : Optional.empty();
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

        return widgetStorage.values().stream().filter(widget -> widget.getZetaIndex().intValue() == zIndex.intValue()).map(Widget::new).findAny();
    }

    @Override
    public Optional<Integer> getMaxZIndex() {
        return widgetStorage.values().stream().map(Widget::getZetaIndex).max(Comparator.naturalOrder());
    }
}
