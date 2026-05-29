package com.devapix.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@Table(name = "api_endpoints")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiEndpoint {

        @Id
        @GeneratedValue
        private Integer id;

        private Integer apiId;

        private String endpoint;

        private String method;

        @Column(columnDefinition = "TEXT")
        private String headersJson;

        @Column(columnDefinition = "TEXT")
        private String paramsJson;

        @Column(columnDefinition = "TEXT")
        private String sampleRequest;

        @Column(columnDefinition = "TEXT")
        private String sampleResponse;

        @Column(columnDefinition = "TEXT")
        private String statusCodesJson;

        @CreationTimestamp
        private Timestamp createdAt;

}
