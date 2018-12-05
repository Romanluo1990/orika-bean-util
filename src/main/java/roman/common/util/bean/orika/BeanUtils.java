package roman.common.util.bean.orika;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class BeanUtils {

	private BeanUtils() {
	}

	private static BeanUtilsBean getInstance() {
		return BeanUtilsHolder.INSTANCE;
	}

	public static <S, D> D map(S orig, Class<D> destClass) {
		return BeanUtils.getInstance().map(orig, destClass);
	}

	public static <S, D> void copyProperties(S orig, D dest) {
		BeanUtils.getInstance().copyProperties(orig, dest);
	}

	public static <S> Map<String, Object> describe(S orig) {
		return BeanUtils.getInstance().map(orig, Map.class);
	}

	public static <S, D> List<D> mapAsList(Iterable<S> origs, Class<D> destClass) {
		return BeanUtils.getInstance().mapAsList(origs, destClass);
	}

	public static <S, D> void mapToCollection(Iterable<S> origs, Class<D> destClass,
			Collection<D> collection) {
		BeanUtils.getInstance().mapToCollection(origs, destClass, collection);
	}

	private static class BeanUtilsHolder {
		private static final BeanUtilsBean INSTANCE = new BeanUtilsBean();
	}

}
