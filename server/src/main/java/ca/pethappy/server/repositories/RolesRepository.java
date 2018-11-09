package ca.pethappy.server.repositories;

import ca.pethappy.server.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolesRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
