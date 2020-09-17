
package Tshop.external;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

//@FeignClient(name="book", url="http://book:8080")
@FeignClient(name="Book", url="${api.url.book}")
public interface BookService {

    @RequestMapping(method= RequestMethod.GET, path="/books/check",produces = "application/json")
    //public @ResponseBody String checkBookQuantity(@RequestBody Book book);
    @ResponseBody String checkBookQuantity(@RequestParam("bookId") String bookId);

}