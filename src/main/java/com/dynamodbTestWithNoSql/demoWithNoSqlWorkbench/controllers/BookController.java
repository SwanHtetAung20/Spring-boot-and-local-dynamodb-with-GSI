package com.dynamodbTestWithNoSql.demoWithNoSqlWorkbench.controllers;

import com.dynamodbTestWithNoSql.demoWithNoSqlWorkbench.enitities.Book;
import com.dynamodbTestWithNoSql.demoWithNoSqlWorkbench.repositories.BookRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("book")
public class BookController {

    private final BookRepo bookRepo;

    @PostMapping("/save")
    public ResponseEntity<Book> insertHandler(@RequestBody Book book) {
        return new ResponseEntity<>(bookRepo.save(book), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<Book>> getBooksById(@PathVariable("id") String id) {
        return ResponseEntity.ok(bookRepo.findBooksById(id));
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<Book>> getAllBooks() {
        return ResponseEntity.ok(bookRepo.getAllBooks());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteHandlerForBook(@PathVariable("id") String id, @RequestParam(required = false) String date) {

        if (date == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Created date is required");
        }
        return ResponseEntity.status(HttpStatus.OK).body(bookRepo.deleteBook(id, date));
    }

    @PutMapping("/{id}/{date}")
    public ResponseEntity<String> updateHandlerForBook(@PathVariable("id") String id,
                                                       @PathVariable("date") String date,
                                                       @RequestBody Book book) {
        return ResponseEntity.ok(bookRepo.updateBook(id, date, book));
    }
}
