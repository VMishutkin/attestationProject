package pro.sky.attestationproject.service;

import org.springframework.stereotype.Service;
import pro.sky.attestationproject.model.MailType;
import pro.sky.attestationproject.model.dto.MailDto;
import pro.sky.attestationproject.model.entity.Mail;
import pro.sky.attestationproject.repository.PostOfficeRepository;

/*
    private int senderIndex;
    private String packageType;

    private int recieverIndex;
    private String recieverAddress;
    private String recieverName;

 */
@Service
public class Mapper {

    public Mail mailDtoToMail(MailDto mailDto) throws NullPointerException, IllegalArgumentException {
        Mail mail = new Mail();
        mail.setRecieverIndex(mailDto.getDestinationOfficeIndex());
        mail.setRecieverAddress(mailDto.getRecipientAddress());
        mail.setRecieverName(mailDto.getRecipientName());
        MailType type = Enum.valueOf(MailType.class, mailDto.getPackageType().toUpperCase());
        mail.setPackageType(type);
        return mail;
    }
}
