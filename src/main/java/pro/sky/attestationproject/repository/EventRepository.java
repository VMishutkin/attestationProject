package pro.sky.attestationproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pro.sky.attestationproject.model.entity.Event;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {

    @Query(value = "select * from event where date = (SELECT MAX(date) FROM event WHERE route = :mailId)", nativeQuery = true)
    Optional<Event> findLastStatusById(int mailId);
    @Query(value = "select * from event where route = :mailId order by date", nativeQuery = true)
    List<Event> getHistoryById(int mailId);
}
