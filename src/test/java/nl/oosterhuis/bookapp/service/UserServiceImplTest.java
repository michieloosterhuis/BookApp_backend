package nl.oosterhuis.bookapp.service;

import nl.oosterhuis.bookapp.dto.UserDto;
import nl.oosterhuis.bookapp.dto.UserInputDto;
import nl.oosterhuis.bookapp.dto.UserUpdateDto;
import nl.oosterhuis.bookapp.exception.DuplicateUserException;
import nl.oosterhuis.bookapp.exception.UserNotFoundException;
import nl.oosterhuis.bookapp.model.User;
import nl.oosterhuis.bookapp.repository.TransactionRepository;
import nl.oosterhuis.bookapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User userA;
    private User updatedUserA;
    private UserDto userDtoA;
    private UserDto updatedUserDtoA;
    private UserInputDto userInputDtoA;
    private UserUpdateDto UserUpdateDtoA;

    @BeforeEach
    public void setup() {
        userA = new User();
        userA.setUsername("christiaan");
        userA.setFirstName("Christiaan");
        userA.setLastName("Kreling");
        userA.setCity("Amersfoort");
        userA.setEmail("christiaan@gmail.com");
        userA.setPassword(new BCryptPasswordEncoder().encode("pass"));

        updatedUserA = userA;
        updatedUserA.setFirstName("Chris");
        updatedUserA.setLastName("Kreel");
        updatedUserA.setCity("Amsterdam");
        updatedUserA.setEmail("chris@gmail.com");

        userDtoA = new UserDto();
        userDtoA.setUsername("christiaan");
        userDtoA.setFirstName("Christiaan");
        userDtoA.setLastName("Kreling");
        userDtoA.setCity("Amersfoort");
        userDtoA.setEmail("christiaan@gmail.com");

        updatedUserDtoA = userDtoA;
        updatedUserDtoA.setFirstName("Chris");
        updatedUserDtoA.setLastName("Kreel");
        updatedUserDtoA.setCity("Amsterdam");
        updatedUserDtoA.setEmail("chris@gmail.com");

        userInputDtoA = new UserInputDto();
        userInputDtoA.setUsername("christiaan");
        userInputDtoA.setFirstName("Christiaan");
        userInputDtoA.setLastName("Kreling");
        userInputDtoA.setCity("Amersfoort");
        userInputDtoA.setEmail("christiaan@gmail.com");
        userInputDtoA.setPassword("pass");

        UserUpdateDtoA = new UserUpdateDto();
        UserUpdateDtoA.setFirstName("Chris");
        UserUpdateDtoA.setLastName("Kreel");
        UserUpdateDtoA.setCity("Amsterdam");
        UserUpdateDtoA.setEmail("chris@gmail.com");
    }

    @Test
    void givenUserList_whenGetAllUsers_thenReturnUserDtoList() {
        // given
        given(userRepository.findAll())
                .willReturn(List.of(userA));
        // when
        List<UserDto> userDtos = userService.getAllUsers();
        // then
        assertThat(userDtos).size().isEqualTo(1);
        assertThat(userDtos).element(0).usingRecursiveComparison().isEqualTo(userDtoA);
    }

    @Test
    void givenUsername_whenGetUser_thenReturnUserDto() throws UserNotFoundException {
        // given
        given(userRepository.findById(userA.getUsername()))
                .willReturn(Optional.ofNullable(userA));
        // when
        UserDto userDto = userService.getUser(userA.getUsername());
        // then
        assertThat(userDto).usingRecursiveComparison().isEqualTo(userDtoA);
    }

    @Test
    void givenUserInputDto_whenAddUser_thenReturnUserDto() throws DuplicateUserException {
        // given
        given(userRepository.save(Mockito.any(User.class))).willReturn(userA);
        // when
        UserDto userDto = userService.addUser(userInputDtoA);
        // then
        assertThat(userDto).usingRecursiveComparison().isEqualTo(userDtoA);
    }

    @Test
    void givenUsernameAndUserUpdateDto_whenUpdateUser_thenReturnUpdatedUserDto() throws UserNotFoundException, DuplicateUserException {
        // given
        given(userRepository.findById(userA.getUsername()))
                .willReturn(Optional.ofNullable(userA));
        given(userRepository.save(Mockito.any(User.class))).willReturn(updatedUserA);
        // when
        UserDto userDto = userService.updateUser(userA.getUsername(), UserUpdateDtoA);
        // then
        assertThat(userDto).usingRecursiveComparison().isEqualTo(updatedUserDtoA);
    }

    @Test
    void givenUsername_whenDeleteUser_thenNothing() throws UserNotFoundException {
        // given
        given(userRepository.findById(userA.getUsername()))
                .willReturn(Optional.ofNullable(userA));
        given(transactionRepository.findAllByRequesterOrProvider(Mockito.any(), Mockito.any()))
                .willReturn(List.of());
        willDoNothing().given(transactionRepository).deleteAll(Mockito.anyCollection());
        willDoNothing().given(userRepository).delete(Mockito.any(User.class));
        // when
        userService.deleteUser(userA.getUsername());
        // then
        verify(userRepository, times(1)).delete(Mockito.any(User.class));
    }

}