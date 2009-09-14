package de.alpe.blog.samples.hibernate_enum;

import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.config.java.annotation.Bean;
import org.springframework.config.java.annotation.Configuration;
import org.springframework.config.java.annotation.Lazy;
import org.springframework.config.java.support.ConfigurationSupport;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.TypeDefinitionBean;
import org.springframework.orm.hibernate3.annotation.AnnotationSessionFactoryBean;

import de.alpe.blog.samples.hibernate_enum.testsupport.SimplePerson;

/**
 * @author Alex Peters
 * 
 */
@Configuration(defaultAutowire = Autowire.BY_TYPE, defaultLazy = Lazy.FALSE)
public class HibernateConfiguration extends ConfigurationSupport {

	private static final String BEAN_DATA_SOURCE = "dataSource";

	private static final String BEAN_HIBERNATE_PROPERTIES = "hibernateProperties";

	/**
	 * Annotated entity classes.
	 */
	private static final Class<?>[] ANNOTATED_CLASSES = { SimplePerson.class };

	@Bean
	public SessionFactory sessionFactory() throws Exception {
		AnnotationSessionFactoryBean sessionFactoryBean = initAnnotationSessionFactoryBean();
		sessionFactoryBean
				.setDataSource((DataSource) getBean(BEAN_DATA_SOURCE));
		sessionFactoryBean
				.setHibernateProperties((Properties) getBean(BEAN_HIBERNATE_PROPERTIES));

		sessionFactoryBean.afterPropertiesSet();
		SessionFactory sessionFactory = (SessionFactory) sessionFactoryBean
				.getObject();
		return sessionFactory;
	}

	protected Class<?>[] getAnnotatedClasses() {
		return ANNOTATED_CLASSES;
	}

	protected AnnotationSessionFactoryBean initAnnotationSessionFactoryBean()
			throws Exception {
		AnnotationSessionFactoryBean sessionFactoryBean = new AnnotationSessionFactoryBean();
		sessionFactoryBean.setTypeDefinitions(getUserTypeDefinitions());
		sessionFactoryBean.setAnnotatedClasses(getAnnotatedClasses());
		return sessionFactoryBean;
	}

	public TypeDefinitionBean[] getUserTypeDefinitions() {
		TypeDefinitionBean[] typeDefinitions = new TypeDefinitionBean[1];
		typeDefinitions[0] = new TypeDefinitionBean();
		typeDefinitions[0].setTypeName(org.hibernate.type.EnumType.class
				.getName());
		typeDefinitions[0].setTypeClass(DbIdAwareEnumType.class.getName());
		return typeDefinitions;
	}

	@Bean(scope = BeanDefinition.SCOPE_PROTOTYPE)
	public HibernateTemplate hibernateTemplate() throws Exception {
		HibernateTemplate hibernateTemplate = new HibernateTemplate();
		hibernateTemplate.setSessionFactory(sessionFactory());
		return hibernateTemplate;
	}
}
