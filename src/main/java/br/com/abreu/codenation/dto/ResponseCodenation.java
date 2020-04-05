package br.com.abreu.codenation.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Setter
@Getter
@JsonInclude(Include.NON_EMPTY)
public class ResponseCodenation {

	private Integer numero_casas;
	private String token;
	private String cifrado;
	private String decifrado;
	private String resumo_criptografico;
}
