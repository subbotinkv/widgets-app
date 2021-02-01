package ru.subbotinkv.widgets.repository;

import ru.subbotinkv.widgets.model.Widget;

import java.util.Collection;
import java.util.Optional;

public interface IWidgetRepository {
    Widget save(Widget widget);

    Collection<Widget> findAllOrderByZetaIndexAsc();

    Optional<Widget> findById(Long id);

    Collection<Widget> saveAll(Collection<Widget> widgets);

    boolean deleteWidget(Long id);

    Optional<Widget> findByZIndex(Integer zIndex);

    Optional<Integer> getMaxZIndex();
}
