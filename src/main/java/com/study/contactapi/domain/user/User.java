package com.study.contactapi.domain.user;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

import java.util.Date;
import java.util.List;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "contact_user")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  private String first_name;

  private String last_name;

  @CreationTimestamp
  @Column(columnDefinition = "TIMESTAMP WITHOUT TIME ZONE default NOW()")
  private Date created_at;

  @UpdateTimestamp
  @Column(columnDefinition = "TIMESTAMP WITHOUT TIME ZONE default NOW()")
  private Date updated_at;

  @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "login_id", referencedColumnName = "id")
  private Login login;

  @OneToMany( cascade = CascadeType.REMOVE, orphanRemoval = true, mappedBy = "contact_user")
  private List<Contact> contact;

  public User(String first_name, String last_name, Login login) {
    this.first_name = first_name;
    this.last_name = last_name;
    this.login = login;
  }
}
