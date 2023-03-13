package pro.sky.attestationproject.model.entity;

import javax.persistence.*;
import lombok.Data;
import pro.sky.attestationproject.model.EventType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;
    private LocalDateTime date;
    @ManyToOne
    @JoinColumn(name = "route")
    private Mail mail;
    @ManyToOne
    private PostOffice postOffice;
    private EventType eventType;

    @Override
    public String toString(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        return date.format(formatter) + " " + mail + ". was " + eventType + " in office: " + postOffice;
    }

}
