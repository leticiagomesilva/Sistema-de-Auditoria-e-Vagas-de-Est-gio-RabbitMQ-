package com.example.ConsApplication;

import java.util.Random;
import java.util.Scanner;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ConsApplication {
	static Random random = new Random();

    // Nome do exchange, filas e routing keys
    static final String topicExchangeName = "topic-exchange";
    
    static final String queueNameFullstack = "fila-fullstack" + random.nextInt(100000);
    static final String routingKeyFullstack = "estagio.fullstack";
    
    static final String queueNameFrontend = "fila-frontend" + random.nextInt(100000);
    static final String routingKeyFrontend = "estagio.frontend";
    
    static final String queueNameBackend = "fila-backend" + random.nextInt(100000);
    static final String routingKeyBackend = "estagio.backend";
    
    static final String queueNameDados = "fila-dados" + random.nextInt(100000);
    static final String routingKeyDados = "estagio.dados";
    
    static final String queueNameCyber = "fila-cyber" + random.nextInt(100000);
    static final String routingKeyCyber = "estagio.cyber";
    
    static final String queueNameAll = "fila-all" + random.nextInt(100000);
    static final String routingKeyAll = "estagio.*";

    // Declaração das filas
    @Bean
    Queue filaFullstack() {
        return new Queue(queueNameFullstack, false, false, true);
    }

    @Bean
    Queue filaFrontend() {
        return new Queue(queueNameFrontend, false, false, true);
    }
    
    @Bean
    Queue filaBackend() {
        return new Queue(queueNameBackend, false, false, true);
    }
    
    @Bean
    Queue filaDados() {
        return new Queue(queueNameDados, false, false, true);
    }
    
    @Bean
    Queue filaCyber() {
        return new Queue(queueNameCyber, false, false, true);
    }
    
    @Bean
    Queue filaAll() {
        return new Queue(queueNameAll, false, false, true);
    }

    // Declaração do TopicExchange
    @Bean
    TopicExchange exchange() {
        return new TopicExchange(topicExchangeName, false, true);
    }

    // Bindings das filas com a exchange
    @Bean
    Binding bindingFullstack(Queue filaFullstack, TopicExchange exchange) {
        return BindingBuilder.bind(filaFullstack).to(exchange).with(routingKeyFullstack);
    }

    @Bean
    Binding bindingFrontend(Queue filaFrontend, TopicExchange exchange) {
        return BindingBuilder.bind(filaFrontend).to(exchange).with(routingKeyFrontend);
    }
    
    @Bean
    Binding bindingBackend(Queue filaBackend, TopicExchange exchange) {
        return BindingBuilder.bind(filaBackend).to(exchange).with(routingKeyBackend);
    }
    
    @Bean
    Binding bindingDados(Queue filaDados, TopicExchange exchange) {
        return BindingBuilder.bind(filaDados).to(exchange).with(routingKeyDados);
    }
    
    @Bean
    Binding bindingCyber(Queue filaCyber, TopicExchange exchange) {
        return BindingBuilder.bind(filaCyber).to(exchange).with(routingKeyCyber);
    }
    
    @Bean
    Binding bindingAll(Queue filaAll, TopicExchange exchange) {
        return BindingBuilder.bind(filaAll).to(exchange).with(routingKeyAll);
    }

    // Container que escuta apenas a fila selecionada
    // O valor padrão é "fila-all" para receber todas as mensagens
    @Bean
    SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
                                               MessageListenerAdapter listenerAdapter,
                                               @Value("${selected.queue:fila-all}") String selectedQueue) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(selectedQueue);
        container.setMessageListener(listenerAdapter);
        return container;
    }

    // Configuração do listener que chamará o método "receiveMessage" do bean Receiver
    @Bean
    MessageListenerAdapter listenerAdapter(Receiver receiver) {
        return new MessageListenerAdapter(receiver, "receiveMessage");
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Selecione o Canal de Transmissão:");
        System.out.println("1 - Desenvolvedor Fullstack");
        System.out.println("2 - Desenvolvedor Frontend");
        System.out.println("3 - Desenvolvedor Backend");
        System.out.println("4 - Cientista de Dados");
        System.out.println("5 - Cybersegurança");
        System.out.println("6 - Todos os Canais!");
        
        String escolha = scanner.nextLine();
        // Fila padrão é a que recebe todas as mensagens
        String selectedQueue = queueNameAll;

        switch (escolha.trim()) {
            case "1":
                selectedQueue = queueNameFullstack;
                break;
            case "2":
                selectedQueue = queueNameFrontend;
                break;
            case "3":
                selectedQueue = queueNameBackend;
                break;
            case "4":
                selectedQueue = queueNameDados;
                break;
            case "5":
                selectedQueue = queueNameCyber;
                break;
            case "6":
                selectedQueue = queueNameAll;
                break;
            default:
                System.out.println("Opção inválida, conectando à fila padrão All: " + selectedQueue);
                break;
        }
        System.out.println("Conectando à fila: " + selectedQueue);
        System.setProperty("selected.queue", selectedQueue);
        SpringApplication.run(ConsApplication.class, args);
    }
}
