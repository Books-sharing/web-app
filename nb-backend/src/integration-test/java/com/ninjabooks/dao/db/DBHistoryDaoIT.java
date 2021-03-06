package com.ninjabooks.dao.db;

import com.ninjabooks.config.AbstractBaseIT;
import com.ninjabooks.dao.HistoryDao;
import com.ninjabooks.domain.History;

import static com.ninjabooks.util.constants.DomainTestConstants.EXPECTED_RETURN_DATE;
import static com.ninjabooks.util.constants.DomainTestConstants.HISTORY;
import static com.ninjabooks.util.constants.DomainTestConstants.ID;

import java.time.LocalDate;
import java.util.Optional;
import java.util.stream.Stream;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@Transactional
public class DBHistoryDaoIT extends AbstractBaseIT
{
    private static final LocalDate UPDATED_RETURN_DATE = LocalDate.now();

    @Autowired
    private HistoryDao sut;

    @Test
    public void testAddHistory() throws Exception {
        sut.add(HISTORY);
        Stream<History> actual = sut.getAll();

        assertThat(actual).containsExactly(HISTORY);
    }

    @Test
    @Sql(value = "classpath:sql_query/dao_import.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    public void testDeleteHistoryByEnity() throws Exception {
        sut.delete(HISTORY);

        assertThat(sut.getAll()).isEmpty();
    }

    @Test
    @Sql(value = "classpath:sql_query/dao_import.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    public void testGetById() throws Exception {
        Optional<History> actual = sut.getById(ID);

        assertThat(actual).contains(HISTORY);
    }

    @Test
    public void testGetByIdEnityWhichNotExistShouldReturnEmptyOptional() throws Exception {
        Optional<History> actual = sut.getById(ID);

        assertThat(actual).isEmpty();
    }

    @Test
    @Sql(value = "classpath:sql_query/dao_import.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    public void testFindAllHistoriesShouldReturnsAllRecords() throws Exception {
        Stream<History> actual = sut.getAll();

        assertThat(actual).containsExactly(HISTORY);
    }

    @Test
    public void testFindAllOnEmptyDBShouldReturnEmptyStream() throws Exception {
        assertThat(sut.getAll()).isEmpty();
    }

    @Test
    @Sql(value = "classpath:sql_query/dao_import.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    public void testUpdateHistoryByEntity() throws Exception {
        History enityToUpdate = createFreshEntity();
        enityToUpdate.setReturnDate(UPDATED_RETURN_DATE);

        sut.update(enityToUpdate);
        Stream<History> actual = sut.getAll();

        assertThat(actual).containsExactly(enityToUpdate);
    }

    private History createFreshEntity() {
        History enityToUpdate = new History(EXPECTED_RETURN_DATE);
        enityToUpdate.setId(ID);

        return enityToUpdate;
    }
}
