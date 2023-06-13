package com.example.bookservice.query.controller;

import com.example.bookservice.query.model.BookResponseModel;
import com.example.bookservice.query.query.GetAllBooksQuery;
import com.example.bookservice.query.query.GetBookQuery;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/books")
public class BookQueryController {

    private final QueryGateway queryGateway;
    @Autowired
    public BookQueryController(QueryGateway queryGateway) {
        this.queryGateway = queryGateway;
    }

    @GetMapping("/{bookId}")
    public BookResponseModel getBookDetail(@PathVariable String bookId) {
        GetBookQuery getBooksQuery = new GetBookQuery();
        getBooksQuery.setBookId(bookId);

        return queryGateway.query(getBooksQuery,
                        ResponseTypes.instanceOf(BookResponseModel.class))
                .join();
    }
    @GetMapping
    public List<BookResponseModel> getAllBooks(){
        GetAllBooksQuery getAllBooksQuery = new GetAllBooksQuery();
        List<BookResponseModel> list = queryGateway.query(getAllBooksQuery, ResponseTypes.multipleInstancesOf(BookResponseModel.class))
                .join();
        return list;
    }
}
