package ledger.testing

import org.apache.kafka.clients.admin.AdminClient
import org.apache.kafka.clients.admin.NewTopic
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import org.testcontainers.kafka.KafkaContainer
import org.testcontainers.utility.DockerImageName
import java.util.Properties

object KafkaFixture {

    private val container = KafkaContainer(DockerImageName.parse("apache/kafka-native:4.0.0"))
        .apply { start() }

    val bootstrapServers: String
        get() = container.bootstrapServers

    fun createTopic(name: String) {
        AdminClient.create(
            mapOf(AdminClientConfigValues.BOOTSTRAP_SERVERS to bootstrapServers),
        ).use { admin ->
            admin.createTopics(listOf(NewTopic(name, 1, 1))).all().get()
        }
    }

    fun producer(): KafkaProducer<String, String> {
        val properties = Properties().apply {
            put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers)
            put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer::class.java.name)
            put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer::class.java.name)
            put(ProducerConfig.ACKS_CONFIG, "all")
            put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true")
        }
        return KafkaProducer(properties)
    }

    fun consumer(groupId: String): KafkaConsumer<String, String> {
        val properties = Properties().apply {
            put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers)
            put(ConsumerConfig.GROUP_ID_CONFIG, groupId)
            put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer::class.java.name)
            put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer::class.java.name)
            put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")
            put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false")
        }
        return KafkaConsumer(properties)
    }

    private object AdminClientConfigValues {
        const val BOOTSTRAP_SERVERS = "bootstrap.servers"
    }
}
