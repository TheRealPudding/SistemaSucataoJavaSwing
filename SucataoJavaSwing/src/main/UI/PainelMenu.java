package main.UI;

import javax.swing.JPanel;
import javax.swing.JButton;

import java.awt.GridLayout;
import java.awt.GridBagLayout;
import java.awt.Dimension;

public class PainelMenu extends JPanel {

	public PainelMenu(TelaPrincipal janela) {
		this.setLayout(new GridBagLayout()); //para centralizar os botoes
		
		//criar os 4 botoes
		JButton btnCompra = new JButton("Registrar Compra de Materiais");
        JButton btnVenda = new JButton("Registrar Venda de Materiais");
        JButton btnCliente = new JButton("Registro de Cliente");
        JButton btnFuncionario = new JButton("Gerenciador de Funcionario");
        
        //colocar acao nos botoes
        btnFuncionario.addActionListener(e -> janela.MudarTelas("FUNCIONARIO"));
        btnCliente.addActionListener(e -> janela.MudarTelas("CLIENTE"));
        
        //Fazer Grid para 4 botoes (2 por 2) com espaco de 10 pixels
		JPanel GridBotao = new JPanel(new GridLayout(2, 2, 10, 10));
		GridBotao.add(btnCompra);
		GridBotao.add(btnVenda);
		GridBotao.add(btnCliente);
		GridBotao.add(btnFuncionario);
		
		GridBotao.setPreferredSize(new Dimension(460, 150)); //Ajustar tamanho dos botoes
		
		add(GridBotao);
	}
}
