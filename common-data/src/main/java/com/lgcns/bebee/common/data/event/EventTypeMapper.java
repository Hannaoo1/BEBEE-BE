package com.lgcns.bebee.common.data.event;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
public class EventTypeMapper {
    private final Map<String, Class<? extends DomainEvent>> registry = new HashMap<>();

    @PostConstruct
    public void init(){
        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter(new AssignableTypeFilter(DomainEvent.class));

        Set<BeanDefinition> components = scanner.findCandidateComponents("com.lgcns.bebee");

        for (BeanDefinition bd : components) {
            try{
                Class<?> type = Class.forName(bd.getBeanClassName());

                if (DomainEvent.class.isAssignableFrom(type)) {
                    Class<? extends DomainEvent> eventClass = type.asSubclass(DomainEvent.class);
                    registry.put(eventClass.getSimpleName(), eventClass);
                }
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("이벤트 클래스를 로드할 수 없습니다.", e);
            }
        }
    }

    public Class<? extends DomainEvent> getClass(String eventType) {
        Class<? extends DomainEvent> clazz = registry.get(eventType);
        if (clazz == null) {
            throw new RuntimeException("미등록된 이벤트 타입입니다: " + eventType);
        }
        return clazz;
    }
}
