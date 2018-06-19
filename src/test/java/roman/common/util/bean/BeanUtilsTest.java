package roman.common.util.bean;

import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.junit.Test;

public class BeanUtilsTest {

    @Test
    public void mapTest(){
        User user = new User();
        user.setId(111L);
        user.setName("张三");

        UserA userA = BeanUtils.map(user, UserA.class);
        System.out.println(userA.toString());
//
//        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
//        mapperFactory.classMap(User.class, UserA.class)
//                .field("name","nameB")
//                .byDefault()
//                .register();

//        System.out.println(mapperFactory.getMapperFacade().map(user,UserA.class));
    }
}
