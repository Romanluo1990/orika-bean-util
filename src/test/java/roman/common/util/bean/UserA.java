package roman.common.util.bean;

import roman.common.util.bean.orika.annotation.FieldMap;
import roman.common.util.bean.orika.annotation.FieldMaps;

public class UserA {

    private Long id;

    @FieldMaps({
            @FieldMap(origClass = User.class,origField = "name")
    })
    private String nameA;

    @FieldMap(origClass = User.class,origField = "name")
    private String nameB;

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

    public String getNameB() {
        return nameB;
    }

    public void setNameB(String nameB) {
        this.nameB = nameB;
    }

    @Override
    public String toString() {
        return "roman.common.util.bean.BeanUtilsTest.UserA{" +
                "id=" + id +
                ", nameA='" + nameA + '\'' +
                ", nameB='" + nameB + '\'' +
                '}';
    }
}
