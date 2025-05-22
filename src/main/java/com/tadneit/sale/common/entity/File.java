package com.tadneit.sale.common.entity;

import com.tadneit.sale.common.enumeration.FileCategory;
import com.tadneit.sale.common.enumeration.FileMappingType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class File extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String fileName;
    private String originalName;
    private String fileId;
    private String contentType;
    private String filePath;
    private Long size;
    private UUID mappingId;

    @Enumerated(EnumType.STRING)
    private FileCategory category;

    @Enumerated(EnumType.STRING)
    private FileMappingType mappingType;
}
