package com.devapix.api_catalog_service.dto.response;


import java.util.Date;
import lombok.Data;

@Data
public class ApiResponse {

    private Integer id;
    private Integer ownerId;
    private String publisherName;
    private Integer categoryId;
    private String name;
    private String description;
    private String baseUrl;
    private String visibility;
    private String status;
    private Date createdAt;
    private Date updatedAt;
}
