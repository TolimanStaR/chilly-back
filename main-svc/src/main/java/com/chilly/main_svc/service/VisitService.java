package com.chilly.main_svc.service;

import com.chilly.main_svc.dto.SaveVisitRequest;
import com.chilly.main_svc.dto.VisitDto;
import com.chilly.main_svc.exception.AccessDeniedException;
import com.chilly.main_svc.exception.NoSuchEntityException;
import com.chilly.main_svc.mapper.VisitDtoMapper;
import com.chilly.main_svc.model.Place;
import com.chilly.main_svc.model.User;
import com.chilly.main_svc.model.Visit;
import com.chilly.main_svc.repository.PlaceRepository;
import com.chilly.main_svc.repository.VisitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VisitService {

    private final VisitRepository visitRepository;
    private final VisitDtoMapper visitDtoMapper;
    private final PlaceRepository placeRepository;
    private final UserService userService;


    public void saveVisit(Long userId, SaveVisitRequest request) {
        User user = userService.findUserOrException(userId);
        Place place = findPlaceOrException(request.getPlaceId());
        Date date = Optional.ofNullable(request.getTimestamp()).orElseGet(Date::new);

        Visit visit = Visit.builder()
                .place(place)
                .user(user)
                .date(date)
                .build();

        visitRepository.save(visit);
    }

    public List<VisitDto> getVisited(Long userId) {
        User user = userService.findUserOrException(userId);
        return user.getVisits().stream()
                .map(visitDtoMapper::toDto)
                .toList();
    }

    private Place findPlaceOrException(Long id) {
        return placeRepository.findById(id)
                .orElseThrow(() -> new NoSuchEntityException("No place with id =" + id));
    }

    public void deleteVisit(Long userId, Long visitId) {
        Visit visit = visitRepository.findById(visitId)
                .orElseThrow(() -> new NoSuchEntityException("No visit with id =" + visitId));

        if (fromOtherUser(visit, userId)) {
            throw new AccessDeniedException("Cannot delete visit of another user");
        }

        visitRepository.deleteById(visitId);
    }

    private boolean fromOtherUser(Visit visit, Long userId) {
        return !Objects.equals(visit.getUser().getId(), userId);
    }
}
