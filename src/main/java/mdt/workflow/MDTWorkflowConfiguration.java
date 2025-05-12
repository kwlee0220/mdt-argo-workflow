package mdt.workflow;

import java.util.Map;

import org.hibernate.jpa.HibernatePersistenceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

import com.google.common.collect.Maps;

import lombok.Data;

import mdt.workflow.jpa.WorkflowPersistenceUnitInfo;

import jakarta.persistence.EntityManagerFactory;

/**
 *
 * @author Kang-Woo Lee (ETRI)
 */
//@Configuration
public class MDTWorkflowConfiguration {
	private static final Logger s_logger = LoggerFactory.getLogger(MDTWorkflowConfiguration.class);

	@Autowired private MDTWorkflowManagerConfiguration m_conf;
	@Autowired private JpaConfiguration m_jpaConf;
	
	@Bean
	WorkflowManager getWorkflowManager() {
		WorkflowModelManager modelMgr = new JpaWorkflowModelManager(getEntityManagerFactory());
		return new OpenApiArgoWorkflowManager(modelMgr, m_conf);
	}
	
	@Bean
	public EntityManagerFactory getEntityManagerFactory() {
		if ( s_logger.isInfoEnabled() ) {
			JdbcConfiguration jdbcConf = m_jpaConf.getJdbc();
			s_logger.info("JDBCConfiguration: url={}, user={}, password={}",
							jdbcConf.getUrl(), jdbcConf.getUser(), jdbcConf.getPassword());
			
			Map<String,String> jpaProps = m_jpaConf.getProperties();
			s_logger.info("JPA Properties: {}", jpaProps);
		}
		
//		return Persistence.createEntityManagerFactory("MDTInstanceManager");
		WorkflowPersistenceUnitInfo pUnitInfo = new WorkflowPersistenceUnitInfo(m_jpaConf);
		return new HibernatePersistenceProvider()
					.createContainerEntityManagerFactory(pUnitInfo, Maps.newHashMap());
	}

    @ConfigurationProperties(prefix = "jpa")
	@Data
	public static class JpaConfiguration {
		private JdbcConfiguration jdbc;
		private Map<String,String> properties;
	}

	@Data
	public static class JdbcConfiguration {
		private String url;
		private String user;
		private String password;
		
		public String toString() {
			return String.format("url=%s, user=%s", this.url, this.user);
		}
	}
}
