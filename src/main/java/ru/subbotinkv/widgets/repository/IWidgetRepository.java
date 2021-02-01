package ru.subbotinkv.widgets.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.subbotinkv.widgets.model.Widget;

import java.util.Collection;
import java.util.Optional;

public interface IWidgetRepository {
    Widget save(Widget widget);

    Page<Widget> findAllOrderByZetaIndexAsc(Pageable pageable);

    Optional<Widget> findById(Long id);

    Collection<Widget> saveAll(Collection<Widget> widgets);

    boolean deleteWidget(Long id);

    Optional<Widget> findByZIndex(Integer zIndex);

    Optional<Integer> getMaxZIndex();
}
