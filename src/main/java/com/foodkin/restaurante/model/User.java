package com.foodkin.restaurante.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

@Data
@Document(collection = "user")
public class User {
    @Id
    private String id;

    private String name;
    @Indexed(unique = true)
    private String email;

    private String password;
    private String rol;
}
