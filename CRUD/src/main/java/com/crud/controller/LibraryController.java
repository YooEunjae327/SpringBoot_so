package com.crud.controller;

import com.crud.model.Book;
import com.crud.service.LibraryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "api/library")
@RequiredArgsConstructor
public class LibraryController {
    private final LibraryService libraryService;

    @GetMapping("/book")
    public ResponseEntity reedBooks(@RequestParam(required = false) String isbn) {
        if (isbn == null) {
            return ResponseEntity.ok(libraryService.readBooks());
        }
        return ResponseEntity.ok(libraryService.readBook(isbn));
    }

    @GetMapping("book/{bookId}")
    public ResponseEntity<Book> readBook(@PathVariable Long bookId) {
        return ResponseEntity.ok(libraryService.readBook(bookId));
    }
}
