package net.robyf.ms.user;

import net.robyf.ms.user.api.AuthenticateRequest;
import net.robyf.ms.user.api.AuthenticateResponse;
import net.robyf.ms.user.api.AuthenticateStatus;
import net.robyf.ms.user.api.CreateUserRequest;
import net.robyf.ms.user.api.User;
import net.robyf.ms.user.persistence.PersistenceUser;
import net.robyf.ms.user.persistence.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;

import java.util.Optional;
import java.util.UUID;

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

    public User getUserById(final UUID id) {
        Optional<PersistenceUser> pUser = repository.findById(id);
        if (pUser.isEmpty()) {
            throw Problem.valueOf(Status.NOT_FOUND, "User with id " + id + " not found");
        }
        return User.build(pUser.get());
    }

    public User getUserByEmail(final String email) {
        Optional<PersistenceUser> pUser = repository.findFirstByEmail(email);
        if (pUser.isEmpty()) {
            throw Problem.valueOf(Status.NOT_FOUND, "User with email " + email + " not found");
        }
        return User.build(pUser.get());
    }

    public AuthenticateResponse authenticate(final AuthenticateRequest request) {
        Optional<PersistenceUser> opUser = repository.findFirstByEmail(request.getEmail());
        if (opUser.isEmpty()) {
            return AuthenticateResponse.builder().status(AuthenticateStatus.FAIL).build();
        }
        PersistenceUser pUser = opUser.get();
        // TODO: consider password encryption
        if (!request.getPassword().equals(pUser.getPassword())) {
            return AuthenticateResponse.builder().status(AuthenticateStatus.FAIL).build();
        }
        return AuthenticateResponse.builder().status(AuthenticateStatus.SUCCESS).user(User.build(pUser)).build();
    }

}
