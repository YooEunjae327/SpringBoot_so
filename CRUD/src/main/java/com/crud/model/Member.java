package com.crud.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter @Setter
@Entity
@Table(name = "member")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;

    @Enumerated(EnumType.STRING)
    private MemberStatus status;

    @ManyToOne
    @JoinColumn(name = "member_id")
    @JsonManagedReference
    private Member member;

    @JsonBackReference
    @OneToMany(mappedBy = "member", fetch =  FetchType.LAZY, cascade = CascadeType.ALL )
    private List<Lend> lends;
}
