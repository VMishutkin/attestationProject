package pro.sky.attestationproject.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;

import java.util.ArrayList;

@Entity
public class PostOffice {
    @Id
    @GeneratedValue
    private int id;
    private int index;
    private String name;
    private String address;
    @ManyToMany
    private ArrayList<Package> packages;
}
