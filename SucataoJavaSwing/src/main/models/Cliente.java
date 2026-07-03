package main.models;

import java.util.UUID;

public class Cliente {
	private String id;
	private String idgov; // String para ID do governo (CPF, RG, CNH, CNPJ)
	private String nome;
	private String email;
	private long telefone;
	private String tipo; // Se e um Comprador, Vendedor ou Ambos (Comprador E Vendedor)
	
	public Cliente() { // Instanciador do metodo. Obrigado java...
	}
	
	public Cliente(String nome, String email, String tipo, String idgov, long telefone) {
		this.id = UUID.randomUUID().toString().substring(0, 8).toUpperCase(); //gera uma ID aleatoria de 8 digitos
		this.idgov = idgov;
		this.nome = nome;
		this.email = email;
		this.telefone = telefone;
		this.tipo = tipo;
	}
	
	public String getId() {return id;}
	
	public String getIdgov() {return idgov;}
	public void setIdgov(String idgov) {this.idgov = idgov;}
	
	public String getNome() {return nome;}
	public void setNome(String nome) {this.nome = nome;}
	
	public String getEmail() {return email;}
	public void setEmail(String email) {this.email = email;}
	
	public long getTelefone() {return telefone;}
	public void setTelefone(long telefone) {this.telefone = telefone;}
	
	public String getTipo() {return tipo;}
	public void setTipo(String tipo) {this.tipo = tipo;}
}
