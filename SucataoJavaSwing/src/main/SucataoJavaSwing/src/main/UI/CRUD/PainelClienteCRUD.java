package main.UI.CRUD;

import java.util.List;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import main.UI.TelaPrincipal;
import main.models.Cliente;
import main.Database.HandlerBD;

public class PainelClienteCRUD extends JPanel {
    private JTable table;
    private DefaultTableModel tableModelo;
    private JTextField txtNome, txtEmail, txtIdGov, txtTelefone;
    private JComboBox<String> cbTipo;
    private JButton btnAdd, btnAtt, btnDel, btnVolta;
    
    private List<Cliente> listaMemoria;
    private int indexSendoEditado = -1;
    
    public PainelClienteCRUD(TelaPrincipal janela) {
        setLayout(new BorderLayout(10,10));
        
        // NORTE - Titulo
        JLabel lblTitulo = new JLabel("Gerenciador de Clientes", JLabel.CENTER);
        lblTitulo.setFont(new Font(Font.SERIF, Font.BOLD, 18));
        add(lblTitulo, BorderLayout.NORTH);
        
        // CENTRO - Lista de Clientes (Tabela)
        String[] columns = {"ID", "Doc. Gov", "Nome", "E-mail", "Telefone", "Tipo"};
        tableModelo = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModelo);
        JScrollPane scrollTabela = new JScrollPane(table);
        add(scrollTabela, BorderLayout.CENTER);
        
        // LESTE - Painel Mestre da Direita (Formulário + Botões)
        JPanel painelMestreDireita = new JPanel(new BorderLayout(5, 5));
        painelMestreDireita.setPreferredSize(new Dimension(280, 0));
        
        // 1. Painel de Inputs (Campos de Texto) - Configurado para rolar se faltar espaço
        JPanel painelInputs = new JPanel(new GridLayout(0, 1, 5, 5));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL; // Force inputs to match the width of the column
        gbc.weightx = 1.0;                        // Stretch across the available width evenly
        gbc.gridx = 0;                            // Single column layout
        gbc.gridy = 0;                            // Row tracker
        gbc.insets = new Insets(0, 0, 8, 0);       // Clean 8px padding under every block
        
        txtNome = new JTextField(63);
        txtEmail = new JTextField(127);
        txtIdGov = new JTextField(20);
        txtTelefone = new JTextField(15); // Campo de texto para ler o input do usuário
        
        String[] tiposPredefinidos = { "Selecione...", "Comprador", "Vendedor", "Ambos" };
        cbTipo = new JComboBox<>(tiposPredefinidos);
        
        addInputField(painelInputs, "Nome Completo:", txtNome, gbc);
        addInputField(painelInputs, "E-mail:", txtEmail, gbc);
        addInputField(painelInputs, "Documento (CPF/CNPJ):", txtIdGov, gbc);
        addInputField(painelInputs, "Telefone (Apenas números):", txtTelefone, gbc);
        addInputField(painelInputs, "Tipo de Cliente:", cbTipo, gbc);
        gbc.weighty = 1.0; 
        painelInputs.add(new Box.Filler(new Dimension(0,0), new Dimension(0,0), new Dimension(0, Short.MAX_VALUE)), gbc);
        
        // Colocar os inputs em um ScrollPane e BLOQUEAR movimento horizontal
        JScrollPane scrollInputs = new JScrollPane(painelInputs);
        scrollInputs.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER); // Bloqueia movimento horizontal
        scrollInputs.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED); // Forca movimento vertical
        scrollInputs.setBorder(BorderFactory.createEmptyBorder());

        // FORCE the scroll pane to stay exactly within your desired width boundaries
        Dimension tamanhoForm = new Dimension(260, 400); // 260px wide
        scrollInputs.setPreferredSize(tamanhoForm);
        scrollInputs.setMinimumSize(tamanhoForm);
        scrollInputs.setMaximumSize(tamanhoForm);
        
        painelMestreDireita.add(scrollInputs, BorderLayout.CENTER);
        
        // 2. Painel de Controle Fixo (Botões) - Sempre visível embaixo
        JPanel painelControlesFixos = new JPanel(new GridLayout(0, 1, 5, 5));
        painelControlesFixos.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        btnAdd = new JButton("Adicionar");
        btnAtt = new JButton("Atualizar");
        btnDel = new JButton("Deletar");
        btnVolta = new JButton("← Menu Principal");
        
        JPanel painelBotoesAcao = new JPanel(new GridLayout(1, 3, 5, 5));
        painelBotoesAcao.add(btnAdd);
        painelBotoesAcao.add(btnAtt);
        painelBotoesAcao.add(btnDel);
        
        painelControlesFixos.add(painelBotoesAcao);
        painelControlesFixos.add(btnVolta);
        
        painelMestreDireita.add(painelControlesFixos, BorderLayout.SOUTH);
        add(painelMestreDireita, BorderLayout.EAST);
        
        // --- FUNCIONALIDADES ---
        btnVolta.addActionListener(e -> janela.MudarTelas("MENU"));
        atualizarTableGeral();
        
        // EVENTO: Clique na Tabela carrega dados nos campos
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int linhaSelecionada = table.getSelectedRow();
                if (linhaSelecionada != -1) {
                    indexSendoEditado = linhaSelecionada;
                    Cliente c = listaMemoria.get(indexSendoEditado);
                    
                    txtNome.setText(c.getNome());
                    txtEmail.setText(c.getEmail());
                    txtIdGov.setText(c.getIdgov() != null ? c.getIdgov() : "");
                    txtTelefone.setText(String.valueOf(c.getTelefone()));
                    cbTipo.setSelectedItem(c.getTipo());
                }
            }
        });
        
        // AÇÃO: ADICIONAR CLIENTE
        btnAdd.addActionListener(e -> {
            String nome = txtNome.getText().trim();
            String email = txtEmail.getText().trim();
            String idgov = txtIdGov.getText().trim().isEmpty() ? "NÃO INFORMADO" : txtIdGov.getText().trim();
            String telRaw = txtTelefone.getText().trim();
            String tipo = cbTipo.getSelectedItem().toString();
            
            if (nome.isEmpty() || email.isEmpty() || telRaw.isEmpty() || tipo.equals("Selecione...")) {
                JOptionPane.showMessageDialog(this, "Por favor, preencha todos os campos obrigatórios.");
                return;
            }
            
            int arroba = email.indexOf("@");
            if (arroba == -1 || email.indexOf(".", arroba) == -1) {
                JOptionPane.showMessageDialog(this, "Endereço de e-mail inválido!", "Erro de Validação", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            try {
                long telefone = Long.parseLong(telRaw); 
                
                Cliente novo = new Cliente(nome, email, tipo, idgov, telefone);
                HandlerBD.adicionarCliente(novo);
                
                atualizarTableGeral();
                limparFormulario();
                JOptionPane.showMessageDialog(this, "Cliente adicionado com sucesso!");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "O telefone deve conter apenas números válidos (sem traços ou espaços).", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
            } catch (RuntimeException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro de Banco de Dados", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        // AÇÃO: ATUALIZAR CLIENTE
        btnAtt.addActionListener(e -> {
            if (indexSendoEditado == -1) {
                JOptionPane.showMessageDialog(this, "Selecione um cliente na tabela primeiro para editar.");
                return;
            }
            
            String nome = txtNome.getText().trim();
            String email = txtEmail.getText().trim();
            String idgov = txtIdGov.getText().trim().isEmpty() ? "NÃO INFORMADO" : txtIdGov.getText().trim();
            String telRaw = txtTelefone.getText().trim();
            String tipo = cbTipo.getSelectedItem().toString();
            
            if (nome.isEmpty() || email.isEmpty() || telRaw.isEmpty() || tipo.equals("Selecione...")) {
                JOptionPane.showMessageDialog(this, "Verifique se há campos obrigatórios vazios.");
                return;
            }
            
            try {
                long telefone = Long.parseLong(telRaw);
                Cliente c = listaMemoria.get(indexSendoEditado);
                
                c.setNome(nome);
                c.setEmail(email);
                c.setIdgov(idgov);
                c.setTelefone(telefone);
                c.setTipo(tipo);
                
                HandlerBD.salvarClientes(listaMemoria);
                
                atualizarTableGeral();
                limparFormulario();
                JOptionPane.showMessageDialog(this, "Cliente atualizado com sucesso!");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "O telefone deve conter apenas números válidos.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
            } catch (RuntimeException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro ao Atualizar", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        // AÇÃO: DELETAR CLIENTE
        btnDel.addActionListener(e -> {
            int linhaSelecionada = table.getSelectedRow();
            if (linhaSelecionada == -1) {
                JOptionPane.showMessageDialog(this, "Selecione um cliente na tabela para deletar.");
                return;
            }
            
            int confirmacao = JOptionPane.showConfirmDialog(this, "Remover este cliente?", "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);
            
            if (confirmacao == JOptionPane.YES_OPTION) {
                try {
                    listaMemoria.remove(linhaSelecionada);
                    HandlerBD.salvarClientes(listaMemoria);
                    
                    atualizarTableGeral();
                    limparFormulario();
                    JOptionPane.showMessageDialog(this, "Cliente removido!");
                } catch (RuntimeException ex) {
                    JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro de Banco de Dados", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
    
    // --- MÉTODOS AUXILIARES ---
    private void limparFormulario() {
        txtNome.setText("");
        txtEmail.setText("");
        txtIdGov.setText("");
        txtTelefone.setText("");
        cbTipo.setSelectedIndex(0);
        indexSendoEditado = -1;
        table.clearSelection();
    }
    
    private void atualizarTableGeral() {
        tableModelo.setRowCount(0);
        listaMemoria = HandlerBD.getClientes();
        
        for (Cliente c : listaMemoria) {
            tableModelo.addRow(new Object[]{ c.getId(), c.getIdgov(), c.getNome(), c.getEmail(), c.getTelefone(), c.getTipo() });
        }
    }
    
    private void addInputField(JPanel panel, String labelText, JComponent inputComponent, GridBagConstraints gbc) {
        JPanel row = new JPanel(new BorderLayout(0, 2));
        row.setBorder(BorderFactory.createEmptyBorder(0, 10, 8, 10));
        
        // Force the row to stay bounded to the panel width
        row.setMaximumSize(new Dimension(240, row.getPreferredSize().height));
        row.setPreferredSize(new Dimension(240, row.getPreferredSize().height));
        
        JLabel label = new JLabel(labelText);
        row.add(label, BorderLayout.NORTH);
        row.add(inputComponent, BorderLayout.CENTER);
        
        panel.add(row, gbc);
        gbc.gridy++; 
    }
}