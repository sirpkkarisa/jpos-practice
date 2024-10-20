package com.pkteq.JPOSServer.task.entities.business;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class CustomerDetail  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToMany(mappedBy = "customerDetail")
    private List<AccountDetail> accountDetails = new ArrayList<>();
}
