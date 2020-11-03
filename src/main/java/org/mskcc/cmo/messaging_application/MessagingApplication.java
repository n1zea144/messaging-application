package org.mskcc.cmo.messaging_application;

import org.mskcc.cmo.messaging.Gateway;
import org.mskcc.cmo.messaging.MessageConsumer;
import org.mskcc.cmo.messaging.model.SampleMetadata;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "org.mskcc.cmo.messaging")
public class MessagingApplication implements CommandLineRunner, MessageConsumer
{
    @Autowired
    @Qualifier("NATSGateway")
    private Gateway messagingGateway;

    @Override
    public void run(String... args)
    {
        try
        {
            messagingGateway.subscribe("NEW_SAMPLE", SampleMetadata.class, this);
            SampleMetadata sm = new SampleMetadata();
            sm.sampleID = "sampleID here";
            messagingGateway.publish("NEW_SAMPLE", sm);
            Thread.sleep(5000);
            messagingGateway.shutdown();
        }
        catch(Exception e) {}
    }

    @Override
    public void onMessage(Object message)
    {
        SampleMetadata sampleMetadata = (SampleMetadata)message;
        System.out.println("*** Sample Metadata received ***");
        System.out.println("SampleID: " + sampleMetadata.sampleID);
        System.out.println("***                          ***");
    }

    public static void main(String[] args)
    {
        SpringApplication.run(MessagingApplication.class, args);
    }
}
