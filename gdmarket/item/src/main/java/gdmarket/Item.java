package gdmarket;

import javax.persistence.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gdmarket.config.kafka.KafkaProcessor;
import org.springframework.beans.BeanUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.util.MimeTypeUtils;

@Entity
@Table(name="Item_table")
public class Item {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer itemNo;
    private String itemName;
    private String itemStatus;
    private Integer itemPrice;
    private String rentalStatus;
    private Integer reservationNo;

    @PostPersist
    public void onPostPersist(){
        ItemRegistered itemRegistered = new ItemRegistered();
        itemRegistered.setItemName(this.getItemName());
        itemRegistered.setItemPrice(this.getItemPrice());
        itemRegistered.setItemStatus("Rentable");
        itemRegistered.setRentalStatus("NotRenting");

        System.out.println("@@@@@@@ ItemRegistered to Json @@@@@@@");
        System.out.println(itemRegistered.toJson());
    }

    @PostUpdate
    public void onPostUpdate() {
        if ("Renting".equals(this.getRentalStatus())) {
            RentedItem rentedItem = new RentedItem();
            rentedItem.setReservationNo(this.getReservationNo());
            rentedItem.setRentalStatus("Renting");

            ObjectMapper objectMapper = new ObjectMapper();
            String json = null;
            try {
                json = objectMapper.writeValueAsString(rentedItem);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("JSON format exception", e);
            }
            KafkaProcessor processor = ItemApplication.applicationContext.getBean(KafkaProcessor.class);
            MessageChannel outputChannel = processor.outboundTopic();
            outputChannel.send(org.springframework.integration.support.MessageBuilder
                    .withPayload(json)
                    .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
                    .build());
            System.out.println("@@@@@@@ rentedItem to Json @@@@@@@");
            System.out.println(rentedItem.toJson());
        }
        if ("Returned".equals(this.getRentalStatus())) {
            ReturnedItem returnedItem = new ReturnedItem();
            returnedItem.setReservationNo(this.getReservationNo());
            returnedItem.setRentalStatus("Returned");

            ObjectMapper objectMapper = new ObjectMapper();
            String json = null;
            try {
                json = objectMapper.writeValueAsString(returnedItem);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("JSON format exception", e);
            }
            KafkaProcessor processor = ItemApplication.applicationContext.getBean(KafkaProcessor.class);
            MessageChannel outputChannel = processor.outboundTopic();
            outputChannel.send(org.springframework.integration.support.MessageBuilder
                    .withPayload(json)
                    .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
                    .build());
            System.out.println("@@@@@@@ returnedItem to Json @@@@@@@");
            System.out.println(returnedItem.toJson());
        }
    }

    @PreRemove
    public void onPreRemove() {
        ItemDeleted itemDeleted = new ItemDeleted();
        BeanUtils.copyProperties(this, itemDeleted);
        itemDeleted.publishAfterCommit();
    }


    public Integer getItemNo () {
        return itemNo;
    }
    public void setItemNo (Integer itemNo){
        this.itemNo = itemNo;
    }

    public String getItemName () {
        return itemName;
    }
    public void setItemName (String itemName){
        this.itemName = itemName;
    }

    public String getItemStatus () {
        return itemStatus;
    }
    public void setItemStatus (String itemStatus){
        this.itemStatus = itemStatus;
    }

    public Integer getItemPrice () {
        return itemPrice;
    }
    public void setItemPrice (Integer itemPrice){
        this.itemPrice = itemPrice;
    }

    private String getRentalStatus () {
        return rentalStatus;
    }
    public void setRentalStatus (String rentalStatus){
        this.rentalStatus = rentalStatus;
    }

    private Integer getReservationNo() {
        return reservationNo;
    }
    public void setReservationNo (Integer reservationNo){
        this.reservationNo = reservationNo;
    }
}