package com.fpt.duantn.domain;

import com.fpt.duantn.models.ERole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Data

@Entity
@Table (name = "roles")
public class Role {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name="code")
    private String code;

    @Enumerated(EnumType.STRING)
    @Column(name="name")
    private ERole name;


    @Column(name="type")
    private Integer type;


    //bi-directional many-to-one association to Employee
//    @OneToMany(mappedBy="role")
//    private List<Employee> employees;



}
