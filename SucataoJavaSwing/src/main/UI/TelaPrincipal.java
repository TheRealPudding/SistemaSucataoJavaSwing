package main.UI;

import main.UI.PainelMenu;
import main.UI.CRUD.*;

import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.CardLayout;
import java.awt.Dimension;

public class TelaPrincipal extends JFrame {
	
	private CardLayout LayoutCard;
	private JPanel Painel;
	
	public TelaPrincipal() {
		setTitle("Sistema Sucatao");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // para o programa/tela fechar quando voce clica no X
		setLocationRelativeTo(null); // centralizar a tela na tela no inicio
		
		setSize(1024, 768); // tamanho da tela
		this.setResizable(true); // para fazer ficar redimensionavel
		this.setMinimumSize(new Dimension(800, 600)); //tamanho minimo da tela, para a Interface nao quebrar.
		
		LayoutCard = new CardLayout();
		Painel = new JPanel(LayoutCard);
		
		// Instanciar as views (Telas que vao ser usadas)
		PainelMenu MenuPrincipal = new PainelMenu(this);
		PainelFuncionarioCRUD MenuFuncionario = new PainelFuncionarioCRUD(this);
		PainelClienteCRUD MenuCliente = new PainelClienteCRUD(this);
		
		// Adicionar as views no Layout do Painel
		Painel.add(MenuPrincipal, "MENU");
		Painel.add(MenuFuncionario, "FUNCIONARIO");
		Painel.add(MenuCliente, "CLIENTE");
		
		// Fazer o Painel visivel na tela (main.java)
		add(Painel);
		
		// Fazer o menu ser a tela inicial
		LayoutCard.show(Painel, "MENU");
	}
	
	public void MudarTelas(String NomeTela) {
		LayoutCard.show(Painel, NomeTela);
	}
}
