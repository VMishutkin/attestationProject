package pro.sky.attestationproject.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

    @Operation(summary = "Регистрация",
            description = "Регистрация отправления, принимает данные по посылке и ID офиса в котором идет регистрация",
            responses = {@ApiResponse(
                    responseCode = "200",
                    description = "регистрация прошла"
            ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Ошибка входных данных, Id офиса не существует или неверно заполнены поля отправления"
                    )},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "mailType = letter, package, postcard, parcel"
            )
    )

    @PostMapping("/register")
    public ResponseEntity registerMail(@RequestBody MailDto mailDto,
                                       Integer officeId) {
        if (postService.registerPackage(mailDto, officeId)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @Operation(summary = "Прибытие отправления",
            description = "Прибытие отправления, оно должно иметь статус Зарегестрировано или Отправлено.",
            responses = {@ApiResponse(
                    responseCode = "200",
                    description = "Прибытие отправления зарегистрировано"
            ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Ошибка входных данных, Id офиса или отправления не существует или неверный статус посылки"
                    )}
    )

    @PostMapping("/arrival")
    public ResponseEntity arrivalMail(Integer mailId, Integer officeId) {
        if (postService.arrivalMail(mailId, officeId)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @Operation(summary = "Отправление",
            description = "Отправка из офиса. Посылка должна быть зарегистрирована или отправлена",
            responses = {@ApiResponse(
                    responseCode = "200",
                    description = "Отправление зарегистрировано"
            ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Ошибка входных данных, Id офиса или отправления не существует или неверный статус посылки"
                    )},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "mailType = letter, package, postcard, parcel"
            )
    )
    @PostMapping("/depart")
    public ResponseEntity departPackage(Integer mailId, Integer officeId) {
        if (postService.departMail(mailId, officeId)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @Operation(summary = "Получение посылки",
            description = "Регистрация получения посылка должна прибыть в конечный пункт",
            responses = {@ApiResponse(
                    responseCode = "200",
                    description = "Посылка получена"
            ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Ошибка входных данных, Id офиса или отправления не существует или неверный статус посылки"
                    )},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "mailType = letter, package, postcard, parcel"
            )
    )
    @PostMapping("/recieving")
    public ResponseEntity recievingMail(Integer mailId, Integer officeId) {
        postService.receivigMail(mailId, officeId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Узнать статус",
            description = "Получение последнего статуса посылки по ее ID"
    )
    @GetMapping("/getStatus/{mailId}")
    public String getMailStatus(@PathVariable Integer mailId) {
        return postService.getMailStatus(mailId);
    }

    @Operation(summary = "Просмотр истории",
            description = "Посмотреть все перемещения посылки по ID"

    )
    @GetMapping("/getHistory/{mailId}")
    public String getMailHistory(@PathVariable Integer mailId) {
        postService.getMailHistory(mailId);
        return postService.getMailHistory(mailId);
    }


}
