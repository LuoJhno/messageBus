package com.message.test.domain;

import com.message.bus.entity.BaseBusMessage;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class TestBusGenericDomain<T extends BaseBusMessage> {
    private Long id;

    private List<T> list;

    private String icon;

    private String description;
}
