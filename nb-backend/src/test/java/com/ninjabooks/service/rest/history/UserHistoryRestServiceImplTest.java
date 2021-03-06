package com.ninjabooks.service.rest.history;

import com.ninjabooks.domain.User;
import com.ninjabooks.json.history.GenericHistoryResponse;
import com.ninjabooks.service.dao.user.UserService;
import com.ninjabooks.util.CommonUtils;

import static com.ninjabooks.util.constants.DomainTestConstants.AUTHOR;
import static com.ninjabooks.util.constants.DomainTestConstants.EXPECTED_RETURN_DATE;
import static com.ninjabooks.util.constants.DomainTestConstants.ID;
import static com.ninjabooks.util.constants.DomainTestConstants.ISBN;
import static com.ninjabooks.util.constants.DomainTestConstants.TITLE;
import static com.ninjabooks.util.constants.DomainTestConstants.USER;
import static com.ninjabooks.util.constants.DomainTestConstants.USER_FULL;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.modelmapper.ModelMapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.groups.Tuple.tuple;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class UserHistoryRestServiceImplTest
{
    private static final Optional<User> USER_OPTIONAL = CommonUtils.asOptional(USER_FULL);
    private static final long MINUS_NUMBER_OF_DAY = 10L;
    private static final long MINUS_ZERO_DAY = 0L;
    private static final int EXPECTED_SIZE = 1;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule().silent();

    @Mock
    private UserService userServiceMock;

    private HistoryRestService sut;

    @Before
    public void setUp() throws Exception {
        this.sut = new UserHistoryRestServiceImpl(userServiceMock, new ModelMapper());
        when(userServiceMock.getById(ID)).thenReturn(USER_OPTIONAL);
    }

    @Test
    public void testGetHistoryShouldReturnListWithExpectedSize() throws Exception {
        List<GenericHistoryResponse> actual = sut.getHistory(MINUS_ZERO_DAY, ID);

        assertThat(actual).hasSize(EXPECTED_SIZE);
        verify(userServiceMock, atLeastOnce()).getById(any());
    }

    @Test
    public void testGetHistoryShouldReturnExpectedDtoFields() throws Exception {
        List<GenericHistoryResponse> actual = sut.getHistory(MINUS_ZERO_DAY, ID);

        assertThat(actual)
            .extracting("historyDto.returnDate", "bookDto.author", "bookDto.isbn", "bookDto.title")
            .containsExactly(tuple(EXPECTED_RETURN_DATE, AUTHOR, ISBN, TITLE));
        verify(userServiceMock, atLeastOnce()).getById(any());
    }

    @Test
    public void testGetHistoryShouldReturnEmptyListWhenUserDontHaveAnyHistory() throws Exception {
        when(userServiceMock.getById(ID)).thenReturn(Optional.ofNullable(USER));
        List<GenericHistoryResponse> actual = sut.getHistory(MINUS_ZERO_DAY, ID);

        assertThat(actual).isEmpty();
        verify(userServiceMock, atLeastOnce()).getById(any());
    }

    @Test
    public void testGetHistoryShouldReturnEmptyListWhenMinusDayIsLarge() throws Exception {
        List<GenericHistoryResponse> actual = sut.getHistory(MINUS_NUMBER_OF_DAY, ID);

        assertThat(actual).isEmpty();
        verify(userServiceMock, atLeastOnce()).getById(any());
    }

    @Test
    public void testGetHistoryShouldThrowsExceptionWhenUnableToFindUser() throws Exception {
        when(userServiceMock.getById(ID)).thenReturn(Optional.empty());

        assertThatExceptionOfType(EntityNotFoundException.class)
            .isThrownBy(() -> sut.getHistory(MINUS_ZERO_DAY, ID))
            .withNoCause();

        verify(userServiceMock, atLeastOnce()).getById(any());
    }
}
