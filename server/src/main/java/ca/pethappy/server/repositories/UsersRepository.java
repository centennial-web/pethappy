package ca.pethappy.server.repositories;

import ca.pethappy.server.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u JOIN FETCH u.roles WHERE u.email = (:email)")
    User findByEmailFetchRoles(@Param("email") String email);

    User findByEmail(String email);
}
