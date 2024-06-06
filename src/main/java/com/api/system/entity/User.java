package com.api.system.entity;



import io.swagger.annotations.ApiModel;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ApiModel(description = "This table holds user accounts information.")
public class User {

    @Id
    @GeneratedValue
    private int id;

    @NotBlank(message="Name should not be null or empty.")
    @Pattern(regexp= "^[A-Z][a-z]{2,15}$", message="Invalid name has been entered.")
    private String name;

    @NotBlank(message="Gender should not be null or empty.")
    @Pattern(regexp="^(female|male|f|m)", flags = Pattern.Flag.CASE_INSENSITIVE, message="Invalid gender has been entered.")
    private String gender;

    @Min(value = 1, message = "Age should be 1 at least.")
    @Max(value = 100, message = "Age should be 100 at most.")
    private int age;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    public User(String name, String gender, int age) {
        this.name = name;
        this.gender = gender;
        this.age = age;
    }

    public User(String gender, int age) {
        this.gender = gender;
        this.age = age;
    }


}
