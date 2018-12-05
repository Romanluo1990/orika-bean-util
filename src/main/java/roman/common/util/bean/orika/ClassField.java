package roman.common.util.bean.orika;

import java.lang.reflect.Field;

/**
 *
 * @Auther: romanluo
 * @Date: 2018/12/3
 */
public class ClassField {

	private Class clazz;

	private Field field;

	public ClassField() {
	}

	public ClassField(Class clazz, Field field) {
		this.clazz = clazz;
		this.field = field;
	}

	public Class getClazz() {
		return clazz;
	}

	public void setClazz(Class clazz) {
		this.clazz = clazz;
	}

	public Field getField() {
		return field;
	}

	public void setField(Field field) {
		this.field = field;
	}
}
