package pro.sky.attestationproject.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import pro.sky.attestationproject.model.PackageType;

import java.util.ArrayList;


@Entity
public class Package {
    @Id
    @GeneratedValue
    private Integer id;
    private PackageType packageType;

    private int recieverIndex;
    private String recieverAddress;
    private String recieverName;
    @ManyToMany
    private ArrayList<PostOffice> route;
}
