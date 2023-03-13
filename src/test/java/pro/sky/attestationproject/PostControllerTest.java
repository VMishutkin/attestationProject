package pro.sky.attestationproject;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import pro.sky.attestationproject.controller.PostController;
import pro.sky.attestationproject.model.EventType;
import pro.sky.attestationproject.model.MailType;
import pro.sky.attestationproject.model.entity.Event;
import pro.sky.attestationproject.model.entity.Mail;
import pro.sky.attestationproject.model.entity.PostOffice;
import pro.sky.attestationproject.repository.EventRepository;
import pro.sky.attestationproject.repository.MailRepository;
import pro.sky.attestationproject.repository.PostOfficeRepository;
import pro.sky.attestationproject.service.Mapper;
import pro.sky.attestationproject.service.PostService;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.web.servlet.function.RequestPredicates.param;

@WebMvcTest
public class PostControllerTest {
    @Autowired
    private MockMvc mockMvc;


    @MockBean
    private EventRepository eventRepository;
    @MockBean
    private MailRepository mailRepository;
    @MockBean
    private PostOfficeRepository postOfficeRepository;
    @SpyBean
    private PostService postService;
    @MockBean
    private Mapper mapper;
    @InjectMocks
    private PostController postController;

    @Test
    public void arrivalTest() throws Exception {
        Mail mail = getMailTest();
        Event event = getEvent();
        PostOffice postOffice = getPostOfficeTest();

        when(eventRepository.findLastStatusById(any(Integer.class))).thenReturn(Optional.of(event));
        when(mailRepository.findById(any(Integer.class))).thenReturn(Optional.of(mail));
        when(postOfficeRepository.findById(any(Integer.class))).thenReturn(Optional.of(postOffice));

        mockMvc.perform(
                post("/arrival")
                        .param("mailId", "1")
                        .param("officeId", "1")
        ).andExpect(status().isOk());
    }

    @Test
    public void departTest() throws Exception {
        Mail mail = getMailTest();
        Event event = getEvent();
        event.setEventType(EventType.ARRIVED);
        PostOffice postOffice = getPostOfficeTest();

        when(eventRepository.findLastStatusById(any(Integer.class))).thenReturn(Optional.of(event));
        when(mailRepository.findById(any(Integer.class))).thenReturn(Optional.of(mail));
        when(postOfficeRepository.findById(any(Integer.class))).thenReturn(Optional.of(postOffice));

        mockMvc.perform(
                post("/depart")
                        .param("mailId", "1")
                        .param("officeId", "1")
        ).andExpect(status().isOk());
    }

    @Test
    public void registerTest() throws Exception {

        JSONObject mailObject = new JSONObject();
        mailObject.put("packageType", "letter");
        mailObject.put("destinationOfficeIndex", 22);
        mailObject.put("recipientAddress", "test");
        mailObject.put("recipientName", "test");
        PostOffice postOffice = getPostOfficeTest();
        Event event = getEvent();
        event.setEventType(EventType.REGISTERED);
        when(eventRepository.save(any(Event.class))).thenReturn(event);
        when(postOfficeRepository.findById(any(Integer.class))).thenReturn(Optional.of(postOffice));



        mockMvc.perform(post("/register")
                        .content(mailObject.toString())
                        .contentType("application/json")
                        .param("officeId", "1"))
                .andExpect(status().isOk());

/*        mockMvc.perform(
                post("/register")
                        .params(params)
        ).andExpect(status().isOk());*/
    }

    @Test
    public void recievingTest() throws Exception {
        Mail mail = getMailTest();
        Event event = getEvent();
        event.setEventType(EventType.ARRIVED);
        PostOffice postOffice = getPostOfficeTest();
        postOffice.setIndex(22);
        event.setPostOffice(postOffice);
        event.setMail(mail);
        when(eventRepository.findLastStatusById(any(Integer.class))).thenReturn(Optional.of(event));
        when(mailRepository.findById(any(Integer.class))).thenReturn(Optional.of(mail));
        when(postOfficeRepository.findById(any(Integer.class))).thenReturn(Optional.of(postOffice));
        mockMvc.perform(
                post("/recieving")
                        .param("mailId", "1")
                        .param("officeId", "1")
        ).andExpect(status().isOk());
    }

    private Mail getMailTest() {
        Mail mail = new Mail();
        mail.setPackageType(MailType.LETTER);
        mail.setRecieverIndex(22);
        mail.setRecieverAddress("test");
        mail.setMailId(1);
        mail.setRecieverName("test");
        return mail;
    }

    private PostOffice getPostOfficeTest() {
        PostOffice postOffice = new PostOffice();
        postOffice.setAddress("test");
        postOffice.setId(1);
        postOffice.setName("test");
        postOffice.setIndex(11);
        return postOffice;
    }

    private Event getEvent() {
        Event event = new Event();
        event.setDate(LocalDateTime.of(2012, 12, 12, 12, 12));
        event.setEventType(EventType.DEPARTED);
        return event;
    }

    @Test
    public void getStatusTest() throws Exception {
        Event event = getEvent();
        Mail mail = getMailTest();
        event.setMail(mail);


        when(eventRepository.findLastStatusById(any(Integer.class))).thenReturn(Optional.of(event));
        mockMvc.perform(
                        get("/getStatus/3")
                ).andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("LETTER to:")));

    }

    @Test
    public void getHistory() throws Exception {
        Event event = getEvent();
        Mail mail = getMailTest();

        mail.setMailId(5);
        event.setMail(mail);


        when(eventRepository.findLastStatusById(any(Integer.class))).thenReturn(Optional.of(event));
        mockMvc.perform(
                        get("/getHistory/1")
                ).andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Mail not found")));
    }


}
