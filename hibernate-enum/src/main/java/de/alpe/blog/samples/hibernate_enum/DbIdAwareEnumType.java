package de.alpe.blog.samples.hibernate_enum;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.hibernate.type.EnumType;

import de.alpe.blog.samples.hibernate_enum.EnumHelper.DBEnum;

/**
 * Hibernate specific {@link org.hibernate.usertype.UserType} implementation to
 * map id aware {@link DBEnum} values from and into the database using their
 * dedicated DbId and NOT a name() or ordinal value.
 * <p>
 * To enable DbIdAwareEnumType mapping add a
 * {@link org.springframework.orm.hibernate3.TypeDefinitionBean} when
 * initializing the sessionFactory like this:
 * 
 * <pre>
 * TypeDefinitionBean[] typeDefinitions = new TypeDefinitionBean[1];
 * typeDefinitions[0] = new TypeDefinitionBean();
 * typeDefinitions[0].setTypeName(org.hibernate.type.EnumType.class.getName());
 * typeDefinitions[0].setTypeClass(DbIdAwareEnumType.class.getName());
 * sessionFactoryBean.setTypeDefinitions(getUserTypeDefinitions());
 * </pre>
 * 
 * </p>
 * 
 * @author ap
 */
public class DbIdAwareEnumType extends EnumType {

	private static final long serialVersionUID = -8755915397973668651L;

	/**
	 * Default constructor.
	 */
	public DbIdAwareEnumType() {
		super();
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Object nullSafeGet(final ResultSet rs, final String[] names,
			final Object owner) throws SQLException {
		final Object object = rs.getInt(names[0]);
		if (rs.wasNull()) {
			return null;
		}
		final Class<?> enumClass = returnedClass();
		if (!DBEnum.class.isAssignableFrom(enumClass)) {
			return super.nullSafeGet(rs, names, owner);
		}
		if (!(object instanceof Number)) {
			throw new IllegalStateException(
					"Loaded value must be of any Number type but was: "
							+ object);
		}
		try {
			final int enumDbId = ((Number) object).intValue();
			return EnumHelper.findByDbId(enumDbId, (Class<Enum>) enumClass);
		} catch (final Exception e) {
			throw new IllegalStateException("Failed to fetch DB id for '"
					+ object + "' of " + enumClass, e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void nullSafeSet(final PreparedStatement st, final Object value,
			final int index) throws SQLException {
		if (value == null) {
			super.nullSafeSet(st, value, index);
			return;

		}
		final Class<?> enumClass = returnedClass();
		if (!DBEnum.class.isAssignableFrom(enumClass)) {
			super.nullSafeSet(st, value, index);
			return;
		}
		st.setObject(index, ((DBEnum) value).getDbId(), Types.INTEGER);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String objectToSQLString(final Object value) {
		throw new AssertionError("Not implemented, yet!");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object fromXMLString(final String xmlValue) {
		throw new AssertionError("Not implemented, yet!");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toXMLString(final Object value) {
		throw new AssertionError("Not implemented, yet!");
	}

}