package pro.sky.attestationproject.service;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.sky.attestationproject.model.EventType;
import pro.sky.attestationproject.model.dto.MailDto;
import pro.sky.attestationproject.model.entity.Event;
import pro.sky.attestationproject.model.entity.Mail;
import pro.sky.attestationproject.model.entity.PostOffice;
import pro.sky.attestationproject.repository.EventRepository;
import pro.sky.attestationproject.repository.MailRepository;
import pro.sky.attestationproject.repository.PostOfficeRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PostService {
    private final Logger logger = LoggerFactory.getLogger(PostService.class);
    private final MailRepository mailRepository;
    private final EventRepository eventRepository;
    private final PostOfficeRepository postOfficeRepository;
    private final Mapper mapper;

    public PostService(MailRepository packageRepository, EventRepository eventRepository, PostOfficeRepository postOfficeRepository, Mapper mapper) {
        this.mailRepository = packageRepository;
        this.eventRepository = eventRepository;
        this.postOfficeRepository = postOfficeRepository;
        this.mapper = mapper;
    }


    public boolean registerPackage(MailDto mailDto, int officeId) {
        try {
            Mail mail = mapper.mailDtoToMail(mailDto);
            mail = mailRepository.save(mail);
            createEvent(mail.getMailId(), officeId, EventType.REGISTERED);
        } catch (NullPointerException | IllegalArgumentException | NoSuchElementException e) {
            logger.error("wrong arguments");
            return false;
        }
        return true;


    }

    private boolean createEvent(int mailId, int officeId, EventType eventType) {
        try {
            Mail mail = mailRepository.findById(mailId).orElseThrow(NoSuchElementException::new);
            PostOffice office = postOfficeRepository.findById(officeId).orElseThrow(NoSuchElementException::new);
            Event event = new Event();
            event.setMail(mail);
            event.setPostOffice(office);
            event.setDate(LocalDateTime.now());
            event.setEventType(eventType);
            eventRepository.save(event);
            return true;
        } catch (NullPointerException | IllegalArgumentException | NoSuchElementException e) {
            logger.error("wrong arguments");
        }
        return false;
    }


    public boolean arrivalMail(int mailId, int postOfficeId) {
        Optional<Event> lastEvent = eventRepository.foundLastStatusById(mailId);
        if (lastEvent.isPresent()) {
            EventType status = lastEvent.get().getEventType();
            if (status == EventType.DEPARTED) {
                return createEvent(mailId, postOfficeId, EventType.ARRIVED);
            }
        }
        return false;
    }

    public boolean departMail(int mailId, int postOfficeId) {
        Optional<Event> lastEvent = eventRepository.foundLastStatusById(mailId);
        if (lastEvent.isPresent()) {
            EventType status = lastEvent.get().getEventType();
            if (status == EventType.REGISTERED || status == EventType.ARRIVED) {
                return createEvent(mailId, postOfficeId, EventType.DEPARTED);
            }
        }
        return false;
    }

    public boolean receivigMail(int mailId, int postOfficeId) {
        Optional<Event> lastEvent = eventRepository.foundLastStatusById(mailId);
        if (lastEvent.isPresent()) {
            EventType status = lastEvent.get().getEventType();
            if (status == EventType.ARRIVED && isDelievered(lastEvent.get(), mailId)) {
                createEvent(mailId, postOfficeId, EventType.RECIEVED);
                return true;
            }
        }
        return false;
    }


    private boolean isDelievered(Event event, int mailId) {
        if (event.getPostOffice().getIndex() == event.getMail().getRecieverIndex()) {
            return true;
        }
        return false;
    }


    public String getMailStatus(int mailId) {
        Optional<Event> status = eventRepository.foundLastStatusById(mailId);
        if (status.isPresent()) {
            return status.get().toString();
        }
        return "Mail not found";

        //return status.toString();
    }

    public String getMailHistory(int mailId) {

        List<Event> history = eventRepository.getHistoryById(mailId);
        if (!history.isEmpty()) {
            return history.stream().map(Event::toString).collect(Collectors.joining("\n"));
        }
        return "Mail not found";
    }
}
