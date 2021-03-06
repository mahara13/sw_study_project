package vmstudy.sw.personageshipping;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Profile;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import vmstudy.sw.db.models.Personage;
import vmstudy.sw.db.models.PersonageCall;
import vmstudy.sw.dto.PersonagesResponse;

@Component
@Profile("prod")
public class ProdPersonageShipping implements PersonageShipping , InitializingBean{
	
	private HttpComponentsClientHttpRequestFactory requestFactory;

	public List<Personage> getPersonages(PersonageCall lastCall) {
		RestTemplate restTemplate = new RestTemplate(requestFactory);
		List<Personage> result = new ArrayList<Personage>();
		String current_API_URL = lastCall.getUrl();
		
		PersonagesResponse response = null;
		boolean isProcessFails = false;
		while (current_API_URL != null) {
			try{
				response = restTemplate.getForObject(current_API_URL, PersonagesResponse.class);
			} catch(Exception e){
				isProcessFails = true;
			}
			
			if(isProcessFails) {
				lastCall.setUrl(current_API_URL);
				lastCall.incrementFailingCount();
				lastCall.incrementCallsCount();
				break;
			} else if (response != null)
			{
				lastCall.incrementCallsCount();
				current_API_URL = response.getNext();
				lastCall.setUrl(current_API_URL);
				result.addAll(response.getResults());
			}	
		} 
		
		if (isProcessFails == false) {
			lastCall.setFinishedStatus();
		}
		
		return result;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		CloseableHttpClient httpClient = HttpClients.custom()
                .setSSLHostnameVerifier(new NoopHostnameVerifier())
                .build();
		requestFactory = new HttpComponentsClientHttpRequestFactory();
		requestFactory.setHttpClient(httpClient);
	}

}
