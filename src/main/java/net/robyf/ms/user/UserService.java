package net.robyf.ms.user;

import net.robyf.ms.user.api.CreateUserRequest;
import net.robyf.ms.user.api.User;
import net.robyf.ms.user.persistence.PersistenceUser;
import net.robyf.ms.user.persistence.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UsersRepository repository;

    public User createUser(final CreateUserRequest request) {
        PersistenceUser pUser = PersistenceUser.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                // TODO: encrypt password
                .password(request.getPassword())
                .build();
        repository.save(pUser);

        return User.build(pUser);
    }

}
