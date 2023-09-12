package com.intuit.craftDemo.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Data
@NoArgsConstructor
@Entity
@Table(name = "authentication_Token", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "id"
        }),
        @UniqueConstraint(columnNames = {
                "token"
        }),
        @UniqueConstraint(columnNames = {
                "email"
        })
})
public class AuthenticationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 100)
    private String token;

    @NaturalId
    private Date createdDate;

    private Long userId;
    @NotBlank
    @Size(max = 50)
    private String email;


    public AuthenticationToken(Long userId, String email) {
        this.userId = userId;
        this.email = email;
        this.createdDate = new Date();
        this.token = UUID.randomUUID().toString();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthenticationToken that = (AuthenticationToken) o;
        return id.equals(that.id) && token.equals(that.token) && Objects.equals(createdDate, that.createdDate) && userId.equals(that.userId) && email.equals(that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, token, createdDate, userId, email);
    }
}
