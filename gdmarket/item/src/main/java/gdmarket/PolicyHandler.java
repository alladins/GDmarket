package gdmarket;

import gdmarket.config.kafka.KafkaProcessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class PolicyHandler{

    @Autowired
    ItemRepository itemManagementRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void onStringEventListener(@Payload String eventString){

    }

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverReserved_(@Payload Reserved reserved){
        if(reserved.isMe()){
            System.out.println("##### listener  : " + reserved.toJson());
            System.out.println("##### reserved itemNo : " + reserved.getItemNo());
            if(reserved.getItemNo() != null && "NotRentable".equals(reserved.getItemStatus())){
                Item item = (Item) itemManagementRepository.findByItemNo(reserved.getItemNo()).get(0);
                item.setItemStatus("NotRentable");
                itemManagementRepository.save(item);
            }
        }
    }

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverReservationCancelled_(@Payload ReservationCancelled reservationCancelled){
        if(reservationCancelled.isMe()){
            System.out.println("##### listener  : " + reservationCancelled.toJson());
            if(reservationCancelled.getItemNo() != null && "Rentable".equals(reservationCancelled.getItemStatus())){
                Item item = (Item) itemManagementRepository.findByItemNo(reservationCancelled.getItemNo()).get(0);
                item.setItemStatus("Rentable");
                itemManagementRepository.save(item);
            }
        }
    }
}
