import pika, os

url = os.environ.get('CLOUDAMQP_URL', 'amqp://sexedswv:VFFRkYN2et4N95bCAB9M6OAdg9yS85-8@jackal-01.rmq.cloudamqp.com/sexedswv')
params = pika.URLParameters(url)
connection = pika.BlockingConnection(params)
channel = connection.channel()

exchange_name = 'topic-exchange'
channel.exchange_declare(
    exchange=exchange_name,
    exchange_type='topic',
    durable=False,
    auto_delete=True
)

result = channel.queue_declare(queue='', exclusive=True)
queue_name = result.method.queue

channel.queue_bind(exchange=exchange_name, queue=queue_name, routing_key='estagio.*')

def callback(ch, method, properties, body):
    print("Mensagem Recebida:", body.decode())

channel.basic_consume(queue=queue_name, on_message_callback=callback, auto_ack=True)

print('Aguardando mensagens. Pressione CTRL+C para sair.')
channel.start_consuming()