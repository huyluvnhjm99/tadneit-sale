package com.tadneit.sale.common.dto;

import com.tadneit.sale.common.enumeration.FileMappingType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileDTO extends BaseDTO {
    private UUID id;
    private String fileId;
    private String fileName;
    private String originalName;
    private String contentType;
    private String filePath;
    private String url;
    private Long size;
    private UUID mappingId;
    private FileMappingType mappingType;
}
