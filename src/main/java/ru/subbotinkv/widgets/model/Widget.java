package ru.subbotinkv.widgets.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Widget {

    public Widget(Widget widget) {
        this.id = widget.id;
        this.xCoordinate = widget.xCoordinate;
        this.yCoordinate = widget.yCoordinate;
        this.zetaIndex = widget.zetaIndex;
        this.width = widget.width;
        this.height = widget.height;
        this.lastModifiedDate = widget.lastModifiedDate;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Integer xCoordinate;

    @NotNull
    private Integer yCoordinate;

    @NotNull
    private Integer zetaIndex;

    @NotNull
    @Positive
    private Integer width;

    @NotNull
    @Positive
    private Integer height;

    @NotNull
    @LastModifiedDate
    private LocalDateTime lastModifiedDate;
}
