package Tshop;

import Tshop.config.kafka.KafkaProcessor;
import Tshop.external.Book;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PolicyHandler{
    @Autowired ReservationRepository reservationRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void onStringEventListener(@Payload String eventString){

    }

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverReservationAccepted_StatusChange(@Payload ReservationAccepted reservationAccepted){

        if(reservationAccepted.isMe()){
            System.out.println("##### listener StatusChange : " + reservationAccepted.toJson());

            Optional<Reservation> optionalReservation = reservationRepository.findById(reservationAccepted.getReservationId());
            Reservation reservation = optionalReservation.orElseGet(Reservation::new);
            //Reservation reservation = new Reservation() ;
            reservation.setStatus(reservationAccepted.getStatus());
            reservationRepository.save(reservation);
        }
    }

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverReservationCanceled_StatusChange(@Payload ReservationCanceled reservationCanceled){

        if(reservationCanceled.isMe()){
            System.out.println("##### listener StatusChange : " + reservationCanceled.toJson());
            Optional<Reservation> optionalReservation = reservationRepository.findById(reservationCanceled.getReservationId());
            Reservation reservation = optionalReservation.orElseGet(Reservation::new);
            //Reservation reservation = new Reservation() ;
            reservation.setReservationId(reservationCanceled.getReservationId());
            reservation.setStatus(reservationCanceled.getStatus());
            reservationRepository.save(reservation);
        }
    }

}
