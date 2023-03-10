package pro.sky.attestationproject.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import pro.sky.attestationproject.service.PostService;
@RestController
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    public ResponseEntity registerPackage(){
               postService.registerPackage();
    }

    public ResponseEntity arrivalPackage(){
        postService.arrivalPackage();
    }

    public ResponseEntity departPackage(){
        postService.departPackage();
    }

    public ResponseEntity recievingPackage(){
        postService.recievigPackage();
    }

    public ResponseEntity getPackageStatus(){
        postService.getPackageStatus();
    }



}
