package br.com.abreu.codenation.service.impl;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.codec.digest.DigestUtils;
import org.json.simple.JSONObject;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.abreu.codenation.service.CodenationService;
import br.com.abreu.codenation.utils.CaesarChiper;

@Service
public class CodenationServiceImpl implements CodenationService {

	private final static String MY_TOKEN = "1a76bfa8576414fe8cd0db836fea4b71c3e67e67";

	private final static String URI_CONDENATION = "https://api.codenation.dev/v1/challenge/dev-ps/generate-data?token=";

	private final static String URI_SEND_FILE = "https://api.codenation.dev/v1/challenge/dev-ps/submit-solution?token=";

	private final static String FILE_NAME = "answer.json";

	private final RestTemplate restTemplate;

	public CodenationServiceImpl(RestTemplateBuilder restTemplateBuilder) {
		this.restTemplate = restTemplateBuilder.build();
	}

	@Override
	public JSONObject getCodenationTest() {
		JSONObject obj = this.getCodenationResponse();

		this.writeFile(obj);

		String dechiperValue = CaesarChiper.dechiper(obj.get("cifrado").toString(), (int) obj.get("numero_casas"));
		obj.replace("decifrado", dechiperValue);
		obj.replace("resumo_criptografico", DigestUtils.sha1Hex(dechiperValue));

		this.writeFile(obj);
		this.sendFile();
		return obj;

	}

	private void sendFile() {
		Path file = Paths.get(FILE_NAME);

		LinkedMultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
		body.add("answer", file);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		headers.set("Accept", "application/json");

		HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new HttpEntity<LinkedMultiValueMap<String, Object>>(
				body, headers);
		ResponseEntity<String> result = restTemplate.exchange(URI_SEND_FILE + MY_TOKEN, HttpMethod.POST, requestEntity,
				String.class);
	}

	private void writeFile(JSONObject object) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME));
			writer.write(object.toJSONString());
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private JSONObject getCodenationResponse() {
		JSONObject response = null;
		ResponseEntity<String> result = restTemplate.exchange(URI_CONDENATION + MY_TOKEN, HttpMethod.GET, null,
				String.class);

		if (result.getStatusCode().equals(HttpStatus.OK)) {
			try {
				response = new ObjectMapper().readValue(result.getBody(), JSONObject.class);
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return response;
	}

}
