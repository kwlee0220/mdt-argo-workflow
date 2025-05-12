package mdt.workflow;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;


/**
 *
 * @author Kang-Woo Lee (ETRI)
 */
@Configuration
@ConfigurationProperties(prefix = "workflow-manager")
@Data
public class MDTWorkflowManagerConfiguration {
	private String argoEndpoint;
	private String argoNamespace;
	private String mdtEndpoint;
	private String clientDockerImage;
}