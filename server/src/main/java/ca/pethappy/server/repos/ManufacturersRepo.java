package ca.pethappy.server.repos;

import ca.pethappy.server.models.Manufacturer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ManufacturersRepo extends JpaRepository<Manufacturer, Long> {
}
