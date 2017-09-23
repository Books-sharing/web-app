package com.ninjabooks.service.dao.book;

import com.ninjabooks.domain.Book;
import com.ninjabooks.service.dao.generic.GenericService;

import java.util.stream.Stream;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public interface BookDaoService extends GenericService<Book, Long>
{
    /**
     * Create stream with matching book title.
     *
     * @param title is parameter which is searched
     * @return book stream with matched titles
     */

    Stream<Book> getByTitle(String title);

    /**
     * Create stream with matching book author.
     *
     * @param author is parameter which is searched
     * @return book stream with matched authors
     */

    Stream<Book> getByAuthor(String author);

    /**
     * Create strean with matching book isbn.
     *
     * @param isbn is parameter which is searched
     * @return book stream with matched isbn
     */

    Stream<Book> getByISBN(String isbn);
}
