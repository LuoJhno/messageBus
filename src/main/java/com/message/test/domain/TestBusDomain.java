package com.message.test.domain;

import com.message.bus.entity.BaseBusMessage;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TestBusDomain extends BaseBusMessage {

    private Long id;

    private String name;

    private String icon;

    private Integer homeSorted;

    private String description;

}
