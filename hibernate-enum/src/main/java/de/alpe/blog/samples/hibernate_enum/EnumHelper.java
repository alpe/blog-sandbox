package de.alpe.blog.samples.hibernate_enum;

import java.util.EnumSet;

import org.springframework.util.Assert;

/**
 * 
 * @author ap
 * 
 */
public abstract class EnumHelper {

	/**
	 * Marker interface to get id for persistence from implementing enum.
	 * 
	 * @author ap
	 * 
	 */
	public interface DBEnum {

		/**
		 * @return database id.
		 */
		int getId();
	}

	/**
	 * Get the type by given dbId.
	 * <p>
	 * Throws {@link IllegalArgumentException} if none found.
	 * 
	 * @param <T>
	 *            the return-type
	 * @param dbId
	 *            the dbId
	 * @param enumClass
	 *            enum class to fetch elements from
	 * @return the matching type
	 */
	public static <T extends Enum<T>> T findByDbId(int dbId, Class<T> enumClass) {
		Assert.isAssignable(DBEnum.class, enumClass, String.format(
				"Enum %s must implement the interface '%s'.", enumClass
						.getName(), DBEnum.class.getName()));
		for (T type : EnumSet.allOf(enumClass)) {
			if (((DBEnum) type).getId() == dbId) {
				return type;
			}
		}
		throw new IllegalArgumentException(String.format(
				"No type available for dbId=%s in types=%s", dbId, EnumSet
						.allOf(enumClass)));
	}

}
