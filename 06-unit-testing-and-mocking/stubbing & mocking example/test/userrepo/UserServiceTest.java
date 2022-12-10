package userrepo;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository repositoryMock;

    // Stub way
    @Test
    void testRegisterWhenUserAlreadyExists() {
        UserRepository repo = new PositiveUserRepositoryStubImpl();
        UserService service = new UserService(repo);

        assertThrows(
                UserAlreadyExistException.class,
                () -> service.register("test@test.com", "weak"),
                "UserAlreadyExistException was expected but was not thrown"
        );
    }

    @Test
    void testRegisterWhenUSerIsSuccessfullyRegistered() {
        UserRepository repo = new InMemoryUserRepositoryStubImpl();
        UserService service = new UserService(repo);

        User user = new User("pesho@gmail.com", "Pesho123");

        assertEquals(
                user, service.register("pesho@gmail.com", "Pesho123"),
                "Registered user should be returned upon registration"
        );
    }

    // Mock way
    @Test
    void testRegisterWhenUserAlreadyExistsMockWay() {
        UserRepository mockRepo = mock(UserRepository.class);
        when(mockRepo.exist("test@test.com")).thenReturn(true);
        // when(repositoryMock.exist("test@test.com")).thenReturn(true);

        UserService service = new UserService(mockRepo);
        // UserService service = new UserService(repositoryMock);
        assertThrows(
                UserAlreadyExistException.class,
                () -> service.register("test@test.com", "weak"),
                "UserAlreadyExistException was expected but was not thrown"
        );

        verify(mockRepo, never()).save(any(User.class));
    }

    @Test
    void testRegisterWhenUSerIsSuccessfullyRegisteredMockWay() {
        UserRepository mockRepo = mock(UserRepository.class);
        when(mockRepo.exist("pesho@gmail.com")).thenReturn(false);

        User user = new User("pesho@gmail.com", "Pesho123");

        UserService service = new UserService(mockRepo);

        assertEquals(
                user, service.register("pesho@gmail.com", "Pesho123"),
                "Registered user should be returned upon registration"
        );

        // verify(mockRepo, atLeastOnce()).save(any(User.class));
        verify(mockRepo, times(1)).save(any(User.class));
        // anyInt(), anyString(), anyList(), any(MyClass.class) - argument matchers.
    }

}
