package roman.common.util.bean.orika;

import ma.glasnost.orika.Converter;

/**
 *
 * @Auther: romanluo
 * @Date: 2018/9/11
 */
public class FieldMap {

	private Class<?> destClass;

	private String destField;

	private Class<?> origClass;

	private String origField;

	private Class<? extends Converter<?, ?>> converter;

	private boolean mapNulls;

	private boolean exclude;

	private boolean byDefault;

	public Class<?> getDestClass() {
		return destClass;
	}

	public void setDestClass(Class<?> destClass) {
		this.destClass = destClass;
	}

	public String getDestField() {
		return destField;
	}

	public void setDestField(String destField) {
		this.destField = destField;
	}

	public Class<?> getOrigClass() {
		return origClass;
	}

	public void setOrigClass(Class<?> origClass) {
		this.origClass = origClass;
	}

	public String getOrigField() {
		return origField;
	}

	public void setOrigField(String origField) {
		this.origField = origField;
	}

	public Class<? extends Converter<?, ?>> getConverter() {
		return converter;
	}

	public void setConverter(Class<? extends Converter<?, ?>> converter) {
		this.converter = converter;
	}

	public boolean isMapNulls() {
		return mapNulls;
	}

	public void setMapNulls(boolean mapNulls) {
		this.mapNulls = mapNulls;
	}

	public boolean isExclude() {
		return exclude;
	}

	public void setExclude(boolean exclude) {
		this.exclude = exclude;
	}

	public boolean isByDefault() {
		return byDefault;
	}

	public void setByDefault(boolean byDefault) {
		this.byDefault = byDefault;
	}
}
