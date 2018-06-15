package roman.common.util.bean.test;

import roman.common.util.bean.orika.annotation.FieldMap;
import roman.common.util.bean.orika.annotation.FieldMaps;

public class UserA {

    private Long id;

    @FieldMaps({
            @FieldMap(origClass = User.class,origField = "name")
    })
    private String nameA;

    private UserA user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNameA() {
        return nameA;
    }

    public void setNameA(String nameA) {
        this.nameA = nameA;
    }

    @Override
    public String toString() {
        return "roman.common.util.bean.test.UserA{" +
                "id=" + id +
                ", nameA='" + nameA + '\'' +
                '}';
    }

    public UserA getUser() {
        return user;
    }

    public void setUser(UserA user) {
        this.user = user;
    }
}
