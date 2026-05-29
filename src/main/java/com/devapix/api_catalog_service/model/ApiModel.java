package com.devapix.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="api")
public class ApiModel {
    @Id
    @GeneratedValue
    Integer id;
    @Column(name = "owner_id")
    Integer ownerId;
    @Column(name = "category_id")
    Integer categoryId;
    String name;
    String description;
    @Column(name = "base_url")
    String baseUrl;
    String visibility;
    int price;
    @Column(name = "request_limit")
    int requestLimit;
    @CreationTimestamp
    @Column(name = "created_at")
    Date createdAt;
    @UpdateTimestamp
    @Column(name = "updated_at")
    Date updatedAt;

}
