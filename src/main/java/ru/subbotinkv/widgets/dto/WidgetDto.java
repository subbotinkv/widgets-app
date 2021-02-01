package ru.subbotinkv.widgets.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@ApiModel("Widget")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WidgetDto {

    @ApiModelProperty(value = "Widget unique identifier", example = "1", position = 1)
    @Null
    private Long id;

    @ApiModelProperty(value = "X coordinate", example = "0", required = true, position = 2)
    @JsonProperty("x_coordinate")
    @NotNull
    private Integer xCoordinate;

    @ApiModelProperty(value = "Y coordinate", example = "0", required = true, position = 3)
    @JsonProperty("y_coordinate")
    @NotNull
    private Integer yCoordinate;

    @ApiModelProperty(value = "Z-index", example = "0", required = true, position = 4)
    @JsonProperty("z_index")
    private Integer zetaIndex;

    @ApiModelProperty(value = "Width", example = "100", required = true, position = 5)
    @NotNull
    @Positive
    private Integer width;

    @ApiModelProperty(value = "Height", example = "100", required = true, position = 6)
    @NotNull
    @Positive
    private Integer height;

    @ApiModelProperty(value = "Last modification date", position = 7)
    @Null
    private LocalDateTime lastModifiedDate;
}
