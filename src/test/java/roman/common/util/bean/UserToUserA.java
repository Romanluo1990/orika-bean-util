package roman.common.util.bean;

import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MappingContext;

public class UserToUserA extends CustomMapper<User,UserA> {

    @Override
    public void mapAtoB(User user, UserA userA, MappingContext context) {
        userA.setId(999l);
        userA.setNameA("dd");
    }
}
