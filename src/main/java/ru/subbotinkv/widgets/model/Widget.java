package ru.subbotinkv.widgets.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.ZonedDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Widget {

    public Widget(Widget widget) {
        this.id = widget.id;
        this.xCoordinate = widget.xCoordinate;
        this.yCoordinate = widget.yCoordinate;
        this.zIndex = widget.zIndex;
        this.width = widget.width;
        this.height = widget.height;
        this.lastModifiedDate = widget.lastModifiedDate;
    }

    private Long id;

    @NotNull
    private Integer xCoordinate;

    @NotNull
    private Integer yCoordinate;

    @NotNull
    private Integer zIndex;

    @NotNull
    @Positive
    private Integer width;

    @NotNull
    @Positive
    private Integer height;

    private ZonedDateTime lastModifiedDate;
}
