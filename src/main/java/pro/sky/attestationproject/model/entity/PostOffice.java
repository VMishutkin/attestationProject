package pro.sky.attestationproject.model.entity;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
@Entity
@Data
public class PostOffice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private int index;
    private String name;
    private String address;
    @OneToMany
    private List<Event> statuses;

    public PostOffice() {
        statuses = new ArrayList<Event>();
    }

    @Override
    public String toString(){
        return id + " " + name +" " + index +   " " + address;
    }


}
