import logging
from structlog import wrap_logger
from app.async_consumer import AsyncConsumer
from app.response_processor import ResponseProcessor

logger = wrap_logger(logging.getLogger(__name__))


class Consumer(AsyncConsumer):
    def on_message(self, unused_channel, basic_deliver, properties, body):
        logger.info('Received message', delivery_tag=basic_deliver.delivery_tag, app_id=properties.app_id, body=body)
        try:
            processor = ResponseProcessor(logger)
            proccessed_ok = processor.process(body.decode("utf-8"))

            if proccessed_ok:
                self.acknowledge_message(basic_deliver.delivery_tag)

        except Exception as e:
            logger.error("ResponseProcessor failed", exception=e)


def main():
    logger.debug("Starting consumer")
    consumer = Consumer()
    try:
        consumer.run()
    except KeyboardInterrupt:
        consumer.stop()

if __name__ == '__main__':
    main()
