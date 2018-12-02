package ca.pethappy.server.repositories;

import ca.pethappy.server.models.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardsRepository extends JpaRepository<Card, Long> {
    List<Card> findAllByUserId(Long userId);
}
