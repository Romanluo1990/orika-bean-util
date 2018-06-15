package roman.common.util.bean.test;

import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MappingContext;

public class UserAToUser extends CustomMapper<User,UserA> {

    @Override
    public void mapAtoB(User user, UserA userA, MappingContext context) {
        userA.setId(999l);
        userA.setNameA("dd");
    }
}
