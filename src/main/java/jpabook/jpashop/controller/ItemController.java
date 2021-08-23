package jpabook.jpashop.controller;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/items/new")
    public String createForm(Model model) {
        model.addAttribute("form", new BookForm());
        return "items/createItemForm";
    }

    @PostMapping("/items/new")
    public String create(BookForm form) {   // 원래는 setter 닫고, creatItem 이라는 비즈니스 로직을 Entity 안에 구현해서 create 할 때 사용하는게 더 좋음
        Book book = new Book();
        book.setName(form.getName());
        book.setPrice(form.getPrice());
        book.setStockQuantity(form.getStockQuantity());
        book.setAuthor(form.getAuthor());
        book.setIsbn(form.getIsbn());

        itemService.saveItem(book);
        return "redirect:/";
    }

    @GetMapping("/items")
    public String list(Model model) {
        List<Item> items = itemService.findItems();
        model.addAttribute("items", items);
        return "items/itemList";
    }

    @GetMapping("/items/{itemId}/edit")
    private String updateItemForm(@PathVariable("itemId") Long itemId, Model model) {
        Book item = (Book) itemService.findOne(itemId);

        BookForm form = new BookForm();
        form.setId(item.getId());
        form.setName(item.getName());
        form.setPrice(item.getPrice());
        form.setStockQuantity(item.getStockQuantity());
        form.setAuthor(item.getAuthor());
        form.setIsbn(item.getIsbn());

        model.addAttribute("form", form);
        return "items/updateItemForm";
    }

    /*
    @PostMapping("/items/{itemId}/edit")    // 변경감지 기능을 사용하지 않은 case? repository 에서 직접 merge..?
    private String updateItem(@PathVariable String itemId, @ModelAttribute("form") BookForm form) {

        Book book = new Book();
        book.setId(form.getId());
        book.setName(form.getName());
        book.setPrice(form.getPrice());
        book.setStockQuantity(form.getStockQuantity());
        book.setAuthor(form.getAuthor());
        book.setIsbn(form.getIsbn());
        // 원래 JPA 는 업데이트 로직을 따로 구현하지 않아도 JPA 에서 Entity 값의 변경점을 dirty checking 하여 업데이트 시켜준다?
        // 하지만 준영속 엔티티의 경우 (ex.Book) 수정하려면 변경감지 기능이나 병합을 사용해야 한다..? pdf 파일 참인


        itemService.saveItem(book);
        return "redirect:/items";
    }
    */

    @PostMapping("items/{itemId}/edit") // 변경감지 기능을 사용하여 update 한 설계..?
    public String updateItem(@PathVariable Long itemId, @ModelAttribute("form") BookForm form) {
       itemService.updateItem(itemId, form.getName(), form.getPrice(), form.getStockQuantity());
    return "redirect:/items";
    }

}