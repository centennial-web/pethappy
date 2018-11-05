package ca.pethappy.server.repositories;

import ca.pethappy.server.models.Manufacturer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@Repository
@RepositoryRestResource(collectionResourceRel = "manufacturers", path = "manufacturers")
public interface ManufacturersRepo extends JpaRepository<Manufacturer, Long> {
}
