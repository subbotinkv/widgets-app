package ru.subbotinkv.widgets.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.subbotinkv.widgets.dto.WidgetDto;
import ru.subbotinkv.widgets.model.Widget;
import ru.subbotinkv.widgets.repository.IWidgetRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
class WidgetServiceTest {

    @InjectMocks
    private WidgetServiceImpl widgetService;

    @Mock
    private IWidgetRepository widgetRepository;

    private final ModelMapper modelMapper = new ModelMapper();

    @BeforeEach
    void init() {
        widgetService = new WidgetServiceImpl(modelMapper, widgetRepository);
    }

    @Test
    void whenCreateWidgetAndWidgetIsNull_thenException() {
        assertThrows(IllegalArgumentException.class, () -> widgetService.createWidget(null));
    }

    @Test
    void whenCreateWidgetAndNoWidgetsAndZIndexIsNull_thenZIndexIsZero() {
        WidgetDto widgetDto = WidgetDto.builder().build();
        Widget widget = Widget.builder().build();

        when(widgetRepository.getMaxZIndex()).thenReturn(Optional.empty());
        when(widgetRepository.save(any())).thenReturn(widget);

        widgetService.createWidget(widgetDto);

        ArgumentCaptor<Widget> widgetArgument = ArgumentCaptor.forClass(Widget.class);
        verify(widgetRepository, times(1)).save(widgetArgument.capture());
        verify(widgetRepository, times(0)).saveAll(any());

        assertEquals(0, widgetArgument.getValue().getZetaIndex());
    }

    @Test
    void whenCreateWidgetAndZIndexIsNull_thenZIndexIsMaximum() {
        WidgetDto widgetDto = WidgetDto.builder().build();
        Widget widget = Widget.builder().build();
        int maxZIndex = 5;

        when(widgetRepository.getMaxZIndex()).thenReturn(Optional.of(maxZIndex));
        when(widgetRepository.save(any())).thenReturn(widget);

        widgetService.createWidget(widgetDto);

        ArgumentCaptor<Widget> widgetArgument = ArgumentCaptor.forClass(Widget.class);
        verify(widgetRepository, times(1)).save(widgetArgument.capture());
        verify(widgetRepository, times(0)).saveAll(any());

        assertEquals(maxZIndex + 1, widgetArgument.getValue().getZetaIndex());
    }

    @Test
    void whenCreateWidgetAndZIndexInTheGap_thenNoShift() {
        WidgetDto widgetDto = WidgetDto.builder().zetaIndex(1).build();
        Widget widget = Widget.builder().build();

        when(widgetRepository.findByZIndex(any())).thenReturn(Optional.empty());
        when(widgetRepository.save(any())).thenReturn(widget);

        widgetService.createWidget(widgetDto);

        ArgumentCaptor<Collection<Widget>> widgetsArgument = ArgumentCaptor.forClass(ArrayList.class);
        verify(widgetRepository, times(0)).getMaxZIndex();
        verify(widgetRepository, times(1)).save(any());
        verify(widgetRepository, times(1)).saveAll(widgetsArgument.capture());

        assertTrue(widgetsArgument.getValue().isEmpty());
    }

    @Test
    void whenCreateWidget_thenShiftTillTheEnd() {
        Widget existingWidget1 = Widget.builder().id(1L).zetaIndex(1).build();
        Widget existingWidget2 = Widget.builder().id(2L).zetaIndex(2).build();
        Widget existingWidget3 = Widget.builder().id(3L).zetaIndex(3).build();
        WidgetDto newWidgetDto = WidgetDto.builder().zetaIndex(2).build();
        Widget newWidget = Widget.builder().id(4L).zetaIndex(2).build();

        when(widgetRepository.findByZIndex(any()))
                .thenReturn(Optional.of(existingWidget2))
                .thenReturn(Optional.of(existingWidget3))
                .thenReturn(Optional.empty());
        when(widgetRepository.save(any())).thenReturn(newWidget);

        widgetService.createWidget(newWidgetDto);

        ArgumentCaptor<Collection<Widget>> widgetsArgument = ArgumentCaptor.forClass(ArrayList.class);
        verify(widgetRepository, times(0)).getMaxZIndex();
        verify(widgetRepository, times(1)).save(any());
        verify(widgetRepository, times(1)).saveAll(widgetsArgument.capture());

        Optional<Widget> widget1 = widgetsArgument.getValue().stream().filter(widget -> widget.getId().longValue() == existingWidget1.getId().longValue()).findAny();
        Optional<Widget> widget2 = widgetsArgument.getValue().stream().filter(widget -> widget.getId().longValue() == existingWidget2.getId().longValue()).findAny();
        Optional<Widget> widget3 = widgetsArgument.getValue().stream().filter(widget -> widget.getId().longValue() == existingWidget3.getId().longValue()).findAny();

        assertTrue(widget1.isEmpty());
        assertTrue(widget2.isPresent() && widget2.get().getZetaIndex() == 3);
        assertTrue(widget3.isPresent() && widget3.get().getZetaIndex() == 4);
    }

    @Test
    void whenCreateWidget_thenShiftTillTheGap() {
        Widget existingWidget1 = Widget.builder().id(1L).zetaIndex(1).build();
        Widget existingWidget2 = Widget.builder().id(2L).zetaIndex(2).build();
        Widget existingWidget3 = Widget.builder().id(3L).zetaIndex(4).build();
        WidgetDto newWidgetDto = WidgetDto.builder().zetaIndex(2).build();
        Widget newWidget = Widget.builder().id(4L).zetaIndex(2).build();

        when(widgetRepository.findByZIndex(any()))
                .thenReturn(Optional.of(existingWidget2))
                .thenReturn(Optional.empty());
        when(widgetRepository.save(any())).thenReturn(newWidget);

        widgetService.createWidget(newWidgetDto);

        ArgumentCaptor<Collection<Widget>> widgetsArgument = ArgumentCaptor.forClass(ArrayList.class);
        verify(widgetRepository, times(0)).getMaxZIndex();
        verify(widgetRepository, times(1)).save(any());
        verify(widgetRepository, times(1)).saveAll(widgetsArgument.capture());

        Optional<Widget> widget1 = widgetsArgument.getValue().stream().filter(widget -> widget.getId().longValue() == existingWidget1.getId().longValue()).findAny();
        Optional<Widget> widget2 = widgetsArgument.getValue().stream().filter(widget -> widget.getId().longValue() == existingWidget2.getId().longValue()).findAny();
        Optional<Widget> widget3 = widgetsArgument.getValue().stream().filter(widget -> widget.getId().longValue() == existingWidget3.getId().longValue()).findAny();

        assertTrue(widget1.isEmpty());
        assertTrue(widget2.isPresent() && widget2.get().getZetaIndex() == 3);
        assertTrue(widget3.isEmpty());
    }

    @Test
    void whenGetAllWidgets_thenFindAllOrderByZetaIndexAscIsCalled() {
        widgetService.getAllWidgets();

        verify(widgetRepository, times(1)).findAllOrderByZetaIndexAsc();
    }

    @Test
    void whenGetWidgetByIdAndIdIsNull_thenException() {
        assertThrows(IllegalArgumentException.class, () -> widgetService.getWidgetById(null));
    }

    @Test
    void whenGetWidgetByIdAndNonExistentId_thenOptionalEmpty() {
        assertTrue(widgetService.getWidgetById(anyLong()).isEmpty());
    }

    @Test
    void whenGetWidgetByIdAndExistingId_thenOptionalWidget() {
        Widget widget = Widget.builder().build();

        when(widgetRepository.findById(anyLong())).thenReturn(Optional.of(widget));

        assertTrue(widgetService.getWidgetById(anyLong()).isPresent());
    }

    @Test
    void whenUpdateWidget_thenShiftTillItself() {
        Widget existingWidget1 = Widget.builder().id(1L).zetaIndex(1).build();
        Widget existingWidget2 = Widget.builder().id(2L).zetaIndex(2).build();
        Widget existingWidget3 = Widget.builder().id(3L).zetaIndex(3).build();
        Widget existingWidget4 = Widget.builder().id(4L).zetaIndex(4).build();
        WidgetDto updatingWidgetDto = WidgetDto.builder().id(3L).zetaIndex(2).build();
        Widget newWidget = Widget.builder().id(3L).zetaIndex(2).build();

        when(widgetRepository.findByZIndex(any()))
                .thenReturn(Optional.of(existingWidget2))
                .thenReturn(Optional.of(existingWidget3))
                .thenReturn(Optional.empty());
        when(widgetRepository.save(any())).thenReturn(newWidget);

        widgetService.updateWidget(updatingWidgetDto);

        ArgumentCaptor<Collection<Widget>> widgetsArgument = ArgumentCaptor.forClass(ArrayList.class);
        verify(widgetRepository, times(0)).getMaxZIndex();
        verify(widgetRepository, times(1)).save(any());
        verify(widgetRepository, times(1)).saveAll(widgetsArgument.capture());

        Optional<Widget> widget1 = widgetsArgument.getValue().stream().filter(widget -> widget.getId().longValue() == existingWidget1.getId().longValue()).findAny();
        Optional<Widget> widget2 = widgetsArgument.getValue().stream().filter(widget -> widget.getId().longValue() == existingWidget2.getId().longValue()).findAny();
        Optional<Widget> widget3 = widgetsArgument.getValue().stream().filter(widget -> widget.getId().longValue() == existingWidget3.getId().longValue()).findAny();
        Optional<Widget> widget4 = widgetsArgument.getValue().stream().filter(widget -> widget.getId().longValue() == existingWidget4.getId().longValue()).findAny();

        assertTrue(widget1.isEmpty());
        assertTrue(widget2.isPresent() && widget2.get().getZetaIndex() == 3);
        assertTrue(widget3.isEmpty());
        assertTrue(widget4.isEmpty());
    }

    @Test
    void whenDeleteWidgetAndIdIsNull_thenException() {
        assertThrows(IllegalArgumentException.class, () -> widgetService.deleteWidget(null));
    }

    @Test
    void whenDeleteWidgetAndNonExistentId_thenFalse() {
        widgetService.deleteWidget(anyLong());

        verify(widgetRepository, times(1)).deleteWidget(anyLong());
    }
}