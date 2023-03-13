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

    /**
     *
     * @param mailDto шаблон для созданиня почтового отправления
     * @param officeId ИД офиса в котором регистрируется отправление
     * @return
     */
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

    /**
     * Регистрация события
     * @param mailId посылка
     * @param officeId ид офиса
     * @param eventType вид события
     * @return
     */
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

    /**
     * Прибытие отправления в офис. она должна иметь статус DEPARTED иначе метод не сработает
     * @param mailId номер отправления
     * @param officeId номер офиса в который прибывает почта
     * @return
     */
    public boolean arrivalMail(int mailId, int officeId) {
        Optional<Event> lastEvent = eventRepository.findLastStatusById(mailId);
        if (lastEvent.isPresent()) {
            EventType status = lastEvent.get().getEventType();
            if (status == EventType.DEPARTED) {
                return createEvent(mailId, officeId, EventType.ARRIVED);
            }
        }
        return false;
    }

    /**
     * Отправление посылки. Она должна иметь статус ARRIVED или REGISTERED
     * @param mailId номер отправления
     * @param officeId номер офиса из которого улетает почта
     * @return
     */
    public boolean departMail(int mailId, int officeId) {
        Optional<Event> lastEvent = eventRepository.findLastStatusById(mailId);
        if (lastEvent.isPresent()) {
            EventType status = lastEvent.get().getEventType();
            if (status == EventType.REGISTERED || status == EventType.ARRIVED) {
                return createEvent(mailId, officeId, EventType.DEPARTED);
            }
        }
        return false;
    }
    /**
     * Получение почты, она должна иметь стасус ARRIVED
     * @param mailId номер отправления
     * @param officeId офис должен совпадать с назначением посылки
     * @return
     */
    public boolean receivigMail(int mailId, int officeId) {
        Optional<Event> lastEvent = eventRepository.findLastStatusById(mailId);
        if (lastEvent.isPresent()) {
            EventType status = lastEvent.get().getEventType();
            if (status == EventType.ARRIVED && isDelievered(lastEvent.get())) {
                createEvent(mailId, officeId, EventType.RECIEVED);
                return true;
            }
        }
        return false;
    }

    /**
     * Проверка, что посылка доехала до пункта назначения
     * @param event последнее событие (должно быть прибытие в конечный пункт)
     * @return
     */
    private boolean isDelievered(Event event) {
        if (event.getPostOffice().getIndex() == event.getMail().getRecieverIndex()) {
            return true;
        }
        return false;
    }

    /**
     * Получение и вывод последнего события по ID
     * @param mailId
     * @return последнее событие в виде строки
     */
    public String getMailStatus(int mailId) {
        Optional<Event> status = eventRepository.findLastStatusById(mailId);
        if (status.isPresent()) {
            return status.get().toString();
        }
        return "Mail not found";

        //return status.toString();
    }

    /**
     * получение и вывод всей истории перемещений по ID посылки
     * @param mailId
     * @return строка с соединенным список событий
     */
    public String getMailHistory(int mailId) {

        List<Event> history = eventRepository.getHistoryById(mailId);
        if (!history.isEmpty()) {
            return history.stream().map(Event::toString).collect(Collectors.joining("\n"));
        }
        return "Mail not found";
    }
}
