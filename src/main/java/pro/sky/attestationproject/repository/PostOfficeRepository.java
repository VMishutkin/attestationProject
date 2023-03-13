package pro.sky.attestationproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.sky.attestationproject.model.entity.PostOffice;

import java.util.Optional;

@Repository
public interface PostOfficeRepository extends JpaRepository<PostOffice, Integer> {

}
