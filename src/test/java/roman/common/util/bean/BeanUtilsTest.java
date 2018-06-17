package roman.common.util.bean;

import org.junit.Test;

public class BeanUtilsTest {

    @Test
    public void mapTest(){
        User user = new User();
        user.setId(111L);
        user.setName("张三");

        UserA userA = BeanUtils.map(user, UserA.class);
        System.out.println(userA.toString());
    }
}
