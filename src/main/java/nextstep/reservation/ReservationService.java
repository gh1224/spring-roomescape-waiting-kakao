package nextstep.reservation;

import auth.AuthenticationException;
import auth.AuthorizationException;
import auth.UserDetail;
import lombok.RequiredArgsConstructor;
import nextstep.exceptions.exception.DataAlreadyExistException;
import nextstep.exceptions.exception.DataNotExistException;
import nextstep.member.MemberService;
import nextstep.schedule.Schedule;
import nextstep.schedule.ScheduleDao;
import nextstep.theme.Theme;
import nextstep.theme.ThemeDao;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ReservationService {
    private final ReservationDao reservationDao;
    private final MemberService memberService;
    private final ThemeDao themeDao;
    private final ScheduleDao scheduleDao;

    public Long create(UserDetail userDetail, ReservationRequest reservationRequest) {
        if (userDetail == null) {
            throw new AuthenticationException();
        }
        Schedule schedule = scheduleDao.findById(reservationRequest.getScheduleId());
        if (schedule == null) {
            throw new DataNotExistException("존재하지 않는 스케줄입니다.");
        }

        List<Reservation> reservation = reservationDao.findByScheduleId(schedule.getId());
        if (!reservation.isEmpty()) {
            throw new DataAlreadyExistException("해당 스케줄 예약이 존재합니다.");
        }

        Reservation newReservation = new Reservation(
                schedule,
                memberService.toMember(userDetail)
        );

        return reservationDao.save(newReservation);
    }

    public List<Reservation> findAllByThemeIdAndDate(Long themeId, String date) {
        Theme theme = themeDao.findById(themeId);
        if (theme == null) {
            throw new DataNotExistException("존재하지 않는 테마입니다.");
        }

        return reservationDao.findAllByThemeIdAndDate(themeId, date);
    }

    public void deleteById(UserDetail userDetail, Long id) {
        Reservation reservation = reservationDao.findById(id);
        if (reservation == null) {
            throw new DataNotExistException("존재하지 않는 예약입니다.");
        }
        if (userDetail == null) {
            throw new AuthenticationException();
        }
        if (!reservation.getMember().getId().equals(userDetail.getId())) {
            throw new AuthorizationException();
        }

        reservationDao.deleteById(id);
    }

    public List<ReservationResponse> findAllByMember(UserDetail userDetail) {
        List<Reservation> reservations = reservationDao.findAllByMemberId(userDetail.getId());
        return reservations.stream()
                .map(ReservationResponse::new)
                .toList();
    }
}
