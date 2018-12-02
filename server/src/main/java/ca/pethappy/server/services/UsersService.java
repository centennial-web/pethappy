package ca.pethappy.server.services;

import ca.pethappy.server.consts.Consts;
import ca.pethappy.server.forms.UserSettings;
import ca.pethappy.server.models.Role;
import ca.pethappy.server.models.User;
import ca.pethappy.server.repositories.RolesRepository;
import ca.pethappy.server.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsersService {
    private final UsersRepository usersRepository;
    private final RolesRepository rolesRepository;
    private final SmsService smsService;

    @Autowired
    public UsersService(UsersRepository usersRepository, RolesRepository rolesRepository, SmsService smsService) {
        this.usersRepository = usersRepository;
        this.rolesRepository = rolesRepository;
        this.smsService = smsService;
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public User findByEmailFetchRoles(String email) {
        return usersRepository.findByEmailFetchRoles(email);
    }

    @Transactional(readOnly = true)
    public User findByEmail(String email) {
        return usersRepository.findByEmail(email);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public User registerUser(User user) {
        // Add ROLE_USER if no roles
        if (user.getRoles().size() == 0) {
            // Find the ROLE_USER id
            Role roleUser = rolesRepository.findByName(Consts.ROLE_USER);
            if (roleUser != null) {
                user.getRoles().add(roleUser);
            } else {
                throw new RuntimeException("ROLE_USER couldn't be found on the database");
            }
        }
        // Save
        return usersRepository.save(user);
    }

    @Transactional(readOnly = true)
    public User findById(Long id) {
        return usersRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    @Transactional(readOnly = true)
    public UserSettings getUserSettings(Long userId) {
        User user = findById(userId);

        // Create user settings
        UserSettings userSettings = new UserSettings();
        userSettings.setId(userId);
        userSettings.setUse2fa(user.isUse2fa());

        return userSettings;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void updateUserSettings(UserSettings userSettings) {
        User user = findById(userSettings.getId());

        // Update user
        user.setUse2fa(userSettings.isUse2fa());
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void handle2FA(String email) {
        User user = findByEmail(email);

        // Uses 2FA?
        if (user.isUse2fa()) {
            String confirmationCode = smsService.generateVerificationCode();
            user.setVerificationCode(confirmationCode);
            user.setConfirmed(false);

            smsService.sendSms(user.getCellPhone(), "Your confirmation code is " + confirmationCode);
        }
    }
}
