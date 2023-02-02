package nextstep.reservation_waiting;

import nextstep.reservation.ReservationDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Sql("classpath:reservation_data.sql")
public class ReservationWaitingDaoTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private ReservationDao reservationDao;

    @BeforeEach
    void setUp() {
        reservationDao = new ReservationDao(jdbcTemplate);
    }

    @DisplayName("멤버 아이디로 예약 대기를 조회한다.")
    @ParameterizedTest
    @MethodSource
    void testFindAllByMember(Long memberId, Integer... expectedWaitNums) {
        assertThat(reservationDao.findAllWaitingByMemberId(memberId))
                .hasSize(expectedWaitNums.length)
                .map(ReservationWaiting::getWaitNum)
                .containsExactly(expectedWaitNums);
    }

    private static Stream<Arguments> testFindAllByMember() {
        return Stream.of(
                Arguments.of(1L, new Integer[]{1}),
                Arguments.of(2L, new Integer[]{1, 1}),
                Arguments.of(3L, new Integer[]{2, 2, 2})
        );
    }
}
