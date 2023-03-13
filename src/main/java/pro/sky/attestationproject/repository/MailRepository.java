package pro.sky.attestationproject.repository;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.sky.attestationproject.model.entity.Mail;

@Repository
public interface MailRepository extends JpaRepository<Mail, Integer> {

}
