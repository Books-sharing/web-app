package com.ninjabooks.security.service.auth;

import com.ninjabooks.config.AbstractBaseIT;
import com.ninjabooks.json.authentication.AuthenticationRequest;
import com.ninjabooks.security.user.SpringSecurityUser;
import com.ninjabooks.security.utils.TokenUtils;
import com.ninjabooks.utils.TestDevice;

import static com.ninjabooks.util.constants.DomainTestConstants.EMAIL;
import static com.ninjabooks.util.constants.DomainTestConstants.PLAIN_PASSWORD;

import java.util.Optional;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@Sql(value = "classpath:sql_query/it_auth_import.sql", executionPhase = BEFORE_TEST_METHOD)
public class AuthenticationServiceImplIT extends AbstractBaseIT
{
    private static final String SECURITY_PATTERN = "Bearer ";
    private static final String TRUNCATE_TABLE = "TRUNCATE TABLE USER ; ";
    private static final String OLD_TOKEN =
        "Bearer " +
            "eyJhbGciOiJIUzUxMiJ9." +
            "eyJzdWIiOiJqb2huLmRlZUBleG1hcGxlLmNvbSIsImF1ZGllbmNlIjoidW5rbm93biIsImNyZWF0ZWQiOjE1MTQ4MTAzMDUwMTQsImV" +
            "4cCI6MTUxNTQxNTEwNX0." +
            "YllmHQp_Uq8hOucwtTLA0XqSP9X4bLU0Yrp4szOqULjM9TJ-9lYzxa9fUvVCjS2BrH4CRoxqqA9pvtjX91r6Ow";

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private AuthenticationService sut;

    @Test
    public void testAuthUserShouldReturnUser() throws Exception {
        UserDetails actual = sut.authUser(createAuthRequest());

        assertThat(actual).extracting("username").containsExactly(EMAIL);
    }

    @Test
    @Sql(value = "classpath:sql_query/it_auth_import.sql", statements = TRUNCATE_TABLE, executionPhase = BEFORE_TEST_METHOD)
    public void testAuthUserShouldThrowsExceptionWhenUserDataNotValid() throws Exception {
        assertThatExceptionOfType(AuthenticationException.class)
            .isThrownBy(() -> sut.authUser(createAuthRequest()))
            .withNoCause();
    }

    @Test
    public void testRefreshTokenShouldReturnNewToken() throws Exception {
        String token = generateToken();
        Optional<String> actual = sut.refreshToken(token);

        assertSoftly(softly -> {
            assertThat(actual).isPresent();
            assertThat(actual).hasValueSatisfying(s -> {
                String t = token.replaceAll(SECURITY_PATTERN, "");
                assertThat(s).isNotEqualTo(t);
            });
        });
    }

    @Test
    @Ignore("Fresh token")
    public void testRefreshTokenShoulReturnEmptyToken() throws Exception {
        Optional<String> actual = sut.refreshToken(OLD_TOKEN);

        assertThat(actual).isEmpty();
    }

    @Test
    public void testGetAuthUserShouldReturnExpectedSpringUser() throws Exception {
        SpringSecurityUser actual = sut.getAuthUser(generateToken());

        assertThat(actual).extracting("username").containsExactly(EMAIL);
    }

    private String generateToken() {
        UserDetails userDetails = userDetailsService.loadUserByUsername(EMAIL);
        String token = tokenUtils.generateToken(userDetails, TestDevice.createDevice());
        return SECURITY_PATTERN + token;
    }

    private AuthenticationRequest createAuthRequest() {
        return new AuthenticationRequest(EMAIL, PLAIN_PASSWORD);
    }

}
