package sviatoslav_slivinskyi_project_2.spring_application.serviceTest;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import sviatoslav_slivinskyi_project_2.spring_application.model.User;
import sviatoslav_slivinskyi_project_2.spring_application.service.UserService;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application.properties")
public class userServiceTest {

    @Autowired
    UserService userService;

    @Test
    @Order(1)
    void saveUserTest(){
        User user = new User();
        user.setFirstName("Andriy");
        user.setLastName("Khalak");
        user.setUsername("halk");
        user.setPassWord("halk95");

        User user1 = userService.insertUser(user);

        assertNotNull(user1);
        assertNotEquals(user.getPassword(), user1.getPassword());
        assertEquals(user1.getUsername(), "halk");
        assertTrue(user1.getUserId() > 0);
    }

    @Test
    @Order(2)
    void getUserByUserNameTest(){
        User user = userService.getUserByName("halk");
        assertNotNull(user);
        assertEquals(user.getFirstName(), "Andriy");
        assertTrue(user.getUserId() > 0);
    }

}
