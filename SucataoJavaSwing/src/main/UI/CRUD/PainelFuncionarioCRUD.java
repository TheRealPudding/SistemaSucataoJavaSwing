package main.UI.CRUD;

import java.util.List;
import java.awt.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import main.UI.TelaPrincipal;
import main.models.Funcionario;
import main.Database.HandlerBD;

public class PainelFuncionarioCRUD extends JPanel {
	private JTable table;
	private DefaultTableModel tableModelo;
	private JTextField txtNome, txtEmail; // txtCargo;
	private JComboBox<String> cbCargo;
    private JButton btnAdd, btnAtt, btnDel, btnVolta;
    
    private List<Funcionario> listaMemoria;
    
    public PainelFuncionarioCRUD(TelaPrincipal janela) {
    	// --- TABELA E ESTILO ---
    	setLayout(new BorderLayout(10,10));
    	
    	//NORTE - Titulo e Header
    	JLabel lblTitulo = new JLabel("Gerenciador de Funcionarios", JLabel.CENTER);
    	lblTitulo.setFont(new Font(Font.SERIF, Font.BOLD, 18)); // definicoes da fonte do titulo
        add(lblTitulo, BorderLayout.NORTH); // onde vai ficar a variavel lblTitulo
        
        //CENTRO - Lista de funcionarios com rollbars 
        String[] columns = {"ID", "Nome", "E-mail", "Cargo"};
        tableModelo = new DefaultTableModel(columns, 0) { // definir a tabela como "Colunas"
        	@Override
        	public boolean isCellEditable(int row, int column) {
                return false; // Nao deixar o usuario editar as celulas diretamente
            }
        };
        table = new JTable(tableModelo);
        JScrollPane scrollPane = new JScrollPane(table); // adicionar a rollbar
        add(scrollPane, BorderLayout.CENTER);
        
        //LESTE - Controle do CRUD, Onde vao ficar os controles em si
        JPanel PainelForm = new JPanel(new GridLayout(0, 1, 5, 5));
        PainelForm.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        
        // definir o tamanho do menu de controle do CRUD
        PainelForm.setMinimumSize(new Dimension(200, 0)); 
        PainelForm.setPreferredSize(new Dimension(250, 0));
        
        // definir o tamanho das variaveis
        txtNome = new JTextField(63);
        txtEmail = new JTextField(127);
        // txtCargo = new JTextField(31);
        
        String[] cargosPredefinidos = { 
        		"Selecione...", "Gerente", "Operador de Pátio", "Recepcionista", "Administrativo" 
        		};
        cbCargo = new JComboBox<>(cargosPredefinidos);
        
        // definir os botoes do menu de controle
        btnAdd = new JButton("Adicionar");
        btnAtt = new JButton("Atualizar");
        btnDel = new JButton("Deletar");
        btnVolta = new JButton("← Menu Principal");
        
        PainelForm.add(new JLabel("Nome Completo:"));
        PainelForm.add(txtNome);
        PainelForm.add(new JLabel("E-mail:"));
        PainelForm.add(txtEmail);
        PainelForm.add(new JLabel("Cargo do Funcionario:"));
        PainelForm.add(cbCargo); //PainelForm.add(txtCargo)
        
        JPanel PainelBotoesAcao = new JPanel(new GridLayout(1, 3, 5, 5));
        PainelBotoesAcao.add(btnAdd);
        PainelBotoesAcao.add(btnAtt);
        PainelBotoesAcao.add(btnDel);
        PainelForm.add(PainelBotoesAcao);
        
        PainelForm.add(btnVolta);
        
        add(PainelForm, BorderLayout.EAST);
        
        // --- FUNCIONALIDADES ---
        btnVolta.addActionListener(e -> janela.MudarTelas("MENU"));
        atualizarTableGeral(); // resetar a tabela toda vez que iniciar o programa
        
        // ADICIONAR FUNCIONARIO
        btnAdd.addActionListener(e -> {
        	String nome = txtNome.getText().trim();
            String email = txtEmail.getText().trim();
            String cargo = cbCargo.getSelectedItem().toString(); // txtCargo.getText().trim();
            
            if (nome.isEmpty() || email.isEmpty() || cargo.isEmpty()) { // Garantir que todos os campos estejam preenchidos
                JOptionPane.showMessageDialog(this, "Por favor, preencha todos os campos solicitados.");
                return;
            } 
            
            if (cargo.equals("Selecione...")) { // para o usuario nao poder usar a opcao "Selecione..."
                JOptionPane.showMessageDialog(this, "Por favor, selecione um cargo válido!");
                return;
            }
            
            int arroba = email.indexOf("@");
            if (arroba == -1 || email.indexOf(".", arroba) == -1) { // checar de se o email e um email
            	JOptionPane.showMessageDialog(this, 
            	        "Endereço de e-mail inválido!\nCertifique-se de que possui '@' e um domínio (ex: .com).", 
            	        "Erro de Validação", 
            	        JOptionPane.WARNING_MESSAGE);
            	    return;
            }
            
            try {
                // Tentar Criar um novo objeto Funcionario e mandar direto pra tabela
                Funcionario novo = new Funcionario(nome, email, cargo);
                HandlerBD.adicionarFuncionario(novo);
                
                // Atualizar a tabela de nomes novamente
                atualizarTableGeral();
                limparFormulario();
                JOptionPane.showMessageDialog(this, "Funcionário adicionado com sucesso!");
            } catch (RuntimeException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro de Banco de Dados", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        // REMOVER FUNCIONARIO
        btnDel.addActionListener(e -> {
        	int linhaSelecionada = table.getSelectedRow();
        	if (linhaSelecionada == -1) {
                JOptionPane.showMessageDialog(this, "Selecione um funcionário na tabela para deletar.");
                return;
            }
            
            int confirmacao = JOptionPane.showConfirmDialog(this, 
                    "Tem certeza que deseja remover este funcionário?", "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);
            
            if (confirmacao == JOptionPane.YES_OPTION) {
                try {
                    // Remover a linha selecionada do programa
                    listaMemoria.remove(linhaSelecionada);
                    
                    // Salvar a lista por cima da antiga no json
                    HandlerBD.salvarFuncionarios(listaMemoria);
                    
                    // Atualizar a tabela de nomes novamente
                    atualizarTableGeral();
                    limparFormulario();
                    JOptionPane.showMessageDialog(this, "Funcionário removido!");
                } catch (RuntimeException ex) {
                    JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro de Banco de Dados", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
    
    // --- METODOS AUXILIARES ---
    private void limparFormulario() { // Para limpar as caixas de texto que o usuario pode escrever
        txtNome.setText("");
        txtEmail.setText("");
        cbCargo.setSelectedIndex(0); // txtCargo.setText("");
    }
    private void atualizarTableGeral() {
        tableModelo.setRowCount(0); // Limpar a JTable visalmente
        listaMemoria = HandlerBD.getFuncionarios(); // Pegar a lista atualizada do JSON
        
        // Popular a lista atualizada na JTable
        for (Funcionario f : listaMemoria) {
            tableModelo.addRow(new Object[]{ f.getId(), f.getNome(), f.getEmail(), f.getCargo() });
        }
    }
}
