package roman.common.util.bean.test;

import roman.common.util.bean.BeanUtils;

public class TestGradle {
    public static void main(String[] args) {
        User user = new User();
        user.setId(123L);
        user.setName("2l");

        User user2 = new User();
        user2.setId(1234L);
        user2.setName("22l");
        user.setUser(user2);

        UserA userA = BeanUtils.map(user, UserA.class);
        BeanUtils.map(user, UserA.class);
        System.out.println(userA.toString());
    }
}
