package de.alpe.blog.samples.hibernate_enum.testsupport;

import de.alpe.blog.samples.hibernate_enum.EnumHelper.DBEnum;

/**
 * Sample enum implementing the DBEnum interface to be stored by dbId.
 * 
 * @author ap
 * 
 */
public enum Salutation implements DBEnum {

	NONE(100), MR(110), MRS(102);

	private final int dbId;

	private Salutation(int dbId) {
		this.dbId = dbId;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getDbId() {
		return dbId;
	}

}
