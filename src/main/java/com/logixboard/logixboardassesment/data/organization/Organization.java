package com.logixboard.logixboardassesment.data.organization;

import com.logixboard.logixboardassesment.data.EventTypes;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document
public class Organization {

    @Id
    private String id;
    @Indexed(unique = true)
    private String code;
    private EventTypes type;
}
