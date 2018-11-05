package ca.pethappy.server.services;

import ca.pethappy.server.models.User;
import ca.pethappy.server.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsersService {
    private final UsersRepository usersRepository;

    @Autowired
    public UsersService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public User findByEmail(String email) {
        return usersRepository.findByEmail(email);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public User findByEmailFetchRoles(String email) {
        return usersRepository.findByEmailFetchRoles(email);
    }

}
