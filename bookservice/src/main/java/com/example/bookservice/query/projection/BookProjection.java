package com.example.bookservice.query.projection;

import com.example.bookservice.command.data.Book;
import com.example.bookservice.command.data.BookRepository;
import com.example.bookservice.query.model.BookResponseModel;
import com.example.bookservice.query.query.GetAllBooksQuery;
import com.example.bookservice.query.query.GetBookQuery;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BookProjection {
    private final BookRepository bookRepository;

    @Autowired
    public BookProjection(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @QueryHandler
    public BookResponseModel handle(GetBookQuery getBooksQuery) {
        BookResponseModel model = new BookResponseModel();
        Book book = bookRepository.getById(getBooksQuery.getBookId());
        BeanUtils.copyProperties(book, model);

        return model;
    }

    @QueryHandler
    public List<BookResponseModel> handle(GetAllBooksQuery getAllBooksQuery) {
        List<Book> listEntity = bookRepository.findAll();
        List<BookResponseModel> listBook = new ArrayList<>();
        listEntity.forEach(s -> {
            BookResponseModel model = new BookResponseModel();
            BeanUtils.copyProperties(s, model);
            listBook.add(model);
        });
        return listBook;
    }
//    @QueryHandler
//    public BookResponseCommonModel handle(GetDetailsBookQuery getDetailsBookQuery) {
//        BookResponseCommonModel model = new BookResponseCommonModel();
//        Book book = bookRepository.getById(getDetailsBookQuery.getBookId());
//        BeanUtils.copyProperties(book, model);
//
//        return model;
//    }
}
