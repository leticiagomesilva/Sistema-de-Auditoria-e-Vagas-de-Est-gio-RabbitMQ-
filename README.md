# **Sistema de Auditoria e Vagas de Estágio (RabbitMQ)**

Este sistema utiliza o **RabbitMQ** para enviar e receber mensagens sobre **vagas de estágio** em diferentes áreas. O sistema é dividido em três partes principais:

1. **Auditoria** (Python): Recebe todas as mensagens publicadas no RabbitMQ.
2. **Produtor** (Python): Envia vagas de estágio para o RabbitMQ.
3. **Consumidor** (Java): Consome as mensagens de acordo com a área de interesse escolhida pelo usuário.

## **Estrutura do Sistema**

- **Auditoria**: Recebe todas as mensagens enviadas para o RabbitMQ e exibe as mensagens na tela.
- **Produtor**: Envia mensagens para o RabbitMQ de acordo com a área de estágio selecionada.
- **Consumidor**: Escuta as mensagens de uma fila específica, de acordo com a área de estágio selecionada.

## **Requisitos**

Antes de começar, você precisará de:

- **Python 3.x** (para o script de auditoria e produtor)
- **Java 11+** (para o consumidor)
- **RabbitMQ** ou **CloudAMQP** configurado

## **Execução**

### **1. Auditoria (Consumidor Python)**

O **consumidor de auditoria** irá escutar todas as mensagens enviadas ao RabbitMQ. Ele vai receber todas as mensagens, independentemente da área de estágio, utilizando a routing key `#`.

### **Executando o Consumidor de Auditoria:**

1. Instale o pacote `pika` 
    
    ```bash
    pip install pika
    
    ```
    
2. Execute o código do consumidor de auditoria (Python) para começar a escutar todas as mensagens enviadas para o RabbitMQ
    
    ```bash
    
    python auditoria.py
    
    ```
    
3. O código ficará aguardando novas mensagens e exibirá todas as mensagens que chegarem. Você verá uma saída como esta:
    
    ```
    AUDITORIA: Mensagem recebida com routing_key 'estagio.backend.vaga': [12/04/2025 - 10:15] João : Vaga para Backend - Desenvolvedor Java
    ```
    

### **2. Publicação (Produtor Python)**

O **produtor** envia mensagens para o RabbitMQ, onde especifica a **área** de interesse da vaga de estágio (por exemplo, `fullstack`, `frontend`, `backend` etc.)

### **Executando o Produtor:**

1. Execute o script Python para enviar uma mensagem de vaga para o RabbitMQ.
    
    ```bash
    python Publish.py
    ```
    
2. O programa solicitará que você insira seu nome, escolha a área da vaga e forneça a descrição da vaga. Exemplo:
    
    ```
    Digite seu nome: João
    Escolha a área da vaga de interesse:
    1. Desenvolvedor FullStack
    2. Desenvolvedor Frontend
    3. Desenvolvedor Backend
    4. Ciência de Dados
    5. Cybersegurança
    Opção: 1
    Digite a Descrição da Vaga de Estágio: Vaga para desenvolvedor FullStack com experiência em React.
    ```
    
3. O produtor envia a mensagem para a **routing key** apropriada (`estagio.fullstack`, `estagio.frontend`, etc.) para o RabbitMQ.

### **3. Consumidor (Java)**

O **consumidor Java** escuta as mensagens de uma fila específica. O usuário pode escolher qual área de estágio deseja escutar (FullStack, Frontend, etc.).

### **Executando o Consumidor:**

1. Compile e execute o consumidor Java.
    
    ```bash
    mvn clean install
    mvn spring-boot:run
    ```
    
2. O programa pedirá que o usuário escolha uma área para começar a escutar (por exemplo, FullStack, Frontend, etc.).
3. O **consumidor** irá escutar a fila associada à área escolhida e exibirá as mensagens recebidas. Exemplo de saída:
    
    ```
    Conectando à fila: fila-fullstack
    Mensagem recebida: [12/04/2025 - 10:15] João : Vaga para FullStack - Desenvolvedor React
    ```
    

### **4. Fluxo Completo:**

1. O **produtor** envia uma vaga de estágio com a routing key apropriada (ex: `estagio.fullstack`).
2. O **consumidor** escuta a fila associada à área de estágio e exibe a mensagem quando ela chega.
3. O **consumidor de auditoria** (Python) escuta todas as mensagens, independentemente da área de estágio.

## **Considerações Finais**

- **RabbitMQ**: Se você não possui um servidor RabbitMQ local, você pode usar o **CloudAMQP** para configurar um serviço RabbitMQ na nuvem.
- **Troca de Mensagens**: A troca é do tipo **topic**, o que permite que diferentes consumidores se inscrevam nas mensagens por área de interesse usando **routing keys** específicas.

Para garantir que tudo funcione corretamente, verifique se o **RabbitMQ** está rodando e configurado corretamente, e se você tem as dependências necessárias instaladas. Se houver algum erro de configuração ou de conexão, verifique as credenciais do RabbitMQ e as configurações de rede.
