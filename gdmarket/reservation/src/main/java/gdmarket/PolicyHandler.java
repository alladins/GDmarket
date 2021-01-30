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
    ReservationRepository reservationManagementRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void onStringEventListener(@Payload String eventString){

    }

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverRentedItem_(@Payload RentedItem rentedItem){
        if(rentedItem.isMe()){
            System.out.println("##### listener  : " + rentedItem.toJson());
            System.out.println("##### rentedItem ReservationNo : " + rentedItem.getReservationNo());
            if(rentedItem.getReservationNo() != null && "Renting".equals(rentedItem.getRentalStatus())){
                Reservation reservation = (Reservation) reservationManagementRepository.findByReservationNo(rentedItem.getReservationNo()).get(0);
                reservation.setRentalStatus("Renting");
                reservationManagementRepository.save(reservation);
            }
        }

    }
    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverReturnedItem_(@Payload ReturnedItem returnedItem){

        if(returnedItem.isMe()){
            System.out.println("##### listener  : " + returnedItem.toJson());
        }
    }

}
