package main.Database;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import main.models.Funcionario;
import main.models.Cliente;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class HandlerBD {
	private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
	
	// METODOS PARA FUNCIONARIOS
	public static List<Funcionario> getFuncionarios() {
        File file = new File("funcionarios.json"); // definir que existe um arquivo json e que ele vai ser lido nesse codigo.
        if (!file.exists()) return new ArrayList<>(); // se o arquivo nao existe, cria um novo.
        
        try (Reader reader = new FileReader(file)) { // tentar ler o arquivo funcionarios.json
            Type listType = new TypeToken<ArrayList<Funcionario>>(){}.getType(); 
            List<Funcionario> list = gson.fromJson(reader, listType);
            return list != null ? list : new ArrayList<>();
            //Info importante (para lembrar): esse TypeToken e um truque interessante do Gson que remove uma limitacao do java
            //Basicamente ele forca o java a lembrar que essa e uma lista <funcionarios> ao inves de so lembrar que e uma lista
            //E isso deixa o pacote Gson ler a lista e da mais opcoes pro usuario manipular o array mais facil
        } 
        catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
	public static void salvarFuncionarios(List<Funcionario> lista) {
        try (Writer writer = new FileWriter("funcionarios.json")) { //tentar definir que o arquivo json vai ser sobreescrito
            gson.toJson(lista, writer); //salvar o arquivo json
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Falha ao salvar no arquivo: " + e.getMessage());
        }
    }

    public static void adicionarFuncionario(Funcionario f) {
    	List<Funcionario> lista = getFuncionarios();
    	lista.add(f);
    	salvarFuncionarios(lista);
    }
    
    // METODOS PARA CLIENTES
    public static List<Cliente> getClientes() {
        File file = new File("clientes.json");
        if (!file.exists()) return new ArrayList<>();
        
        try (Reader reader = new FileReader(file)) {
            // Notice the generic token is configured specifically for Cliente!
            Type listType = new TypeToken<ArrayList<Cliente>>(){}.getType();
            List<Cliente> list = gson.fromJson(reader, listType);
            return list != null ? list : new ArrayList<>();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Não foi possível ler o arquivo de clientes.");
        }
    }

    public static void salvarClientes(List<Cliente> lista) {
        try (Writer writer = new FileWriter("clientes.json")) {
            gson.toJson(lista, writer);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Não foi possível salvar os dados no arquivo de clientes.");
        }
    }

    public static void adicionarCliente(Cliente c) {
        List<Cliente> lista = getClientes();
        lista.add(c);
        salvarClientes(lista);
    }
}
