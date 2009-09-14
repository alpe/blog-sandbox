package de.alpe.blog.samples.hibernate_enum;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import de.alpe.blog.samples.hibernate_enum.testsupport.Salutation;
import de.alpe.blog.samples.hibernate_enum.testsupport.SimplePerson;
import de.alpe.blog.samples.hibernate_enum.testsupport.Title;

/**
 * @author Alex Peters
 * 
 */
@ContextConfiguration(locations = "/applicationContext.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class DbIdAwareEnumFunctionalTest extends
		AbstractTransactionalJUnit4SpringContextTests {

	static final String NAME_FOO = "NAME_FOO";

	static final String NAME_BAR = "NAME_BAR";

	@Autowired
	HibernateTemplate template;

	@Transactional
	@Before
	public void prepareTestdata() {
		deleteFromTables(SimplePerson.TABLE_NAME);
		template.save(new SimplePerson(NAME_FOO, Salutation.MR, Title.KING));
		template.save(new SimplePerson(NAME_BAR, Salutation.MRS, Title.NONE));
	}

	@Test
	public void storeWasSuccessfull() throws Exception {
		assertThat(countRowsInTable(SimplePerson.TABLE_NAME), is(2));
	}

	@Test
	@Transactional
	public void findWithNamedQuery_matchingResultFound() throws Exception {
		List<?> result = template.findByNamedQueryAndNamedParam(
				SimplePerson.QUERY_FIMD_BY_SALUTATION, "salutation",
				Salutation.MR);
		assertThat(result.size(), is(1));
		SimplePerson p = (SimplePerson) result.get(0);
		assertThat(p.getName(), is(NAME_FOO));
	}

	@Test
	@Transactional
	public void findWithNamedQuery_noMatchingResult() throws Exception {
		List<?> result = template.findByNamedQueryAndNamedParam(
				SimplePerson.QUERY_FIMD_BY_SALUTATION, "salutation",
				Salutation.NONE);
		assertThat(result.size(), is(0));
	}

	@Test
	@Transactional
	public void findWithCriteria_matchingResultFound() throws Exception {
		List<?> result = template.findByCriteria(DetachedCriteria.forClass(
				SimplePerson.class).add(
				Restrictions.eq("salutation", Salutation.MRS)));
		assertThat(result.size(), is(1));
		SimplePerson p = (SimplePerson) result.get(0);
		assertThat(p.getName(), is(NAME_BAR));
	}

	@Test
	@Transactional
	public void findWithSQLAndDBId_matchingResultFound() throws Exception {
		List<Map<String, Object>> result = simpleJdbcTemplate.queryForList(
				"select name from " + SimplePerson.TABLE_NAME
						+ " where salutation=?", Salutation.MRS.getDbId());
		assertThat(result.size(), is(1));
		assertThat(result.get(0).get("name"), is((Object) NAME_BAR));
	}

	@Test
	@Transactional
	public void findWithSQLAndOrdinal_noMatchingResult() throws Exception {
		List<Map<String, Object>> result = simpleJdbcTemplate.queryForList(
				"select name from " + SimplePerson.TABLE_NAME
						+ " where salutation=?", Salutation.MRS.ordinal());
		assertThat(result.size(), is(0));
	}

	@Test
	@Transactional
	public void findClassicEnum_withSQLAndOrdinal_matchingResultFound()
			throws Exception {
		List<Map<String, Object>> result = simpleJdbcTemplate.queryForList(
				"select name from " + SimplePerson.TABLE_NAME
						+ " where title=?", Title.KING.ordinal());
		assertThat(result.size(), is(1));
		assertThat(result.get(0).get("name"), is((Object) NAME_FOO));
	}

}
