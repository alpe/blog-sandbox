package de.alpe.blog.samples.hibernate_enum.testsupport;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Simple entity for testing.
 * 
 * @author ap
 * 
 */
@NamedQuery(name = SimplePerson.QUERY_FIMD_BY_SALUTATION, query = "from de.alpe.blog.samples.hibernate_enum.testsupport.SimplePerson where salutation =:salutation")
@Entity(name = SimplePerson.TABLE_NAME)
public class SimplePerson {

	public static final String TABLE_NAME = "SimplePerson";

	public static final String QUERY_FIMD_BY_SALUTATION = "SimplePerson.salutation";

	@Id
	@GeneratedValue
	@Column(name = "id")
	private Long id;

	private String name;

	// dbEnum type. will be mapped by dbId value
	private Salutation salutation;

	// common enum type. will be mapped by ordinal
	@Enumerated(EnumType.ORDINAL)
	private Title title;

	/** ORM constructor. */
	@SuppressWarnings("unused")
	private SimplePerson() {
	}

	public SimplePerson(String name, Salutation salutation, Title title) {
		super();
		this.name = name;
		this.salutation = salutation;
		this.title = title;
	}

	public String getName() {
		return name;
	}

	public Salutation getSalutation() {
		return salutation;
	}

	public void setSalutation(Salutation salutation) {
		this.salutation = salutation;
	}

	public Title getTitle() {
		return title;
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this, new String[] { "id" });
	}

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj, new String[] { "id" });
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
