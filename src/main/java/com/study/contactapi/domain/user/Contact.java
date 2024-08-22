package com.study.contactapi.domain.user;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "contact")
@Getter
@Setter
@NoArgsConstructor
public class Contact {
  @Id()
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;
  private String first_name;

  private String last_name;

  private String phone_number;

  @CreationTimestamp
  @Column(columnDefinition = "TIMESTAMP WITHOUT TIME ZONE default NOW()")
  private Date created_at;

  @UpdateTimestamp
  @Column(columnDefinition = "TIMESTAMP WITHOUT TIME ZONE default NOW()")
  private Date updated_at;


  @ManyToOne(optional = false)
  @PrimaryKeyJoinColumn(name = "contact_user_id", referencedColumnName = "id")
  private User contact_user;

  public Contact(String first_name, String last_name, String phone_number, User user) {
    this.first_name = first_name;
    this.last_name = last_name;
    this.phone_number = phone_number;
    this.contact_user = user;
  }
}
