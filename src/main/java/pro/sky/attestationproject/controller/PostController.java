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

    @PostMapping("/register")
    public ResponseEntity registerMail(MailDto mailDto, int officeId) {
        if (postService.registerPackage(mailDto, officeId)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @PostMapping("/arrival")
    public ResponseEntity arrivalMail(int mailId, int officeId) {
        if (postService.arrivalMail(mailId, officeId)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @PostMapping("/depart")
    public ResponseEntity departPackage(int mailId, int officeId) {
        if (postService.departMail(mailId, officeId)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @PostMapping("/recieving")
    public ResponseEntity recievingMail(int mailId, int officeId) {
        postService.receivigMail(mailId, officeId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/getStatus/{mailId}")
    public String getMailStatus(@PathVariable int mailId) {
        return postService.getMailStatus(mailId);
    }

    @GetMapping("/getHistory/{mailId}")
    public String getMailHistory(@PathVariable int mailId) {
        postService.getMailHistory(mailId);
        return postService.getMailHistory(mailId);
    }


}
