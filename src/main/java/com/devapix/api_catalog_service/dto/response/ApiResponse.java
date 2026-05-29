package com.devapix.dto.response;

import lombok.Data;
import java.util.Date;

@Data
public class ApiResponse {

    private Integer id;
    private Integer ownerId;
    private Integer categoryId;
    private String name;
    private String description;
    private String baseUrl;
    private String visibility;
    private int price;
    private int requestLimit;
    private Date createdAt;
    private Date updatedAt;
}
