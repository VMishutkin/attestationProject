package pro.sky.attestationproject.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pro.sky.attestationproject.model.dto.MailDto;
import pro.sky.attestationproject.service.PostService;

@RestController
@RequestMapping
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    /**
     *
     * @param mailDto шаблон для созданиня почтового отправления
     * @param officeId ИД офиса в котором регистрируется отправление
     * @return
     */

    @PostMapping("/register")
    public ResponseEntity registerMail(MailDto mailDto, int officeId) {
        if (postService.registerPackage(mailDto, officeId)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    /**
     * Прибытие отправления в офис. она должна иметь статус DEPARTED иначе метод не сработает
     * @param mailId номер отправления
     * @param officeId номер офиса в который прибывает почта
     * @return
     */
    @PostMapping("/arrival")
    public ResponseEntity arrivalMail(int mailId, int officeId) {
        if (postService.arrivalMail(mailId, officeId)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    /**
     * Отправление посылки. Она должна иметь статус ARRIVED или REGISTERED
     * @param mailId номер отправления
     * @param officeId номер офиса из которого улетает почта
     * @return
     */
    @PostMapping("/depart")
    public ResponseEntity departPackage(int mailId, int officeId) {
        if (postService.departMail(mailId, officeId)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    /**
     * Получение почты, она должна иметь стасус ARRIVED
     * @param mailId номер отправления
     * @param officeId офис должен совпадать с назначением посылки
     * @return
     */
    @PostMapping("/recieving")
    public ResponseEntity recievingMail(int mailId, int officeId) {
        postService.receivigMail(mailId, officeId);
        return ResponseEntity.ok().build();
    }

    /**
     * Посмотреть текущий статус почты
     * @param mailId номер отправления
     * @return
     */
    @GetMapping("/getStatus/{mailId}")
    public String getMailStatus(@PathVariable int mailId) {
        return postService.getMailStatus(mailId);
    }

    /**
     * посмотреть историю перемещений посылки
     * @param mailId номер отправления
     * @return
     */
    @GetMapping("/getHistory/{mailId}")
    public String getMailHistory(@PathVariable int mailId) {
        postService.getMailHistory(mailId);
        return postService.getMailHistory(mailId);
    }


}
