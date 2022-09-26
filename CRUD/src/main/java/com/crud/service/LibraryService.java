package com.crud.service;

import com.crud.model.*;
import com.crud.model.request.AuthorCreationRequest;
import com.crud.model.request.BookCreationRequest;
import com.crud.model.request.MemberCreationRequest;
import com.crud.repository.AuthorRepository;
import com.crud.repository.BookRepository;
import com.crud.repository.LendRepository;
import com.crud.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
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

        if (book.isPresent()) {
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
        if (book.isPresent()) {
            return book.get();
        }

        throw new EntityNotFoundException(
                "Can't find any book under given ISBN"
        );
    }

    public Book createBook(BookCreationRequest book) {
        Optional<Author> author = authorRepository.findById(book.getAuthorId());
        if (author.isEmpty()) {
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
        return memberRepository.save(member);
    }

    public Member updateMember(Long id, MemberCreationRequest request) {
        Optional<Member> optionalMember = memberRepository.findById(id);
        if (optionalMember.isPresent()) {
            throw new EntityNotFoundException(
                    "Member not present in the database"
            );
        }

        Member member = optionalMember.get();
        member.setLastName(request.getLastName());
        member.setFirstName(request.getFirstName());
        return memberRepository.save(member);
    }

    public Author createAuthor(AuthorCreationRequest request) {
        Author author = new Author();
        BeanUtils.copyProperties(request, author);
        return authorRepository.save(author);
    }

    public List<String> lendBook(List<BookCreationRequest> list) {
        List<String> booksApprovedToBurrow = new ArrayList<>();
        list.forEach(bookCreationRequest -> {
            Optional<Book> bookForId = bookRepository.findById(bookCreationRequest.getAuthorId());
            if (bookForId.isEmpty()) {
                throw new EntityNotFoundException(
                        "Can't find any book under given ID"
                );
            }

            Optional<Member> memberForId = memberRepository.findById(bookCreationRequest.getAuthorId());
            if(memberForId.isEmpty()) {
                throw new EntityNotFoundException(
                        "Member not present in the database"
                );
            }

            Member member = memberForId.get();
            if(member.getStatus() != MemberStatus.ACTIVE) {
                throw new RuntimeException(
                        "User id not active to proceed a lending."
                );
            }

            Optional<Lend> burrowedBook = lendRepository.findByBookAndStatus(bookForId.get(),LendStatus.BURROWED);
            if(burrowedBook.isEmpty()) {
                booksApprovedToBurrow.add(bookForId.get().getName());
                Lend lend = new Lend();
                lend.setMember(memberForId.get());
                lend.setBook(bookForId.get());
                lend.setStatus(LendStatus.BURROWED);
                lend.setStartOn(Instant.now());
                lend.setDueOn(Instant.now().plus(30, ChronoUnit.DAYS));
                lendRepository.save(lend);
            }
        });

        return booksApprovedToBurrow;
    }


}
