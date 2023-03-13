package pro.sky.attestationproject.model.dto;

import lombok.Data;

@Data
public class MailDto {

//    private int senderOfficeIndex;
    private String packageType;
    private int destinationOfficeIndex;
    private String recipientAddress;
    private String recipientName;

}
