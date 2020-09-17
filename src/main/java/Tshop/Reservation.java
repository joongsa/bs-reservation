package Tshop;

import javax.persistence.*;

import Tshop.external.Book;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.BeanUtils;

import java.io.IOException;
import java.util.List;

@Entity
@Table(name="Reservation")
public class Reservation {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long reservationId;
    private Long bookId;
    private String status;

    /**
     * 예약접수를 신청하면 상품수량을 확인하고 예약가능/불가여부를 판단
     * */
    @PrePersist
    public void onPrePersist(){
        //Tshop.external.Book book = new Tshop.external.Book();

        String checkQuantity = ReservationApplication.applicationContext.getBean(Tshop.external.BookService.class).checkBookQuantity(this.getBookId().toString());

        if(Integer.parseInt(checkQuantity) > 0){
            this.setStatus("bookReserveReq");
        }else{
            this.setStatus("bookOutOfStock");
        }
    }
    /**
     * 예약신청 가능이면 배정관리서비스로 예약번호 전송
     * */
    @PostPersist
    public void onPostPersist(){
        ReservationRequested reservationRequested = new ReservationRequested();
        BeanUtils.copyProperties(this, reservationRequested);
        if("bookReserveReq".equals(this.getStatus())) reservationRequested.publishAfterCommit();
    }
    /**
     * 예약취소 이 후 상품재고 원복 및 배정정보 삭제 이벤트 전달
     * */
    @PostUpdate
    public  void onPostUpdate(){
        if("bookCancel".equals(this.getStatus())){
            ReservationCancelRequested reservationCancelRequested = new ReservationCancelRequested();
            BeanUtils.copyProperties(this, reservationCancelRequested);
            reservationCancelRequested.publishAfterCommit();
        }
    }

    public Long getReservationId() {
        return reservationId;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }
    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }




}
