# 📋 Checklist Digital

Aplicativo Android desenvolvido em **Kotlin** com **Jetpack Compose**, que permite gerenciar checklists de clientes, veículos e serviços, incluindo captura de fotos e exportação em PDF.

---

## 🚀 Funcionalidades

- **Gerenciamento de Clientes**
  - Cadastro, edição e exclusão de clientes.
  - Validação de dados obrigatórios (nome, telefone, seguradora, etc.).

- **Informações do Veículo**
  - Registro de modelo, placa, cor e ano.
  - Edição e validação de dados.

- **Endereços**
  - Cadastro de origem e destino.
  - Validação de campos obrigatórios.

- **Status do Veículo**
  - **Status 1**: Itens e acessórios (documentos, extintor, rádio, estepe, etc.).
  - **Status 2**: Pneus, nível de combustível e observações.

- **Fotos**
  - Captura de imagens diretamente pela câmera.
  - Armazenamento interno e exibição em grade.
  - Exclusão de fotos individuais.

- **Exportação em PDF**
  - Geração de relatórios completos com:
    - Dados do cliente.
    - Informações do veículo.
    - Endereços.
    - Status do veículo.
    - Fotos capturadas.
  - Layout personalizado com logo e informações da empresa.

---

## 🛠️ Tecnologias Utilizadas

- **Kotlin** + **Jetpack Compose** → UI moderna e declarativa.
- **Android ViewModel** + **StateFlow** → Gerenciamento de estado reativo.
- **Room / Repository Pattern** → Persistência de dados.
- **Coil** → Carregamento de imagens.
- **iText PDF** → Geração de relatórios em PDF.
- **Material 3** → Componentes visuais atualizados.

---

## 📂 Estrutura do Projeto

- `ui/` → Telas e componentes visuais.
- `viewmodel/` → Lógica de negócios e gerenciamento de estado.
- `data/` → Entidades, DAO e repositório.
- `utils/` → Funções auxiliares (ex: exportação PDF).
- `navigation/` → Grafo de navegação entre telas.
- `ChecklistApplication` → Inicialização do container de dependências.
- `MainActivity` → Ponto de entrada da interface.

---

## 📸 Fluxo de Uso

1. **Cadastrar Cliente** → Inserir dados básicos.
2. **Adicionar Veículo** → Informar modelo, placa, cor e ano.
3. **Registrar Endereços** → Origem e destino do serviço.
4. **Preencher Status** → Itens/acessórios e pneus/combustível.
5. **Capturar Fotos** → Usar câmera e salvar imagens.
6. **Exportar PDF** → Gerar relatório completo para impressão ou envio.

---

## 🎨 Interface

- **TopAppBar personalizada** com logo e nome da empresa.
- **Scaffold** para estruturação das telas.
- **LazyVerticalGrid** para exibição das fotos.
- **AlertDialog** para permissões e mensagens de erro.

