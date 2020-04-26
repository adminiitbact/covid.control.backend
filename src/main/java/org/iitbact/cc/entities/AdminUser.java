package org.iitbact.cc.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.io.Serializable;


/**
 * The persistent class for the admin_users database table.
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "admin_users")
@NamedQuery(name = "AdminUser.findAll", query = "SELECT a FROM AdminUser a")
public class AdminUser implements Serializable {
    private static final long serialVersionUID = 1L;

    private String designation;
    
    private String jurisdiction;
    
    private int region;

    @Column(name = "email_id")
    private String emailId;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    @Column(name = "user_id")
    private String userId;
}