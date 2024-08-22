package com.study.contactapi.domain.user;

import org.hibernate.annotations.CreationTimestamp;
import jakarta.persistence.Table;

import java.util.Date;
import java.util.List;

import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "login")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Login {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;
  
  @Column(unique = true)
  private String email;

  private String password;

  @Column(columnDefinition = "TIMESTAMP WITHOUT TIME ZONE default NULL")
  private Date account_activated_at;
  
  @CreationTimestamp
  @Column(columnDefinition = "TIMESTAMP WITHOUT TIME ZONE default NOW()")
  private Date created_at;

  @UpdateTimestamp
  @Column(columnDefinition = "TIMESTAMP WITHOUT TIME ZONE default NOW()")
  private Date updated_at;

  @OneToOne(mappedBy = "login")
  private User user;

  @OneToMany( cascade = CascadeType.REMOVE, orphanRemoval = true, mappedBy = "login")
  private List<AccountConfirmationToken> account_confirmation_token;

  public Login(String email, String password) {
    this.email = email;
    this.password = password;
  }
}
