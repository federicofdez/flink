package org.apache.flink.streaming.connectors.smartsantander;

/**
 * URIs of the resources in Smart Santander API
 *
 * @author federicofdez
 */
public enum SmartSantanderAPIEndpoints {
	TRAFFIC("http://datos.santander.es/api/rest/datasets/mediciones"),
	AIR_QUALITY("http://datos.santander.es/api/rest/datasets/sensores_smart_mobile"),
	ENVIRONMENT("http://datos.santander.es/api/rest/datasets/sensores_smart_env_monitoring");

	private String url;

	private SmartSantanderAPIEndpoints(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}

}
