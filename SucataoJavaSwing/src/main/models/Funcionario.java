package main.models;

import java.util.UUID;

public class Funcionario {
	private String id;
	private String nome;
	private String email;
	private String cargo;
	
	public Funcionario() { // Instanciador do metodo. Obrigado java...
	}
	
	public Funcionario(String nome, String email, String cargo) {
		this.id = UUID.randomUUID().toString().substring(0, 8).toUpperCase(); //gera uma ID aleatoria de 8 digitos
		this.nome = nome;
		this.email = email;
		this.cargo = cargo;
	}
	
	public String getId() {return id;}
	
	public String getNome() {return nome;}
	public void setNome(String nome) {this.nome = nome;}
	
	public String getEmail() {return email;}
	public void setEmail(String email) {this.email = email;}
	
	public String getCargo() {return cargo;}
	public void setCargo(String cargo) {this.cargo = cargo;}
}
