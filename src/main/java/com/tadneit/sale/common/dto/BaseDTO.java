package com.tadneit.sale.common.dto;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BaseDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String createdBy;

    @JsonIgnore
    private LocalDateTime createdDate;

    @JsonIgnore
    private LocalDateTime updatedDate;

    private String updatedBy;

    @JsonGetter("updatedDate")
    public Long getEpochMillisUpdatedDate() {
        if (Objects.isNull(updatedDate)) {
            return LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli();
        }
        return updatedDate.toInstant(ZoneOffset.UTC).toEpochMilli();
    }

    @JsonGetter("createdDate")
    public Long getEpochMillisCreatedDate() {
        if (Objects.isNull(createdDate)) {
            return LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli();
        }
        return createdDate.toInstant(ZoneOffset.UTC).toEpochMilli();
    }
}

