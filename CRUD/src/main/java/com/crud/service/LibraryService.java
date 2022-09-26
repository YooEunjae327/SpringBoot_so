package com.crud.service;

import com.crud.model.Author;
import com.crud.model.Book;
import com.crud.model.Member;
import com.crud.model.request.BookCreationRequest;
import com.crud.model.request.MemberCreationRequest;
import com.crud.repository.AuthorRepository;
import com.crud.repository.BookRepository;
import com.crud.repository.LendRepository;
import com.crud.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.action.internal.EntityActionVetoException;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LibraryService {

    private final AuthorRepository authorRepository;
    private final MemberRepository memberRepository;
    private final LendRepository lendRepository;
    private final BookRepository bookRepository;

    public Book readBook(Long id) {
        Optional<Book> book = bookRepository.findById(id);

        if(book.isPresent()) {
            return book.get();
        }

        throw new EntityNotFoundException(
                "Can't find any book under given ID"
        );
    }

    public List<Book> readBooks() {
        return bookRepository.findAll();
    }

    public Book readBook(String isbn) {
        Optional<Book> book = bookRepository.findByIsbn(isbn);
        if(book.isPresent()) {
            return book.get();
        }

        throw new EntityNotFoundException(
                "Can't find any book under given ISBN"
        );
    }

    public Book createBook(BookCreationRequest book) {
        Optional<Author> author = authorRepository.findById(book.getAuthorId());
        if(!author.isPresent()) {
            throw new EntityNotFoundException(
                    "Author Not Found"
            );
        }

        Book bookToCreate = new Book();
        BeanUtils.copyProperties(book, bookToCreate);
        bookToCreate.setAuthor(author.get());
        return bookRepository.save(bookToCreate);
    }

    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }

    public Member createMember(MemberCreationRequest request) {
        Member member = new Member();
        BeanUtils.copyProperties(request, member);
        return memberRepository.save(member)
    }

    // public Member updateMember (Long id)

}
