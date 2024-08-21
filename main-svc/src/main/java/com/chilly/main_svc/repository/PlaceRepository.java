package com.chilly.main_svc.repository;

import com.chilly.main_svc.model.Place;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceRepository extends JpaRepository<Place, Long> {
}
