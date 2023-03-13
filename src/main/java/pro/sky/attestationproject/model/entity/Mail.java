package pro.sky.attestationproject.model.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import pro.sky.attestationproject.model.MailType;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Mail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer mailId;
    private MailType packageType;
    private int recieverIndex;
    private String recieverAddress;
    private String recieverName;
    @OneToMany(mappedBy = "mail")
    private List<Event> route;
    public Mail() {
        route = new ArrayList<Event>();
    }

    @Override
    public String toString(){
        return "id: " + mailId +" " +  packageType + " to: " + recieverName
                + " " + recieverIndex + " " + recieverAddress;
    }

}
