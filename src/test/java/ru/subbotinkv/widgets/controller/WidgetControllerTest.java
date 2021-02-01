package ru.subbotinkv.widgets.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.subbotinkv.widgets.dto.WidgetDto;
import ru.subbotinkv.widgets.service.IWidgetService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = WidgetController.class)
class WidgetControllerTest {

    private static final String BASE_URL = "/api/widgets";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private IWidgetService widgetService;

    @Test
    void whenPostRequestToWidgetsAndValidWidget_thenCreated() throws Exception {
        WidgetDto widget = WidgetDto.builder()
                .xCoordinate(0)
                .yCoordinate(0)
                .width(100)
                .height(100)
                .build();

        WidgetDto createdWidget = WidgetDto.builder()
                .build();

        when(widgetService.createWidget(any())).thenReturn(createdWidget);

        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(widget)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void whenPostRequestToWidgetsAndInvalidWidget_thenBadRequest() throws Exception {
        WidgetDto widget = WidgetDto.builder()
                .id(1L)
                .lastModifiedDate(LocalDateTime.now())
                .build();

        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(widget)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON));
    }

    @Test
    void whenGetRequestToWidgets_thenOk() throws Exception {
        when(widgetService.getAllWidgets()).thenReturn(Collections.emptyList());

        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void whenGetRequestToWidgetsAndExistingId_thenOk() throws Exception {
        WidgetDto widget = WidgetDto.builder()
                .build();

        when(widgetService.getWidgetById(any())).thenReturn(Optional.of(widget));

        mockMvc.perform(get(BASE_URL + "/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void whenGetRequestToWidgetsAndNonExistentId_thenNotFound() throws Exception {
        when(widgetService.getWidgetById(any())).thenReturn(Optional.empty());

        mockMvc.perform(get(BASE_URL + "/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON));
    }

    @Test
    void whenPutRequestToWidgetsAndExistingIdAndValidWidget_thenOk() throws Exception {
        WidgetDto widget = WidgetDto.builder()
                .xCoordinate(0)
                .yCoordinate(0)
                .zetaIndex(0)
                .width(100)
                .height(100)
                .build();

        WidgetDto createdWidget = WidgetDto.builder().id(1L)
                .xCoordinate(0)
                .yCoordinate(0)
                .zetaIndex(0)
                .width(100)
                .height(100)
                .build();

        when(widgetService.updateWidget(any())).thenReturn(createdWidget);

        mockMvc.perform(put(BASE_URL + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(widget)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void whenPutRequestToWidgetsAndInvalidWidget_thenBadRequest() throws Exception {
        WidgetDto widget = WidgetDto.builder()
                .xCoordinate(-1)
                .yCoordinate(-1)
                .lastModifiedDate(LocalDateTime.now())
                .build();

        mockMvc.perform(put(BASE_URL + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(widget)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON));
    }

    @Test
    void whenDeleteRequestToWidgetsAndExistingId_thenNoContent() throws Exception {
        when(widgetService.deleteWidget(any())).thenReturn(true);

        mockMvc.perform(delete(BASE_URL + "/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void whenDeleteRequestToWidgetsAndNonExistentId_thenNotFound() throws Exception {
        when(widgetService.deleteWidget(any())).thenReturn(false);

        mockMvc.perform(delete(BASE_URL + "/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON));
    }
}
