package ru.subbotinkv.widgets.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ru.subbotinkv.widgets.dto.WidgetDto;
import ru.subbotinkv.widgets.model.Widget;
import ru.subbotinkv.widgets.repository.IWidgetRepository;
import ru.subbotinkv.widgets.service.IWidgetService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

@Transactional
@Service
public class WidgetServiceImpl implements IWidgetService {

    private final ModelMapper modelMapper;
    private final IWidgetRepository widgetRepository;
    private final Lock writeLock;
    private final Lock readLock;

    public WidgetServiceImpl(ModelMapper modelMapper, IWidgetRepository widgetRepository) {
        this.modelMapper = modelMapper;
        this.widgetRepository = widgetRepository;

        ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
        writeLock = readWriteLock.writeLock();
        readLock = readWriteLock.readLock();
    }

    @Override
    public WidgetDto createWidget(WidgetDto widgetDto) {
        Assert.notNull(widgetDto, "Widget must not be null");

        try {
            writeLock.lock();

            return saveWidget(widgetDto);
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public Collection<WidgetDto> getAllWidgets() {
        try {
            readLock.lock();

            return widgetRepository.findAllOrderByZetaIndexAsc().stream()
                    .map(source -> modelMapper.map(source, WidgetDto.class))
                    .collect(Collectors.toList());
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public Optional<WidgetDto> getWidgetById(Long id) {
        Assert.notNull(id, "Id must not be null");

        try {
            readLock.lock();

            Optional<Widget> widget = widgetRepository.findById(id);
            Optional<WidgetDto> widgetDto = Optional.empty();

            if (widget.isPresent()) {
                widgetDto = Optional.ofNullable(modelMapper.map(widget.get(), WidgetDto.class));
            }

            return widgetDto;
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public WidgetDto updateWidget(WidgetDto widgetDto) {
        Assert.notNull(widgetDto, "Widget must not be null");

        try {
            writeLock.lock();

            return saveWidget(widgetDto);
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public boolean deleteWidget(Long id) {
        Assert.notNull(id, "Id must not be null");

        try {
            writeLock.lock();

            return widgetRepository.deleteWidget(id);
        } finally {
            writeLock.unlock();
        }
    }

    private WidgetDto saveWidget(WidgetDto widgetDto) {
        if (widgetDto.getZetaIndex() == null) {
            Optional<Integer> maxZIndex = widgetRepository.getMaxZIndex();
            int zIndex = 0;
            if (maxZIndex.isPresent()) {
                zIndex = maxZIndex.get() + 1;
            }

            widgetDto.setZetaIndex(zIndex);
        } else {
            Integer zIndex = widgetDto.getZetaIndex();

            ArrayList<Widget> shiftingWidgets = new ArrayList<>();

            // Shift till the end or till the gap
            Optional<Widget> optWidget = widgetRepository.findByZIndex(zIndex);
            while (optWidget.isPresent()) {
                if (widgetDto.getId() != null && widgetDto.getId().longValue() == optWidget.get().getId().longValue()) {
                    // Or till itself
                    break;
                }

                zIndex++;
                Widget shiftingWidget = optWidget.get();
                optWidget = widgetRepository.findByZIndex(zIndex);

                shiftingWidget.setZetaIndex(zIndex);
                shiftingWidgets.add(shiftingWidget);
            }

            widgetRepository.saveAll(shiftingWidgets);
        }

        Widget widget = modelMapper.map(widgetDto, Widget.class);

        return modelMapper.map(widgetRepository.save(widget), WidgetDto.class);
    }
}
