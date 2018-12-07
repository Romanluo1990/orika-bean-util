# orika-bean-util
基于orika的BeanUtil工具包
---
支持注解映射  
@DestFieldMap 目标对象字段映射（用在目标对象上）  
@OrigFieldMap 源对象字段映射（用在源对象上）  

## example
```
@Data
public class UserA {

    private Long id;

    //多映射
    @DestFieldMaps({
            @DestFieldMap(origClass = User.class,origField = "name")
    })
    private String nameA;

    @DestFieldMap(origClass = User.class,origField = "name")
    private String nameB;

    private String nameC;

}

@Data
public class User {

    private Long id;

    @OrigFieldMap(destClass = UserA.class, destField = "nameC")
    private String name;

}

@Test
public void mapTest(){
    User user = new User();
    user.setId(111L);
    user.setName("张三");

    UserA userA = BeanUtils.map(user, UserA.class);
    System.out.println(userA.toString());
}
```
reslut:
```
UserA(id=111, nameA=张三, nameB=张三, nameC=张三)
```
