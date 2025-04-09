import pika
import datetime

host = 'jackal-01.rmq.cloudamqp.com'
username = 'sexedswv'
password = 'VFFRkYN2et4N95bCAB9M6OAdg9yS85-8'
virtual_host = 'sexedswv'

credentials = pika.PlainCredentials(username, password)

parameters = pika.ConnectionParameters(
    host=host,
    port=5672,
    virtual_host=virtual_host,
    credentials=credentials
)

def menu():
    nomeProd = input("Digite seu nome: ")
    while True:
        print("------Vaga à Vista------")
        print("1. Enviar Mensagem")
        print("2. Sair") 
        opcao = input("Escolha uma opção: ")
        if opcao == "1":
            enviarMensagem()
        elif opcao == "2":
            print("Saindo do Vaga à Vista...")
            break
        else:
            print("Opção Inválida!\n")

def enviarMensagem(nomeProd):
    connection = pika.BlockingConnection(parameters)
    channel = connection.channel()

    exchange_name = 'topic-exchange'
    channel.exchange_declare(
        exchange=exchange_name,
        exchange_type='topic',
        durable=False,
        auto_delete=True
    )

    areaInteresse = input(
        "Escolha a área da vaga de interesse:\n"
        "1. Desenvolvedor FullStack\n"
        "2. Desenvolvedor Frontend\n"
        "3. Desenvolvedor Backend\n"
        "4. Ciência de Dados\n"
        "5. Cybersegurança\n"
        "Opção: "
    )

    if areaInteresse == "1":
        area = "fullstack"
    elif areaInteresse == "2":
        area = "frontend"
    elif areaInteresse == "3":
        area = "backend"
    elif areaInteresse == "4":
        area = "dados"
    elif areaInteresse == "5":
        area = "cyber"
    else:
        print("Opção Inválida!\n")
        connection.close()
        return

    body = input("Digite a Descrição da Vaga de Estágio: ")

    # Define o routing key conforme a área escolhida
    routing_key = f"estagio.{area}"

    data_hora = datetime.datetime.now().strftime("%d/%m/%Y - %H:%M")
    mensagem = f"[{data_hora}] {nomeProd} : Vaga para {area} - {body}"

    channel.basic_publish(
        exchange=exchange_name,
        routing_key=routing_key,
        body=mensagem
    )

    print("Mensagem enviada:", mensagem)
    connection.close()

if __name__ == "__main__":
    menu()
