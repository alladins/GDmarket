package gdmarket;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="ItemInfo_table")
public class ItemInfo {

        @Id
        @GeneratedValue(strategy=GenerationType.AUTO)
        private Integer itemInfoNo;
        private Integer itemNo;
        private String itemName;
        private String itemStatus;
        private Integer itemPrice;
        private Integer reservationNo;
        private String rentalStatus;
        private Integer customerId;
        private String customerName;

        public Integer getItemInfoNo() {
            return itemInfoNo;
        }
        public void setItemInfoNo(Integer itemInfoNo) {
        this.itemInfoNo = itemInfoNo;
    }
        public Integer getItemNo() {
            return itemNo;
        }
        public void setItemNo(Integer itemNo) {
            this.itemNo = itemNo;
        }
        public String getItemName() {
            return itemName;
        }
        public void setItemName(String itemName) {
            this.itemName = itemName;
        }
        public String getItemStatus() {
            return itemStatus;
        }
        public void setItemStatus(String itemStatus) {
            this.itemStatus = itemStatus;
        }
        public Integer getItemPrice() {
            return itemPrice;
        }
        public void setItemPrice(Integer itemPrice) {
            this.itemPrice = itemPrice;
        }
        public Integer getReservationNo() {
            return reservationNo;
        }
        public void setReservationNo(Integer reservationNo) {
            this.reservationNo = reservationNo;
        }
        public String getRentalStatus() {
            return rentalStatus;
        }
        public void setRentalStatus(String rentalStatus) {
            this.rentalStatus = rentalStatus;
        }
        public Integer getCustomerId() {
            return customerId;
        }
        public void setCustomerId(Integer customerId) {
            this.customerId = customerId;
        }
        public String getCustomerName() {
            return customerName;
        }
        public void setCustomerName(String customerName) {
            this.customerName = customerName;
        }

}
