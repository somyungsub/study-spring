package io.ssosso.jpashop1.controller;

import io.ssosso.jpashop1.domain.item.Book;
import io.ssosso.jpashop1.domain.item.Item;
import io.ssosso.jpashop1.service.ItemService;
import jdk.nashorn.internal.objects.annotations.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {

  private final ItemService itemService;

  @GetMapping("/items/new")
  public String itemForm(Model model) {
    model.addAttribute("form", new BookForm());
    return "items/createItemForm";
  }

  @PostMapping("/items/new")
  public String createBook(BookForm form) {
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
  public String itemList(Model model) {
    final List<Item> items = itemService.findItems();
    model.addAttribute("items", items);
    return "items/itemList";
  }

  @GetMapping("/items/{itemId}/edit")
  public String updateItemForm(@PathVariable Long itemId, Model model) {

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

  @PostMapping("/items/{itemId}/edit")
  public String updateItem(@PathVariable String itemId, @ModelAttribute BookForm form) {

//    Book book = new Book();
//    book.setId(form.getId());
//    book.setName(form.getName());
//    book.setPrice(form.getPrice());
//    book.setStockQuantity(form.getStockQuantity());
//    book.setAuthor(form.getAuthor());
//    book.setIsbn(form.getIsbn());
//    itemService.saveItem(book);

    itemService.updateItem(Long.valueOf(itemId), form.getName(), form.getPrice(), form.getStockQuantity());

    return "redirect:/items";
  }
}
