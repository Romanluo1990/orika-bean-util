package roman.common.util.bean;

import roman.common.util.bean.orika.annotation.DestFieldMap;
import roman.common.util.bean.orika.annotation.DestFieldMaps;

public class UserA {

    private Long id;

    @DestFieldMaps({
            @DestFieldMap(origClass = User.class,origField = "name")
    })
    private String nameA;

    @DestFieldMap(origClass = User.class,origField = "name")
    private String nameB;

	private String nameC;

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

	public String getNameC() {
		return nameC;
	}

	public void setNameC(String nameC) {
		this.nameC = nameC;
	}

	@Override
	public String toString() {
		return "UserA{" +
				"id=" + id +
				", nameA='" + nameA + '\'' +
				", nameB='" + nameB + '\'' +
				", nameC='" + nameC + '\'' +
				'}';
	}
}
