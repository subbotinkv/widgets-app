package ru.subbotinkv.widgets.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;
import ru.subbotinkv.widgets.model.Widget;
import ru.subbotinkv.widgets.repository.IDatabaseWidgetRepository;
import ru.subbotinkv.widgets.repository.IWidgetRepository;

import java.util.Collection;
import java.util.Optional;

@Profile("h2")
@Repository
@RequiredArgsConstructor
public class DatabaseWidgetRepositoryImpl implements IWidgetRepository {

    private final IDatabaseWidgetRepository widgetRepository;

    @Override
    public Widget save(Widget widget) {
        Assert.notNull(widget, "Widget must not be null");

        return widgetRepository.save(widget);
    }

    @Override
    public Collection<Widget> findAll() {
        return widgetRepository.findAll();
    }

    @Override
    public Optional<Widget> findById(Long id) {
        Assert.notNull(id, "Id must not be null");

        return widgetRepository.findById(id);
    }

    @Override
    public Collection<Widget> saveAll(Collection<Widget> widgets) {
        Assert.notNull(widgets, "Widgets must not be null");

        return widgetRepository.saveAll(widgets);
    }

    @Override
    public boolean deleteWidget(Long id) {
        Assert.notNull(id, "Id must not be null");

        boolean deleted = widgetRepository.existsById(id);
        widgetRepository.deleteById(id);
        return deleted;
    }

    @Override
    public Optional<Widget> findByZIndex(Integer zIndex) {
        return widgetRepository.findByZetaIndex(zIndex);
    }

    @Override
    public Optional<Integer> getMaxZIndex() {
        return widgetRepository.getMaxZIndex();
    }
}
