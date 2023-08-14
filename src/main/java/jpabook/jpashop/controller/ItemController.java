package jpabook.jpashop.controller;


import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/items/new")
    public String createForm(Model model) {
        model.addAttribute("form", new BookForm());
        // new BookForm() 을 넣는 이유는 IDE 에서 validation 체크를 해줄 수 있다. addAttribute 의 Name (form) 과 html 의 사용 변수가 다르면 오류가 표시된다. (강의참조)
        return "items/createItemForm";
    }

    @PostMapping("/items/new")
    public String create(BookForm form) { //Validation 생략

        Book book = new Book();
        // 실무에서는 무조건 setter 를 날리고, 생성자로 주입되도록 설계 및 개발한다 (order 처럼)
        book.setName(form.getName());
        book.setPrice(form.getPrice());
        book.setStockQuantity(form.getStockQuantity());
        book.setAuthor(form.getAuthor());
        book.setIsbn(form.getIsbn());

        itemService.saveItem(book);
        return "redirect:/items";
    }

    @GetMapping("/items")
    public String list(Model model) {
        List<Item> items = itemService.findItems();
        model.addAttribute("items", items);
        return "items/itemList";
    }

    @GetMapping("/items/{itemId}/edit")
    public String updateItemForm(@PathVariable("itemId") Long itemId, Model model) {
        Book item = (Book) itemService.findOne(itemId); // 예제 단순화를 위한 Book 으로 캐스팅 원래는 item 으로 받아야함

        BookForm form = new BookForm(); //update 하는 것에 bookForm 을 보낼거임
        form.setId(item.getId());
        form.setName(item.getName());
        form.setPrice(item.getPrice());
        form.setStockQuantity(item.getStockQuantity());
        form.setAuthor(item.getAuthor());
        form.setIsbn(item.getIsbn());

        model.addAttribute("form", form);
        return "items/updateItemForm";
    }

    @PostMapping("/items/{itemId}/edit")
    public String updateItem(@PathVariable Long itemId, @ModelAttribute("form") BookForm form) {

//        Book book = new Book();
//        book.setId(form.getId());
//        book.setName(form.getName());
//        book.setPrice(form.getPrice());
//        book.setStockQuantity(form.getStockQuantity());
//        book.setAuthor(form.getAuthor());
//        book.setIsbn(form.getIsbn());

        /**
         * bookForm 은 웹 계층에서만 사용하기로 한 클래스이다.
         * 근데 form 을 service 계청으로 넘기면 지저분해진다.
         * >> 위의 set 코드는 Book 을 어설프게 가공하여 만든 것
         *
         * 아래의 updateItem 형식으로 하는게 좋다.
         * 필요한 데이터만 받은 것이다.
         *
         * 컨트롤러에서 어설프게 엔티티를 생성하지 마세요.
         * 트랜잭션이 있는 서비스 계층에 식별자( id )와 변경할 데이터를 명확하게 전달하세요.(파라미터 or dto)
         * 트랜잭션이 있는 서비스 계층에서 영속 상태의 엔티티를 조회하고, 엔티티의 데이터를 직접 변경하세요.
         * 트랜잭션 커밋 시점에 변경 감지가 실행됩니다.
         */

        itemService.updateItem(itemId, form.getName(), form.getPrice(), form.getStockQuantity());
        return "redirect:/items";
    }
}
