package gdmarket;

import gdmarket.config.kafka.KafkaProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemInfoViewHandler {


    @Autowired
    private ItemInfoRepository itemInfoRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whenItemRegistered_then_CREATE_1 (@Payload ItemRegistered itemRegistered) {
        try {
            if (itemRegistered.isMe()) {
                // view 객체 생성
                ItemInfo itemInfo= new ItemInfo();
                // view 객체에 이벤트의 Value 를 set 함
                itemInfo.setItemNo(itemRegistered.getItemNo());
                itemInfo.setItemName(itemRegistered.getItemName());
                itemInfo.setItemStatus(itemRegistered.getItemStatus());
                itemInfo.setItemPrice(itemRegistered.getItemPrice());
                // view 레파지 토리에 save
                itemInfoRepository.save(itemInfo);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @StreamListener(KafkaProcessor.INPUT)
    public void whenReserved_then_UPDATE_1(@Payload Reserved reserved) {
        try {
            if (reserved.isMe()) {
                // view 객체 조회
                List<ItemInfo> itemInfoList = itemInfoRepository.findByItemNo(reserved.getItemNo());
                System.out.println("$$$ itemInfo View를 위한 reserved.toJson() : " + reserved.toJson());
                for(ItemInfo itemInfo : itemInfoList) {
                    itemInfo.setItemNo(reserved.getItemNo());
                    itemInfo.setReservationNo(reserved.getReservationNo());
                    itemInfo.setItemStatus(reserved.getItemStatus());
                    itemInfo.setCustomerId(reserved.getCustomerId());
                    itemInfo.setCustomerName(reserved.getCustomerName());
                    itemInfo.setRentalStatus("NotRenting");
                    // view 객체에 이벤트의 eventDirectValue 를 set 함
                    // view 레파지 토리에 save
                    itemInfoRepository.save(itemInfo);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @StreamListener(KafkaProcessor.INPUT)
    public void whenReservationCancelled_then_UPDATE_2(@Payload ReservationCancelled reservationCancelled) {
        try {
            if (reservationCancelled.isMe()) {
                // view 객체 조회
                List<ItemInfo> itemInfoList = itemInfoRepository.findByItemNo(reservationCancelled.getItemNo());
                for(ItemInfo itemInfo : itemInfoList) {
                    itemInfo.setItemStatus(reservationCancelled.getItemStatus());
                    // view 객체에 이벤트의 eventDirectValue 를 set 함
                    // view 레파지 토리에 save
                    itemInfoRepository.save(itemInfo);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @StreamListener(KafkaProcessor.INPUT)
    public void whenRentedItem_then_UPDATE_3(@Payload RentedItem rentedItem) {
        try {
            if (rentedItem.isMe()) {
                // view 객체 조회
                List<ItemInfo> itemInfoList = itemInfoRepository.findByItemNo(rentedItem.getItemNo());
                for(ItemInfo itemInfo : itemInfoList) {
                    itemInfo.setRentalStatus("Renting");
                    // view 객체에 이벤트의 eventDirectValue 를 set 함
                    // view 레파지 토리에 save
                    itemInfoRepository.save(itemInfo);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @StreamListener(KafkaProcessor.INPUT)
    public void whenReturnedItem_then_UPDATE_4(@Payload ReturnedItem returnedItem) {
        try {
            if (returnedItem.isMe()) {
                // view 객체 조회
                List<ItemInfo> itemInfoList = itemInfoRepository.findByItemNo(returnedItem.getItemNo());
                for(ItemInfo itemInfo : itemInfoList) {
                    itemInfo.setRentalStatus("Returned");
                    // view 객체에 이벤트의 eventDirectValue 를 set 함
                    // view 레파지 토리에 save
                    itemInfoRepository.save(itemInfo);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @StreamListener(KafkaProcessor.INPUT)
    public void whenItemDeleted_then_DELETE_1(@Payload ItemDeleted itemDeleted) {
        try {
            if (itemDeleted.isMe()) {
                // view 레파지 토리에 삭제 쿼리
                itemInfoRepository.deleteByItemNo(itemDeleted.getItemNo());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}