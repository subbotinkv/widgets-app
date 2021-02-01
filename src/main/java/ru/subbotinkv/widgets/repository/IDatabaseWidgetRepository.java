package ru.subbotinkv.widgets.repository;

import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.subbotinkv.widgets.model.Widget;

import java.util.Collection;
import java.util.Optional;

@Profile("h2")
@Repository
public interface IDatabaseWidgetRepository extends JpaRepository<Widget, Long> {
    Collection<Widget> findAllByOrderByZetaIndexAsc();

    Optional<Widget> findByZetaIndex(Integer zIndex);

    @Query("SELECT MAX(zetaIndex) FROM Widget")
    Optional<Integer> getMaxZIndex();
}
