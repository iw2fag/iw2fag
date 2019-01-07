package com.iw2fag.lab.kafka;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Properties;

public class KafkaProducerTest {
    public static void main(String[] args) {
        final KafkaProducer<Integer, String> producer;
        final String topic;
        final Properties props = new Properties();
        props.put("bootstrap.servers", "16.187.189.144:9092");
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        producer = new KafkaProducer<>(props);
        topic = "test";

        int messageNo = 1;
        while (messageNo < 5) {
            String messageStr = "Message_" + messageNo;
            System.out.println("Send:" + messageStr);
            producer.send(new ProducerRecord<>(topic, messageStr), new Callback() {
                @Override
                public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                    System.out.println("Complete");
                }
            });
            messageNo++;
          try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }
}
