package br.com.abreu.codenation.controller;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.abreu.codenation.service.CodenationService;

@RestController
public class CodenationController {

	@Autowired
	private CodenationService codenationService;

	@GetMapping
	public JSONObject Test() {
		return codenationService.getCodenationTest();
	}

}
